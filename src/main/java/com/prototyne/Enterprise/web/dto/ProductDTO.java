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
        private Long productId;         // 시제품 아이디
        private String thumbnailUrl;    // 시제품 썸네일
        private String productName;     // 시제품 명
        private Integer reqTickets;     // 시제품 필요 티켓 수
        private LocalDate createdDate;  // 시제품 등록일
        private ProductCategory category;   // 시제품 카테고리
        private Integer eventCount;     // 진행 중인 체험
//        private LocalDate date;       // 출시 예정일
    }

}
