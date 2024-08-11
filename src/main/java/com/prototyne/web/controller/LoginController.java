package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.service.LoginService.KakaoServiceImpl;
import com.prototyne.web.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
@Tag(name = "${swagger.tag.auth}")
public class LoginController {

    private final KakaoServiceImpl kakaoService;

    @GetMapping("/login")
    @Operation(summary = "로그인 API",
            description = "Access Token 응답")
    public ApiResponse<UserDto.KakaoTokenResponse> callback(@RequestParam("code") String code) {
        UserDto.KakaoTokenResponse accessToken = kakaoService.getAccessToken(code);
        return ApiResponse.onSuccess(accessToken);
    }

//
//    @GetMapping("/user")
//    @Operation(summary = "사용자 정보 조회 API - 인증 필요",
//            description = "사용자 정보 조회 API - 인증 필요",
//            security = {@SecurityRequirement(name = "session-token")})
//    public ApiResponse<UserDto.UserRequest> userInfo(HttpServletRequest token) {
//        String aouthtoken = token.getHeader("Authorization").replace("Bearer ", "");
//        return ApiResponse.onSuccess(kakaoService.getUserInfo(aouthtoken));
//    }

}