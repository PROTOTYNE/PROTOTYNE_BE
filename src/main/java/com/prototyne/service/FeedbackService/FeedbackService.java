package com.prototyne.service.FeedbackService;

import com.prototyne.web.dto.FeedbackDTO;
import com.prototyne.web.dto.FeedbackImageDTO;

public interface FeedbackService {
    FeedbackDTO UpdateFeedbacks(String accessToken, Long investmentId, FeedbackDTO feedbackDTO);

    FeedbackImageDTO CreateFeedbacksImage(String accessToken, Long feedbackId, FeedbackImageDTO feedbackImageDTO);
}
