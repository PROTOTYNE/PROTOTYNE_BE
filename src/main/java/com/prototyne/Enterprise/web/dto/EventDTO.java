package com.prototyne.Enterprise.web.dto;

import com.prototyne.domain.enums.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class EventDTO {

    // 체험 등록 요청 형식
    @Builder
    @Getter
    public static class createEventRequest {
        @NotBlank(message = "eventStart가 공백입니다.")
        private final LocalDate eventStart;     // 체험 시작
        @NotBlank(message = "eventEnd가 공백입니다.")
        private final LocalDate eventEnd;       // 체험 마감
        @NotBlank(message = "releaseStart가 공백입니다.")
        private final LocalDate releaseStart;   // 당첨자 시작
        @NotBlank(message = "releaseEnd가 공백입니다.")
        private final LocalDate releaseEnd;     // 당첨자 종료
        @NotBlank(message = "feedbackStart가 공백입니다.")
        private final LocalDate feedbackStart;  // 피드백 시작
        @NotBlank(message = "feedbackEnd가 공백입니다.")
        private final LocalDate feedbackEnd;    // 피드백 종료
    }

    // 체험 목록 조회 응답 형식
    @Builder
    @Getter
    public static class EventResponse {
        private final Long eventId;           // 체험(이벤트) 아이디
        private final String thumbnailUrl;    // 시제품 썸네일
        private final String productName;     // 시제품 명
        private final StageAndDate stageAndDate;  // 체험(이벤트) 단계와 그에 따른 날짜
        private final LocalDate createdDate;  // 체험(이벤트) 등록일
        private final ProductCategory category;   // 시제품 카테고리
    }

    // 체험(이벤트) 단계 및 날짜
    @Getter
    @Builder
    public static class StageAndDate {
        private final Integer stage;       // 이벤트 단계
        private final LocalDate stageDate;   // 단계에 따른 날짜
    }
}
