package com.prototyne.Enterprise.web.dto;

import com.prototyne.domain.FeedbackImage;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
public class EntReviewDTO {
    @Builder
    @Getter
    public static class ReviewResponse{

        private Long investmentId;

        private Long userId;

        private Boolean penalty;

        private Integer answer1;

        private Integer answer2;

        private Integer answer3;

        private Integer answer4;

        private String answer5;

        private Boolean answer6;

        private List<String> imagefiles;

    }


}
