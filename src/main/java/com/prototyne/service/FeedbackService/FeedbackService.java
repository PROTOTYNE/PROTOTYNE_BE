package com.prototyne.service.FeedbackService;

import com.prototyne.web.dto.FeedbackDTO;
import com.prototyne.web.dto.FeedbackImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedbackService {
    FeedbackDTO UpdateFeedbacks(String accessToken, Long investmentId, FeedbackDTO feedbackDTO);

    FeedbackImageDTO CreateFeedbacksImage(String accessToken, Long feedbackId, String directory, List<MultipartFile> feedbackImages);
}
