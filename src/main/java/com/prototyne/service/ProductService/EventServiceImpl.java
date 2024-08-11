package com.prototyne.service.ProductService;

import com.prototyne.converter.ProductConverter;
import com.prototyne.domain.Event;
import com.prototyne.domain.Product;
import com.prototyne.repository.EventRepository;
import com.prototyne.web.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public List<ProductDTO.EventResponse> getEventsByType(String type) {
        LocalDateTime now = LocalDateTime.now();
        List<Event> events;

        switch (type) {
            // 인기순
            case "popular":
                events = eventRepository.findAllActiveEventsByPopular(now);
                break;

            // 마감 입박순 (3일 기준)
            case "imminent":
                events = eventRepository.findAllEventsByImminent(now);
                events.stream()
                        .filter(event ->
                                event.getEventEnd().isAfter(now) && event.getEventEnd().isBefore(now.plusDays(3)))
                        .collect(Collectors.toList());
                break;

            // 최신 등록순 (일주징 기준)
            case "new":
                events = eventRepository.findAllEventsByNew(now);
                events.stream()
                        .filter(event ->
                                event.getEventStart().isAfter(now.minusWeeks(1)) && event.getEventStart().isBefore(now))
                        .collect(Collectors.toList());
                break;

            default:
                return List.of(); // 빈 리스트를 반환 -> 에러?
        }

        return events.stream()
                .map(event -> {
                    Product product = event.getProduct();
                    int investCount = event.getInvestmentList().size();
                    return ProductConverter.toProduct(event, product, investCount);
                })
                .collect(Collectors.toList());
    }
}
