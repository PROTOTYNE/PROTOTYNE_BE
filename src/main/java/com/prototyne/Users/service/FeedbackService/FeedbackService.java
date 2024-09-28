package com.prototyne.Users.service.FeedbackService;

import com.prototyne.Users.web.dto.FeedbackDTO;
import com.prototyne.Users.web.dto.FeedbackImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedbackService {
    FeedbackDTO UpdateFeedbacks(String accessToken, Long eventId, FeedbackDTO feedbackDTO);

    FeedbackImageDTO CreateFeedbacksImage(String accessToken, Long eventId, String directory, List<MultipartFile> feedbackImages);
}
