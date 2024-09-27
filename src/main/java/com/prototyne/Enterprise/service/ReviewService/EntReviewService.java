package com.prototyne.Enterprise.service.ReviewService;

import com.prototyne.Enterprise.web.dto.EntAllReviewResponseDTO;
import com.prototyne.Enterprise.web.dto.EntReviewDTO;
import com.prototyne.Users.web.dto.ReviewDTO;

import java.util.List;

public interface EntReviewService {
    EntAllReviewResponseDTO.ReviewResponse getAllReviews(String token, Long eventId);
    EntReviewDTO.ReviewResponse getReviewByUserId(String token, Long eventId, Long userId);
    EntReviewDTO.ReviewResponse updatePenaltyByUserId(String token, Long eventId, Long userId);
    ReviewDTO.ReviewResponseDTO updateReviewQuestion(String token, Long productId, ReviewDTO.ReviewResponseDTO request);

}
