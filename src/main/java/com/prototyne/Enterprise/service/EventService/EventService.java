package com.prototyne.Enterprise.service.EventService;

import com.prototyne.Enterprise.web.dto.EventDTO;

import java.util.List;

public interface EventService {
    // 기업 id에 따른 체험 목록 조회 (토큰 인증 필수)
    List<EventDTO.EventResponse> getEvents(String accessToken);

    // 기업 id가 가진 시제품에 체험 등록 (토큰 인증 필수) - id 반환
    Long createEvent(String accessToken, Long productId, EventDTO.createEventRequest request);

}
