package com.prototyne.Enterprise.web.dto;

import com.prototyne.domain.enums.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class ProductDTO {

    // 시제품 등록 요청 형식
    @Builder
    @Getter
    public static class CreateProductRequest {
        private ProductInfo productInfo;
        private Questions questions;        // 질문 목록
    }

    // 시제품 정보
    @Builder
    @Getter
    public static class ProductInfo {
        @NotBlank(message = "시제품 명이 공백입니다.")
        private final String productName;   // 시제품 명

        @NotBlank(message = "시제품 설명이 공백입니다.")
        private final String contents;      // 시제품 설명

        private final Integer reqTickets;   // 시제품 필요 티켓 수

        @NotBlank(message = "시제품 추가 안내사항이 공백입니다.")
        private String notes;               // 시제품 추가 안내사항
        private final ProductCategory category; // 시제품 카테고리
    }

    // 질문 목록
    @Builder
    @Getter
    public static class Questions {
        private final String question1;
        private final String question2;
        private final String question3;
        private final String question4;
        private final String question5;
    }

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
}
