package com.prototyne.Users.web.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class ReviewDTO {

    @Getter
    @Builder
    public static class ReviewResponseDTO {
        private Long id;  //시제품 아이디 (API 명세서에 투자 ID로 작성되어 있음)

        private String question1; // 객관식 1번 질문

        private String question2; // 객관식 2번 질문

        private String question3; // 객관식 3번 질문

        private String question4; // 객관식 4번 질문

        private String question5; // 주관식 질문

    }

    @Getter
    @Builder
    public static class ReviewQuestionDTO {
        private Long productId;

        private String name;

        private String thumbnailUrl;

        private LocalDate feedbackStart;

        private LocalDate feedbackEnd;

        private String question1; // 객관식 1번 질문

        private String question2; // 객관식 2번 질문

        private String question3; // 객관식 3번 질문

        private String question4; // 객관식 4번 질문

        private String question5; // 주관식 질문
    }
}
