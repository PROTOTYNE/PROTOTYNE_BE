package com.prototyne.Enterprise.converter;

import com.prototyne.Enterprise.web.dto.ProductDTO;
import com.prototyne.domain.Event;
import com.prototyne.domain.Product;

import java.time.LocalDate;

public class EventConverter {
    // 체험 목록 조회 응답 형식으로
    public static ProductDTO.EventResponse toEventResponse(Event event, Product product, LocalDate now) {
        // 1. 체험(이벤트) 단계와 그에 따른 날짜 계산
        ProductDTO.StageAndDate stageAndDate = calculateStageAndDate(event, now);

        // 2. DTO 반환
        return ProductDTO.EventResponse.builder()
                .eventId(event.getId())
                .thumbnailUrl(product.getThumbnailUrl())
                .productName(product.getName())
                .stageAndDate(stageAndDate)
                .createdDate(event.getCreatedAt().toLocalDate())
                .category(product.getCategory())
                .build();
    }

    // 현재 날짜에 따라 체험(이벤트) 단계와 날짜 반환
    public static ProductDTO.StageAndDate calculateStageAndDate(Event event, LocalDate now) {
        Integer stage;
        LocalDate stageDate;

        if (now.isBefore(event.getEventStart().toLocalDate())) {
            stage = 1; // 시작 전 -> 이벤트 시작일
            stageDate = event.getEventStart().toLocalDate();
        } else if (now.isBefore(event.getEventEnd().toLocalDate())) {
            stage = 2; // 신청자 모집 기간 -> 이벤트 종료일
            stageDate = event.getEventEnd().toLocalDate();
        } else if (now.isBefore(event.getReleaseEnd().toLocalDate())) {
            stage = 3; // 당첨자 발표 기간 -> 발표 종료일
            stageDate = event.getReleaseEnd().toLocalDate();
        } else if (now.isBefore(event.getFeedbackEnd().toLocalDate())) {
            stage = 4; // 후기 작성 기간 -> 작성 종료일
            stageDate = event.getFeedbackEnd().toLocalDate();// 작성 종료일
        } else {
            stage = 5; // 종료 -> 종료일
            stageDate = event.getEndDate().toLocalDate();
        }

        return ProductDTO.StageAndDate.builder()
                .stage(stage)
                .stageDate(stageDate)
                .build();
    }
}
