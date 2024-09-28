package com.prototyne.Enterprise.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class EntAllReviewResponseDTO {

    @Builder
    @Getter
    public static class ObjectDTO{

        private String question;

        private List<Integer> answers;
    }

    @Builder
    @Getter
    public  static class SubjectiveAnswerDTO{

        private Long userId;

        private String answer;
    }

    @Builder
    @Getter
    public static class SubjectiveDTO{

        private String question;

        private EntAllReviewResponseDTO.SubjectiveAnswerDTO answers;
    }

    @Builder
    @Getter
    public static class ImageDTO{

        private Long userId;

        private List<String> imageFiles;

    }

    @Builder
    @Getter
    public static class ReviewResponse{

        private List<EntAllReviewResponseDTO.ObjectDTO> objectives;

        //private SubjectiveDTO subjective;

        private List<EntAllReviewResponseDTO.SubjectiveDTO> subjectiveList;

        private List<Integer> repurchase;

        private List<ImageDTO> images;
    }

}
