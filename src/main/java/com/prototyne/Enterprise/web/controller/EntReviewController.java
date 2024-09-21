package com.prototyne.Enterprise.web.controller;

import com.prototyne.Enterprise.service.ReviewService.EntReviewService;
import com.prototyne.Enterprise.web.dto.EntReviewDTO;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ApiResponse<List<EntReviewDTO.ReviewResponse>> getAllReviews(HttpServletRequest token, @PathVariable Long eventId){
        String accessToken = jwtManager.getToken(token);
        List<EntReviewDTO.ReviewResponse> reviewList= entReviewService.getAllReviews(accessToken,eventId);
        return ApiResponse.onSuccess(reviewList);

    }

    @GetMapping("/review/{investmentId}/{userId}")
    @Operation(summary="개별 설문조사 조회 API - 인증 필수")
    @SecurityRequirement(name = "session-token")
    public ApiResponse<EntReviewDTO.ReviewResponse> getReviewByUserId(HttpServletRequest token, @PathVariable Long investmentId, @PathVariable Long userId){
        String accessToken = jwtManager.getToken(token);
        EntReviewDTO.ReviewResponse review= entReviewService.getReviewByUserId(accessToken,investmentId,userId);
        return ApiResponse.onSuccess(review);
    }

}
