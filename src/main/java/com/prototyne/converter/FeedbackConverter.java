package com.prototyne.converter;

import com.prototyne.domain.Feedback;
import com.prototyne.domain.Investment;
import com.prototyne.web.dto.FeedbackDTO;
import org.springframework.stereotype.Component;

@Component
public class FeedbackConverter {
    public static Feedback toFeedback(FeedbackDTO feedbackRequestDTO) {
        return Feedback.builder()
                .id(feedbackRequestDTO.getId())
                .answer1(feedbackRequestDTO.getAnswer1())
                .answer2(feedbackRequestDTO.getAnswer2())
                .answer3(feedbackRequestDTO.getAnswer3())
                .answer4(feedbackRequestDTO.getAnswer4())
                .answer5(feedbackRequestDTO.getAnswer5())
                .answer6(feedbackRequestDTO.getAnswer6())
                .build();

    }

    public static FeedbackDTO toFeedbackDto(Feedback feedback, Investment investment) {
        return FeedbackDTO.builder()
                .id(investment.getId()) //맞는지 확인 필요
                .answer1(feedback.getAnswer1())
                .answer2(feedback.getAnswer2())
                .answer3(feedback.getAnswer3())
                .answer4(feedback.getAnswer4())
                .answer5(feedback.getAnswer5())
                .answer6(feedback.getAnswer6())
                .build();
    }
}
