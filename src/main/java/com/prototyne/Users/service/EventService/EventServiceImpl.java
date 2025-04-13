package com.prototyne.Users.service.EventService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.Users.converter.ProductConverter;
import com.prototyne.domain.Event;
import com.prototyne.domain.Investment;
import com.prototyne.domain.Product;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.ProductCategory;
import com.prototyne.repository.EventRepository;
import com.prototyne.repository.HeartRepository;
import com.prototyne.repository.InvestmentRepository;
import com.prototyne.repository.UserRepository;
import com.prototyne.config.JwtManager;
import com.prototyne.Users.web.dto.ProductDTO;
import com.prototyne.repository.querydsl.EventRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service("usersEventServiceImpl")
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final InvestmentRepository investmentRepository;
    private final HeartRepository heartRepository;
    private final JwtManager jwtManager;

    @Override
    public ProductDTO.HomeResponse getHomeByLimit(String accessToken, Integer popular, Integer imminent, Integer latest) {
        // 유저 아이디 객체 가져옴
        Long userId = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("해당하는 회원이 존재하지 않습니다."));

        // 인기순
        List<ProductDTO.EventDTO> pList = eventRepository.findAllEventsByLimit("popular", popular)
                .stream()
                .map(event -> {
                    boolean isBookmarked = heartRepository.findFirstByUserIdAndEvent(userId, event).isPresent();
                    return ProductConverter.toEvent(event, isBookmarked);
                })
                .collect(Collectors.toList());

        // 임박순
        List<ProductDTO.EventDTO> iList = eventRepository.findAllEventsByLimit("imminent", imminent)
                .stream()
                .map(event -> {
                    boolean isBookmarked = heartRepository.findFirstByUserIdAndEvent(userId, event).isPresent();
                    return ProductConverter.toEvent(event, isBookmarked);
                })
                .collect(Collectors.toList());

        // 최신순
        List<ProductDTO.EventDTO> lList = eventRepository.findAllEventsByLimit("new", latest)
                .stream()
                .map(event -> {
                    boolean isBookmarked = heartRepository.findFirstByUserIdAndEvent(userId, event).isPresent();
                    return ProductConverter.toEvent(event, isBookmarked);
                })
                .collect(Collectors.toList());

        return ProductConverter.toHomeResponse(user, pList, iList, lList);
    }

    // 홈 화면 더보기 조회
    // type에 따라 시제품 이벤트 리스트 반환
    @Override
    public List<ProductDTO.EventDTO> getEventsByType(Long userId, String type, String cursor, Integer pageSize) {

        // 유저 아이디 확인
        userRepository.findById(userId).orElseThrow(() -> new TempHandler(ErrorStatus.LOGIN_ERROR_ID));

        List<Event> events = eventRepository.findAllEventsByType(type, cursor, pageSize);

        // 이번 주에 등록된 시제품이 없을 경우
        if ("new".equals(type) && (events == null || events.isEmpty())) {
            throw new TempHandler(ErrorStatus.PRODUCT_ERROR_NEW);
        }

        return events.stream()
                .map(event -> {
                    // 북마크 상태 확인
                    boolean isBookmarked = heartRepository.findFirstByUserIdAndEvent(userId, event).isPresent();
                    return ProductConverter.toEvent(event, isBookmarked);
                })
                .collect(Collectors.toList());
    }

    @Override
    public String getNextCursor(List<ProductDTO.EventDTO> events, String type) {
        // 리스트가 비어있으면 null 반환
        if (events.isEmpty()) {
            return null;
        }

        // 마지막 이벤트 가져오기
        ProductDTO.EventDTO lastEvent = events.get(events.size() - 1);

        // 정렬 기준에 따라 커서 생성
        switch (type) {
            case "popular":
                // 투자자 수, 등록일 기준
                return lastEvent.getInvestCount() + "," + lastEvent.getCreatedAt();
            case "imminent":
                // 마감일, 등록일 기준
                return lastEvent.getEventEnd() + "," + lastEvent.getCreatedAt();
            case "new":
                // 등록일 기준
                return lastEvent.getCreatedAt().toString();
            default:
                throw new TempHandler(ErrorStatus.PRODUCT_ERROR_TYPE);
        }
    }

    @Override
    public List<ProductDTO.SearchResponse> getEventsBySearch(String accessToken, String name) {
        Long userId = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당하는 회원이 존재하지 않습니다."));

        addSearchTerm(user, name);


        // 신청 진행 중인 시제품 이벤트만 가져옴
        LocalDate now = LocalDate.now();
        List<Event> events = eventRepository.findAllByProductNameContaining(name).stream()
                .filter(event -> now.isAfter(event.getEventStart()) && now.isBefore(event.getEventEnd()))
                .collect(Collectors.toList());

        return events.stream()
                .map(event -> {
                    Product product = event.getProduct();
                    int dDay = calculateDDay(now, event.getEventEnd());
                    // 북마크 상태 확인
                    boolean isBookmarked = heartRepository.findFirstByUserIdAndEvent(userId, event).isPresent();
                    return ProductConverter.toSearch(event, product, dDay, isBookmarked);
                })
                .collect(Collectors.toList());
    }

    private void addSearchTerm(User user, String searchTerm) {
        List<String> recentSearchList = user.getRecentSearchList();
        recentSearchList.remove(searchTerm);
        recentSearchList.add(0, searchTerm);

        if (recentSearchList.size() > 10) {
            recentSearchList.remove(recentSearchList.size() - 1);
        }
        userRepository.save(user);
    }

    @Override
    public List<String> getRecentSearches(String accessToken) {
        Long userId = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당하는 회원이 존재하지 않습니다."));

        return user.getRecentSearchList();
    }

    @Override
    public List<String> deleteSearchHistory(String searchTerm, String accessToken) {
        Long userId = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당하는 회원이 존재하지 않습니다."));

        user.getRecentSearchList().remove(searchTerm);
        userRepository.save(user);
        return user.getRecentSearchList(); // 업데이트된 최근검색어 목록 10개 반환
    }

    @Override
    public List<String> deleteAllSearchHistory(String accessToken) {
        Long userId = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당하는 회원이 존재하지 않습니다."));

        user.getRecentSearchList().clear();
        userRepository.save(user);
        return user.getRecentSearchList();
    }


    @Override
    public List<ProductDTO.SearchResponse> getEventsByCategory(String accessToken, String category, String sortType) {
        Long userId = jwtManager.validateJwt(accessToken);
        ProductCategory productCategory = changeToProductCategory(category); // 카테고리 타입 변환 (String -> Enum)
        LocalDate now = LocalDate.now();

        // 이벤트 조회 및 정렬
        List<Event> events = switch (sortType) {
            case "popular" -> eventRepository.findByCategoryPopular(productCategory, now).stream().collect(Collectors.toList());
            case "lowPrice" -> eventRepository.findByCategoryLowPrice(productCategory, now).stream().collect(Collectors.toList());
            case "highPrice" -> eventRepository.findByCategoryHighPrice(productCategory, now).stream().collect(Collectors.toList());
            case "new" -> eventRepository.findByCategoryNew(productCategory, now).stream().collect(Collectors.toList());
            default -> throw new IllegalArgumentException("Invalid sortType: " + sortType);
        };

        // DTO 변환
        return events.stream()
                .map(event -> {
                    Product product = event.getProduct();
                    int dDay = calculateDDay(now, event.getEventEnd());
                    boolean isBookmarked = heartRepository.findFirstByUserIdAndEvent(userId, event).isPresent(); // 북마크 상태 확인
                    return ProductConverter.toSearch(event, product, dDay, isBookmarked);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO.EventDetailsResponse getEventDetailsById(String accessToken, Long eventId) {
        // 이벤트 아이디로 이벤트 객체 가져옴
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.PRODUCT_ERROR_EVENT));

        // 유저 아이디와 이벤트 아이디로 투자 객체 가져옴
        Long userId = jwtManager.validateJwt(accessToken);
        Investment investment = investmentRepository.findFirstByUserIdAndEventId(userId, eventId)
                .orElse(null);

        Boolean isBookmarked = heartRepository.findFirstByUserIdAndEvent(userId, event).isPresent();


        // DTO로 변환
        return ProductConverter.toEventDetails(event, investment, isBookmarked);
    }

    // Enum에 category 없으면 오류 처리
    private ProductCategory changeToProductCategory(String category) {
        try {
            return ProductCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TempHandler(ErrorStatus.PRODUCT_ERROR_CATEGORY);
        }
    }

    // 디데이 계산 -> 컨버터로 옮기기
    private Integer calculateDDay(LocalDate now, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(now, endDate);
    }

    public void saveInvestment(Investment investment) {
        investmentRepository.save(investment);
    }
}
