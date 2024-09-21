package com.prototyne.Enterprise.service.EventService;

import com.prototyne.Enterprise.converter.EventConverter;
import com.prototyne.Enterprise.web.dto.EventDTO;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.Event;
import com.prototyne.domain.Product;
import com.prototyne.repository.EventRepository;
import com.prototyne.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service("enterpriseEventServiceImpl")
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final JwtManager jwtManager;
    private final ProductRepository productRepository;
    private final EventRepository eventRepository;

    // 체험 목록 조회
    @Override
    public List<EventDTO.EventResponse> getEvents(String accessToken) {
        Long enterpriseId = jwtManager.validateJwt(accessToken);
        LocalDate now = LocalDate.now(); // 현재 날짜

        // 1. 기업 ID로 모든 이벤트 조회
        List<Event> events = eventRepository.findByEnterpriseId(enterpriseId);

        /// 2. DTO 변환
        return events.stream()
                .map(event -> EventConverter.toEventResponse(event, event.getProduct(), now))
                .collect(Collectors.toList());
    }

    // 체험 등록
    @Override
    public Long createEvent(String accessToken, Long productId, EventDTO.EventDate request) {
        Long enterpriseId = jwtManager.validateJwt(accessToken);

        // 시제품 객체 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.PRODUCT_ERROR_ID));

        // 해당 시제품을 기업이 가졌는지 확인
        if (!product.getEnterprise().getId().equals(enterpriseId))
            throw new TempHandler(ErrorStatus.ENTERPRISE_ERROR_PRODUCT);

        // 체험 저장 및 id 반환
        Event newEvent = EventConverter.toEvent(product, request);
        Event event = eventRepository.save(newEvent);

        return event.getId();
    }

    // 체험 상세 조회
    @Override
    public EventDTO.EventInfo getEventInfo(String accessToken, Long eventId) {
        Long enterpriseId = jwtManager.validateJwt(accessToken);

        // 체험 객체 조회
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.EVENT_ERROR_ID));

        // 이벤트로부터 제품 객체 가져오기
        Product product = event.getProduct();

        // 해당 시제품을 기업이 가졌는지 확인
        if (!product.getEnterprise().getId().equals(enterpriseId))
            throw new TempHandler(ErrorStatus.ENTERPRISE_ERROR_PRODUCT);

        return EventConverter.toEventInfo(product, event);
    }
}
