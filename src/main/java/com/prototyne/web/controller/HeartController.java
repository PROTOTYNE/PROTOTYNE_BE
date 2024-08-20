package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.service.HeartService.HeartServiceImpl;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.web.dto.HeartDto;
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
public class HeartController {
    private final HeartServiceImpl heartService;
    private final JwtManager jwtManager;

    @Tag(name = "${swagger.tag.like}")
    @GetMapping("/like/list")
    @Operation(summary = "유저 북마크 목록 조회 API - 인증 필요",
        description = "유저 북마크 목록 조회 API - 인증 필요",
        security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<HeartDto.HeartResponseDTO> likeList(HttpServletRequest token) throws Exception {
        String accessToken = jwtManager.getToken(token);

        HeartDto.HeartResponseDTO heartResponse = heartService.getLikeList(accessToken);
        return ApiResponse.onSuccess(heartResponse);
    }

    @Tag(name = "${swagger.tag.like}")
    @PostMapping("/like/{eventId}")
    @Operation(summary = "유저 북마크 등록 API - 인증 필요",
            description = "유저 북마크 등록 API - 인증 필요",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<HeartDto.HeartActionResponseDTO> likeEvent(@PathVariable Long eventId, HttpServletRequest token){
        String accessToken = jwtManager.getToken(token);

        HeartDto.HeartActionResponseDTO response = heartService.likeEvent(eventId, accessToken);
        return ApiResponse.onSuccess(response);
    }

    @Tag(name = "${swagger.tag.like}")
    @DeleteMapping("/unlike/{eventId}")
    @Operation(summary = "유저 북마크 해제 API - 인증 필요",
            description = "유저 북마크 해제 API - 인증 필요",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<HeartDto.HeartActionResponseDTO> unlikeEvent(@PathVariable Long eventId, HttpServletRequest token){
        String accessToken = jwtManager.getToken(token);

        HeartDto.HeartActionResponseDTO response = heartService.unlikeEvent(eventId, accessToken);
        return ApiResponse.onSuccess(response);
    }

}
