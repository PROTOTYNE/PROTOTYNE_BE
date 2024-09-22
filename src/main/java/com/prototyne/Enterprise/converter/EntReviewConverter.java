package com.prototyne.Enterprise.converter;

import com.prototyne.Enterprise.web.dto.EntReviewDTO;
import com.prototyne.domain.Feedback;
import com.prototyne.domain.User;

import java.util.List;

public class EntReviewConverter {
    public static EntReviewDTO.ReviewResponse toReviewResponse(Feedback feedback, User user, List<String> imagefiles) {

        return EntReviewDTO.ReviewResponse.builder()
                .investmentId(feedback.getInvestment().getId())
                .penalty(feedback.getPenalty())
                .userId(user.getId())
                .answer1(feedback.getAnswer1())
                .answer2(feedback.getAnswer2())
                .answer3(feedback.getAnswer3())
                .answer4(feedback.getAnswer4())
                .answer5(feedback.getAnswer5())
                .answer6(feedback.getAnswer6())
                .imagefiles(imagefiles)
                .build();
    }

}
