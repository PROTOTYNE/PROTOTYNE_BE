package com.prototyne.service.ProductService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.converter.ProductConverter;
import com.prototyne.domain.Event;
import com.prototyne.domain.Investment;
import com.prototyne.domain.Product;
import com.prototyne.domain.enums.ProductCategory;
import com.prototyne.repository.EventRepository;
import com.prototyne.repository.InvestmentRepository;
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
    private final InvestmentRepository investmentRepository;
    private final JwtManager jwtManager;

    public List<ProductDTO.EventResponse> getEventsByType(String type) {
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
                    return ProductConverter.toEvent(event, product, investCount);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO.SearchResponse> getEventsBySearch(String name) {
        // 신청 진행 중인 시제품 이벤트만 가져옴
        LocalDateTime now = LocalDateTime.now();
        List<Event> events = eventRepository.findAllByProductNameContaining(name).stream()
                .filter(event -> now.isAfter(event.getEventStart()) && now.isBefore(event.getEventEnd()))
                .collect(Collectors.toList());

        return events.stream()
                .map(event -> {
                    Product product = event.getProduct();
                    int dDay = calculateDDay(now, event.getEventEnd());
                    return ProductConverter.toSearch(event, product, dDay);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO.SearchResponse> getEventsByCategory(String category) {
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
                    return ProductConverter.toSearch(event, product, dDay);
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


        // DTO로 변환
        return ProductConverter.toEventDetails(event, investment);
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
}
