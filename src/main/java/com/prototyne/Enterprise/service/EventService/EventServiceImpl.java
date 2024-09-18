package com.prototyne.Enterprise.service.EventService;

import com.prototyne.Enterprise.converter.EventConverter;
import com.prototyne.Enterprise.web.dto.ProductDTO;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.domain.Event;
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
    public List<ProductDTO.EventResponse> getEvents(String accessToken) {
        Long enterpriseId = jwtManager.validateJwt(accessToken);
        LocalDate now = LocalDate.now(); // 현재 날짜

        // 1. 기업 ID로 모든 이벤트 조회
        List<Event> events = eventRepository.findByEnterpriseId(enterpriseId);

        /// 2. DTO 변환
        return events.stream()
                .map(event -> EventConverter.toEventResponse(event, event.getProduct(), now))
                .collect(Collectors.toList());
    }
}
