package com.prototyne.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class HeartDto {
    @Builder
    @Getter
    public static class HeartRequestDTO {
        private Long eventId;   // 이벤트 id
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HeartResponseDTO {
        private Long userId;
        private List<ProductInfo> products;

        @Builder
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ProductInfo{
            private Long productId;
            private Long eventId;
            private String name;
            private Integer reqTickets;
            private String thumbnailUrl;
            private Long count;
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HeartActionResponseDTO {
        private Long eventId;   // 등록 or 해제한 이벤트 id
        private String message;
    }
}
