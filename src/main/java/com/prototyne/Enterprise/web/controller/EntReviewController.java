package com.prototyne.Enterprise.web.controller;

import com.prototyne.Enterprise.service.ReviewService.EntReviewService;
import com.prototyne.Enterprise.web.dto.EntAllReviewResponseDTO;
import com.prototyne.Enterprise.web.dto.EntReviewDTO;
import com.prototyne.Users.web.dto.ReviewDTO;
import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.apiPayload.config.JwtManager;
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
@RequestMapping("/enterprise")
@Tag(name="${swagger.tag.enterprise-review}")
public class EntReviewController {

    private final EntReviewService entReviewService;
    private final JwtManager jwtManager;

    @GetMapping("/review/{eventId}")
    @Operation(summary="전체 설문조사 조회 API - 인증 필수")
    @SecurityRequirement(name = "session-token")
    public ApiResponse<EntAllReviewResponseDTO.ReviewResponse> getAllReviews(HttpServletRequest token, @PathVariable Long eventId){
        String accessToken = jwtManager.getToken(token);
        EntAllReviewResponseDTO.ReviewResponse reviewList= entReviewService.getAllReviews(accessToken,eventId);
        return ApiResponse.onSuccess(reviewList);

    }

    @GetMapping("/review/{eventId}/{userId}")
    @Operation(summary="개별 설문조사 조회 API - 인증 필수")
    @SecurityRequirement(name = "session-token")
    public ApiResponse<EntReviewDTO.ReviewResponse> getReviewByUserId(HttpServletRequest token, @PathVariable Long eventId, @PathVariable Long userId){
        String accessToken = jwtManager.getToken(token);
        EntReviewDTO.ReviewResponse review= entReviewService.getReviewByUserId(accessToken,eventId,userId);
        return ApiResponse.onSuccess(review);
    }

    @PatchMapping("/review/{eventId}/{userId}")
    @Operation(summary="패널티 부여 API - 인증 필수")
    @SecurityRequirement(name = "session-token")
    public ApiResponse<EntReviewDTO.ReviewResponse> updatePenaltyByUserId(HttpServletRequest token, @PathVariable Long eventId, @PathVariable Long userId){
        String accessToken = jwtManager.getToken(token);
        EntReviewDTO.ReviewResponse review= entReviewService.updatePenaltyByUserId(accessToken, eventId, userId);

        return ApiResponse.onSuccess(review);
    }

    @PutMapping("/review/{productId}")
    @Operation(summary="설문조사 질문 목록 생성 API - 인증 필수")
    @SecurityRequirement(name="session-token")
    public ApiResponse<ReviewDTO.ReviewResponseDTO> updateReviewQuestion(HttpServletRequest token, @PathVariable Long productId,@RequestBody ReviewDTO.ReviewResponseDTO request){
        String accessToken=jwtManager.getToken(token);
        ReviewDTO.ReviewResponseDTO reviewQuestion=entReviewService.updateReviewQuestion(accessToken,productId, request);

        return ApiResponse.onSuccess(reviewQuestion);
    }
}