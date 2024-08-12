package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.service.ReviewService.ReviewService;
import com.prototyne.web.dto.ReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @Tag(name="${swagger.tag.product-etc}")
    @GetMapping("/{productId}")
    @Operation(summary="후기 질문 목록 조회 API", description="시제품의 후기 질문 목록 조회")
    public ApiResponse<ReviewDTO.ReviewResponseDTO> getReviewQuestions(@PathVariable Long productId){
        ReviewDTO.ReviewResponseDTO reviewQuestions = reviewService.getReviewById(productId);
        return ApiResponse.onSuccess(reviewQuestions);
    }

}
