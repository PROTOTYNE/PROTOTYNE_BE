package com.prototyne.service.ReviewService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.Product;
import com.prototyne.repository.ReviewRepository;
import com.prototyne.web.dto.ReviewDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.prototyne.converter.ReviewConverter.toReview;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    @Override
    public ReviewDTO.ReviewResponseDTO getReviewById(Long productId) {
        Product product = reviewRepository.findById(productId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.PRODUCT_ERROR_ID));
        return toReview(product);
    }

}
