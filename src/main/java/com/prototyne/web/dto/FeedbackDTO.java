package com.prototyne.web.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class FeedbackDTO {


    private Long id; //InvestmentId

    private Byte reYn;  // 재사용 여부

    private Boolean penalty;  // 패널티 여부

    private Integer answer1; //객관식 1번

    private Integer answer2; //객관식 2번

    private Integer answer3; //객관식 3번

    private Integer answer4; //객관식 4번

    private String answer5; //주관식 5번

    private Boolean answer6; //재사용 유무 Y/N

    //private List<FeedbackImageDTO> feedbackImages;  // 피드백에 첨부된 이미지 목록

}
