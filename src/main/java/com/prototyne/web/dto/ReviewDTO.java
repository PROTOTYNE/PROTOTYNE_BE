package com.prototyne.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}
