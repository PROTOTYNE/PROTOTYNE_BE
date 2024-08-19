package com.prototyne.service.ReviewService;

import com.prototyne.web.dto.ReviewDTO;

public interface ReviewService {
    ReviewDTO.ReviewResponseDTO getReviewById(Long productId);
}
