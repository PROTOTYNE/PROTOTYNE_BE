package com.prototyne.Enterprise.service.EventService;

import com.prototyne.Enterprise.web.dto.EventDTO;

import java.util.List;

public interface EventService {
    // 기업 id에 따른 체험 목록 조회 (토큰 인증 필수)
    List<EventDTO.EventResponse> getEvents(String accessToken);

    // 기업 id가 가진 시제품에 체험 등록 (토큰 인증 필수) - id 반환
    Long createEvent(String accessToken, Long productId, EventDTO.EventDate request);

    // 체험 정보 상세 조회
    EventDTO.EventInfo getEventInfo(String accessToken, Long eventId);

    // 체험 삭제
    void deleteEvent(String accessToken, Long eventId);

    // 체험 현황 조회
    EventDTO.EventProgress getEventProgress(String accessToken, Long eventId);
}
