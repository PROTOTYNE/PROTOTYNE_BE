package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.service.FeedbackService.FeedbackService;
import com.prototyne.service.ReviewService.ReviewService;
import com.prototyne.web.dto.FeedbackDTO;
import com.prototyne.web.dto.FeedbackImageDTO;
import com.prototyne.web.dto.ReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final FeedbackService feedbackService;

    @Tag(name="${swagger.tag.product-etc}")
    @GetMapping("/{productId}")
    @Operation(summary="후기 질문 목록 조회 API", description="시제품의 후기 질문 목록 조회")
    public ApiResponse<ReviewDTO.ReviewResponseDTO> getReviewQuestions(@PathVariable Long productId){
        ReviewDTO.ReviewResponseDTO reviewQuestions = reviewService.getReviewById(productId);
        return ApiResponse.onSuccess(reviewQuestions);
    }

    @Tag(name="${swagger.tag.product-etc}")
    @PutMapping("/text/{investmentId}")
    @Operation(summary="후기 작성 텍스트 API", description="사용자가 체험했던 시제품에 대한 후기 작성")
    public ApiResponse<FeedbackDTO> UpdateFeedbacks(@PathVariable Long investmentId,
                                                    @RequestBody FeedbackDTO feedbackDTO,
                                                    @RequestHeader("Authorization") String accessToken){
        FeedbackDTO UpdateFeedbacks = feedbackService.UpdateFeedbacks(investmentId, feedbackDTO, accessToken);
        return ApiResponse.onSuccess(UpdateFeedbacks);
    }
    @Tag(name="${swagger.tag.product-etc}")
    @PostMapping("/image/{investmentId}")
    @Operation(summary="후기 작성 이미지 API", description="사용자가 체험했던 시제품에 대한 후기 이미지 업로드")
    public ApiResponse<FeedbackImageDTO> CreateFeedbacksImage(@PathVariable Long investmentId,
                                                              @RequestBody FeedbackImageDTO feedbackImageDTO,
                                                              @RequestHeader("Authorization") String accessToken) {
        FeedbackImageDTO CreateFeedbacksImage = feedbackService.CreateFeedbacksImage(investmentId, feedbackImageDTO, accessToken);

        return ApiResponse.onSuccess(CreateFeedbacksImage);
    }


}
