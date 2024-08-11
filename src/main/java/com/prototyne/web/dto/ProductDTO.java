package com.prototyne.web.dto;

import lombok.*;

import java.time.LocalDateTime;

public class ProductDTO {
    @Getter
    @Builder
    // 시제품 응답 형식
    public static class EventResponse{
        private Long id;                // 이벤트 시제품 아이디
        private String name;            // 시제품 이름
        private String thumbnailUrl;    // 시제품 썸네일
        private Integer investCount;    // 신청한 사람 수 -> 투자 테이블 수
        private Integer reqTickets;     // 시제품 필요 티켓 수
    }
}
