package com.prototyne.Enterprise.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntReviewDTO {
    @Builder
    @Getter
    public static class ReviewResponse{

        private Long investmentId;

        private Long userId;

        private Integer answer1;

        private Integer answer2;

        private Integer answer3;

        private Integer answer4;

        private String answer5;

        private Boolean answer6;

    }

}
