package com.prototyne.converter;

import com.prototyne.domain.Feedback;
import com.prototyne.domain.FeedbackImage;
import com.prototyne.web.dto.FeedbackImageDTO;
import org.springframework.stereotype.Component;

@Component
public class FeedbackImageConverter {
    public static FeedbackImageDTO toFeedbackImageDTO(FeedbackImage feedbackImage)
    {
        return FeedbackImageDTO.builder()
                .id(feedbackImage.getId())
                .imageUrl(feedbackImage.getImageUrl())
                .feedbackId(feedbackImage.getFeedback().getId())
                .build();
    }

    public static FeedbackImage toFeedbackImage(FeedbackImageDTO feedbackImageDTO, Feedback feedback)
    {
        return FeedbackImage.builder()
                .imageUrl(feedbackImageDTO.getImageUrl())
                .feedback(feedback)
                .build();
    }
}
