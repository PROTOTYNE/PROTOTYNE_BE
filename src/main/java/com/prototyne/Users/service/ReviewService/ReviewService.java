package com.prototyne.Users.service.ReviewService;

import com.prototyne.Users.web.dto.ReviewDTO;

public interface ReviewService {
    ReviewDTO.ReviewQuestionDTO getReviewById(Long eventId);
}
