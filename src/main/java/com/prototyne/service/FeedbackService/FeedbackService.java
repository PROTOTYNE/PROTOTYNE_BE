package com.prototyne.service.FeedbackService;

import com.prototyne.web.dto.FeedbackDTO;
import com.prototyne.web.dto.FeedbackImageDTO;

public interface FeedbackService {
    FeedbackDTO UpdateFeedbacks(Long investmentId, FeedbackDTO feedbackDTO, String accessToken);

    //FeedbackImageDTO CreateFeedbacksImage(Long investmentId, FeedbackImageDTO feedbackImageDTO, String accessToken);
}
