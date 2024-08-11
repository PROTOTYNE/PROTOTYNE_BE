package com.prototyne.web.dto;

import lombok.*;

public class ProductDTO {

    // 시제품 응답 형식
    @Getter
    @Builder
    public static class EventResponse{
        private Long id;                // 이벤트 시제품 아이디
        private String name;            // 시제품 이름
        private String thumbnailUrl;    // 시제품 썸네일
        private Integer investCount;    // 신청한 사람 수 -> 투자 테이블 수
        private Integer reqTickets;     // 시제품 필요 티켓 수
    }

    // 시제품 검색/카테고리 응답 형식
    @Getter
    @Builder
    public static class SearchResponse{
        private Long id;                // 이벤트 시제품 아이디 (프로덕트가 기준? 이벤트가 기준?)
        private String name;            // 시제품 이름
        private String thumbnailUrl;    // 시제품 썸네일
        private Integer dDay;           // 디데이(신청마감 기준, 프론트 처리?)
        private Integer reqTickets;     // 시제품 필요 티켓 수
    }

    // 개수?
    // public static class
}
