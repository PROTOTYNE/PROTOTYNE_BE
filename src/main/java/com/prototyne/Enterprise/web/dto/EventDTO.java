package com.prototyne.Enterprise.web.dto;

import com.prototyne.domain.enums.ProductCategory;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class EventDTO {

    // 체험 등록 요청 형식
    @Builder
    @Getter
    public static class EventDate {
        @NotNull(message = "eventStart가 공백입니다.")
        private final LocalDate eventStart;     // 체험 시작
        @NotNull(message = "eventEnd가 공백입니다.")
        private final LocalDate eventEnd;       // 체험 마감
        @NotNull(message = "releaseDate가 공백입니다.")
        private final LocalDate releaseDate;    // 당첨자 발표
        @NotNull(message = "feedbackStart가 공백입니다.")
        private final LocalDate feedbackStart;  // 피드백 시작
        @NotNull(message = "feedbackEnd가 공백입니다.")
        private final LocalDate feedbackEnd;    // 피드백 종료
    }

    // 체험 목록 조회 응답 형식
    @Builder
    @Getter
    public static class EventResponse {
        private final Long eventId;           // 체험(이벤트) 아이디
        private final String thumbnailUrl;    // 시제품 썸네일
        private final String productName;     // 시제품 명
        private final Integer stage;       // 이벤트 단계
        private final LocalDate stageDate;   // 단계에 따른 날짜
        private final LocalDate createdDate;  // 체험(이벤트) 등록일
        private final ProductCategory category;   // 시제품 카테고리
    }

    // 체험 정보 조회
    @Builder
    @Getter
    public static class EventInfo {
        private final Long productId;
        private final Long eventId;
        private final List<String> productImages;
        private final ProductDTO.ProductInfo productInfo;
        private final EventDate dates;
    }

    // 체험 현황
    @Builder
    @Getter
    public static class EventProgress {
        private final Integer stage;         // 이벤트 단계
        private final Integer investCount;   // 당첨자 발표 수
        private final Integer feedbackCount; // 후기 작성 수
    }
}
