package com.prototyne.Users.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.Users.service.FeedbackService.FeedbackService;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.Users.service.ReviewService.ReviewService;
import com.prototyne.Users.web.dto.FeedbackDTO;
import com.prototyne.Users.web.dto.FeedbackImageDTO;
import com.prototyne.Users.web.dto.ReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/review")
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
    public ApiResponse<FeedbackDTO> UpdateFeedbacks(HttpServletRequest token, @PathVariable Long investmentId,
                                                    @RequestBody FeedbackDTO feedbackDTO){
        String aouthtoken = jwtManager.getToken(token);
        FeedbackDTO UpdateFeedbacks = feedbackService.UpdateFeedbacks(aouthtoken,investmentId, feedbackDTO);
        return ApiResponse.onSuccess(UpdateFeedbacks);
    }

    @Tag(name="${swagger.tag.product-etc}")
    @PostMapping(value="/image/{investmentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary="후기 작성 이미지 API", description="사용자가 체험했던 시제품에 대한 후기 이미지 업로드",security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<FeedbackImageDTO> CreateFeedbacksImages(HttpServletRequest token, @PathVariable Long investmentId,
                                                              @RequestPart("imageFiles")
                                                              List<MultipartFile> images) {
        String aouthtoken = jwtManager.getToken(token);
        FeedbackImageDTO CreateFeedbacksImages = feedbackService.CreateFeedbacksImage(aouthtoken, investmentId,"feedback-images", images);

        return ApiResponse.onSuccess(CreateFeedbacksImages);
    }


}
