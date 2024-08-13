package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.service.FeedbackService.FeedbackService;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.service.ReviewService.ReviewService;
import com.prototyne.web.dto.FeedbackDTO;
import com.prototyne.web.dto.FeedbackImageDTO;
import com.prototyne.web.dto.ReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtManager jwtManager;

    @Tag(name="${swagger.tag.product-etc}")
    @GetMapping("/{productId}")
    @Operation(summary="후기 질문 목록 조회 API", description="시제품의 후기 질문 목록 조회")
    public ApiResponse<ReviewDTO.ReviewResponseDTO> getReviewQuestions(@PathVariable Long productId){
        ReviewDTO.ReviewResponseDTO reviewQuestions = reviewService.getReviewById(productId);
        return ApiResponse.onSuccess(reviewQuestions);
    }

    @Tag(name="${swagger.tag.product-etc}")
    @PutMapping("/text/{investmentId}")
    @Operation(summary="후기 작성 텍스트 API", description="사용자가 체험했던 시제품에 대한 후기 작성",security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<FeedbackDTO> UpdateFeedbacks(HttpServletRequest token,  @PathVariable Long investmentId,
                                                    @RequestBody FeedbackDTO feedbackDTO){
        String aouthtoken = jwtManager.getToken(token);
        FeedbackDTO UpdateFeedbacks = feedbackService.UpdateFeedbacks(aouthtoken,investmentId, feedbackDTO);
        return ApiResponse.onSuccess(UpdateFeedbacks);
    }

    @Tag(name="${swagger.tag.product-etc}")
    @PostMapping(value="/image/{feedbackId}", consumes="multipart/form-data")
    @Operation(summary="후기 작성 이미지 API", description="사용자가 체험했던 시제품에 대한 후기 이미지 업로드")
    public ApiResponse<FeedbackImageDTO> CreateFeedbacksImage(HttpServletRequest token, @PathVariable Long feedbackId,
                                                              @RequestBody FeedbackImageDTO feedbackImageDTO) {
        String aouthtoken = jwtManager.getToken(token);
        FeedbackImageDTO CreateFeedbacksImage = feedbackService.CreateFeedbacksImage(aouthtoken, feedbackId, feedbackImageDTO);

        return ApiResponse.onSuccess(CreateFeedbacksImage);
    }


}
