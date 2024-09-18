package com.prototyne.Enterprise.web.dto;

import com.prototyne.domain.enums.ProductCategory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class ProductDTO {

    // 시제품 목록 조회 응답 형식
    @Builder
    @Getter
    public static class ProductResponse {
        private final Long productId;         // 시제품 아이디
        private final String thumbnailUrl;    // 시제품 썸네일
        private final String productName;     // 시제품 명
        private final Integer reqTickets;     // 시제품 필요 티켓 수
        private final LocalDate createdDate;  // 시제품 등록일
        private final ProductCategory category;   // 시제품 카테고리
        private final Integer eventCount;     // 진행 중인 체험
//        private LocalDate date;       // 출시 예정일
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

    @Getter
    @Builder
    public static class StageAndDate {
        private final Integer stage;       // 이벤트 단계
        private final LocalDate stageDate;   // 단계에 따른 날짜
    }
}
