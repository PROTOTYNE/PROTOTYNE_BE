package com.prototyne.Enterprise.service.ReviewService;

import com.prototyne.Enterprise.web.dto.EntReviewDTO;

import java.util.List;

public interface EntReviewService {
    List<EntReviewDTO.ReviewResponse> getAllReviews(String token, Long eventId);

}
