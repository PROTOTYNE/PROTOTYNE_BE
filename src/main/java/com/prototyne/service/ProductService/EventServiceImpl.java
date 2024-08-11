package com.prototyne.service.ProductService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.converter.ProductConverter;
import com.prototyne.domain.Event;
import com.prototyne.domain.Product;
import com.prototyne.repository.EventRepository;
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
}
