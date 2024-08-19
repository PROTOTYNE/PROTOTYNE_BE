package com.prototyne.service.ProductService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.converter.ProductConverter;
import com.prototyne.domain.Event;
import com.prototyne.domain.Investment;
import com.prototyne.domain.Product;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.ProductCategory;
import com.prototyne.repository.EventRepository;
import com.prototyne.repository.HeartRepository;
import com.prototyne.repository.InvestmentRepository;
import com.prototyne.repository.UserRepository;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.web.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final InvestmentRepository investmentRepository;
    private final HeartRepository heartRepository;
    private final JwtManager jwtManager;

    @Override
    public ProductDTO.HomeResponse getHomeById(String accessToken) {
        // 유저 아이디 객체 가져옴
        Long userId = jwtManager.validateJwt(accessToken);

        List<ProductDTO.EventResponse> poluarList =  getEventsByType(userId, "popular")
                .stream().limit(3).collect(Collectors.toList());

        List<ProductDTO.EventResponse> imminentList =  getEventsByType(userId, "imminent")
                .stream().limit(2).collect(Collectors.toList());

        List<ProductDTO.EventResponse> newList =  getEventsByType(userId, "new")
                .stream().limit(2).collect(Collectors.toList());
        return ProductConverter.toHome(poluarList, imminentList, newList);
    }

    @Override
    public List<ProductDTO.EventResponse> getEventsByType(Long userId, String type) {

        LocalDateTime now = LocalDateTime.now();
        List<Event> events = switch (type) {
            // 인기순
            case "popular" -> eventRepository.findAllActiveEventsByPopular(now);

            // 마감 임박순 (3일)
            case "imminent" -> eventRepository.findAllEventsByImminent(now).stream()
                    .filter(event -> event.getEventEnd().isBefore(now.plusDays(3)))
                    .collect(Collectors.toList());

            // 최신 등록순 (일주일)
            case "new" -> eventRepository.findAllEventsByNew(now).stream()
                    .filter(event -> event.getEventStart().isAfter(now.minusWeeks(1)))
                    .collect(Collectors.toList());

            // 그 외 예외처리
            default -> throw new TempHandler(ErrorStatus.PRODUCT_ERROR_TYPE);
        };

        // DTO 변환
        return events.stream()
                .map(event -> {
                    Product product = event.getProduct();
                    int investCount = event.getInvestmentList().size();
                    // 북마크 상태 확인
                    boolean isBookmarked = heartRepository.findByUserIdAndEvent(userId, event).isPresent();
                    return ProductConverter.toEvent(event, product, investCount, isBookmarked);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO.SearchResponse> getEventsBySearch(String accessToken, String name) {
        Long userId = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당하는 회원이 존재하지 않습니다."));

        addSearchTerm(user, name);


        // 신청 진행 중인 시제품 이벤트만 가져옴
        LocalDateTime now = LocalDateTime.now();
        List<Event> events = eventRepository.findAllByProductNameContaining(name).stream()
                .filter(event -> now.isAfter(event.getEventStart()) && now.isBefore(event.getEventEnd()))
                .collect(Collectors.toList());

        return events.stream()
                .map(event -> {
                    Product product = event.getProduct();
                    int dDay = calculateDDay(now, event.getEventEnd());
                    // 북마크 상태 확인
                    boolean isBookmarked = heartRepository.findByUserIdAndEvent(userId, event).isPresent();
                    return ProductConverter.toSearch(event, product, dDay, isBookmarked);
                })
                .collect(Collectors.toList());
    }

    private void addSearchTerm(User user, String searchTerm){
        List<String> recentSearchList = user.getRecentSearchList();
        recentSearchList.remove(searchTerm);
        recentSearchList.add(0, searchTerm);

        if(recentSearchList.size() > 10){
            recentSearchList.remove(recentSearchList.size());
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
    public List<String> deleteSearchHistory(String searchTerm, String accessToken){
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
    public List<ProductDTO.SearchResponse> getEventsByCategory(String accessToken, String category) {
        // 유저 아이디 객체 가져옴
        Long userId = jwtManager.validateJwt(accessToken);
        // 카테고리 타입 변환 (String -> Enum)
        ProductCategory productCategory = changeToProductCategory(category);
        // 신청 진행 중인 시제품 이벤트만 가져옴
        LocalDateTime now = LocalDateTime.now();
        List<Event> events = eventRepository.findByProductCategory(productCategory).stream()
                .filter(event -> now.isAfter(event.getEventStart()) && now.isBefore(event.getEventEnd()))
                .collect(Collectors.toList());

        // DTO 변환
        return events.stream()
                .map(event -> {
                    Product product = event.getProduct();
                    int dDay = calculateDDay(now, event.getEventEnd());
                    // 북마크 상태 확인
                    boolean isBookmarked = heartRepository.findByUserIdAndEvent(userId, event).isPresent();
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
        Investment investment = investmentRepository.findByUserIdAndEventId(userId, eventId)
                .orElse(null);

        Boolean isBookmarked = heartRepository.findByUserIdAndEvent(userId, event).isPresent();


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

    // 디데이 계산
    private Integer calculateDDay(LocalDateTime now, LocalDateTime endDate) {
        long daysBetween = java.time.Duration.between(now, endDate).toDays();
        return (int) daysBetween;
    }

    public void saveInvestment(Investment investment){
        investmentRepository.save(investment);
    }
}
