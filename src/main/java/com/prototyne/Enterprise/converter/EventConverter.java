package com.prototyne.Enterprise.converter;

import com.prototyne.Enterprise.web.dto.EventDTO;
import com.prototyne.domain.Event;
import com.prototyne.domain.Product;

import java.time.LocalDate;

public class EventConverter {
    // 체험 목록 조회 응답 형식으로
    public static EventDTO.EventResponse toEventResponse(Event event, Product product, LocalDate now) {
        // 1. 체험(이벤트) 단계와 그에 따른 날짜 계산
        EventDTO.StageAndDate stageAndDate = calculateStageAndDate(event, now);

        // 2. DTO 반환
        return EventDTO.EventResponse.builder()
                .eventId(event.getId())
                .thumbnailUrl(product.getThumbnailUrl())
                .productName(product.getName())
                .stageAndDate(stageAndDate)
                .createdDate(event.getCreatedAt().toLocalDate())
                .category(product.getCategory())
                .build();
    }

    // 현재 날짜에 따라 체험(이벤트) 단계와 날짜 반환
    public static EventDTO.StageAndDate calculateStageAndDate(Event event, LocalDate now) {
        Integer stage;
        LocalDate stageDate;

        if (now.isBefore(event.getEventStart())) {
            stage = 1; // 시작 전 -> 이벤트 시작일
            stageDate = event.getEventStart();
        } else if (now.isBefore(event.getReleaseStart())) {
            stage = 2; // 신청자 모집 기간 -> 이벤트 종료일
            stageDate = event.getEventEnd();
        } else if (now.isBefore(event.getFeedbackStart())) {
            stage = 3; // 당첨자 발표 기간 -> 발표 종료일
            stageDate = event.getReleaseEnd();
        } else if (now.isBefore(event.getEndDate())) {
            stage = 4; // 후기 작성 기간 -> 작성 종료일
            stageDate = event.getFeedbackEnd();// 작성 종료일
        } else {
            stage = 5; // 종료 -> 종료일
            stageDate = event.getEndDate();
        }

        return EventDTO.StageAndDate.builder()
                .stage(stage)
                .stageDate(stageDate)
                .build();
    }

    // 체험 엔티티 형식으로
    public static Event toEvent(Product product, EventDTO.createEventRequest request) {
        // 체험 생성
        Event newEvent = Event.builder()
                .product(product)
                .eventStart(request.getEventStart())
                .eventEnd(request.getEventEnd())
                .releaseStart(request.getReleaseStart())
                .releaseEnd(request.getReleaseEnd())
                .feedbackStart(request.getFeedbackStart())
                .feedbackEnd(request.getFeedbackEnd())
                .build();
        return newEvent;
    }
}
