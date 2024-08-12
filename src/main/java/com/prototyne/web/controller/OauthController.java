package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.service.LoginService.KakaoServiceImpl;
import com.prototyne.service.SignupService.UserSignupServiceImpl;
import com.prototyne.web.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
@Tag(name = "${swagger.tag.auth}")
public class OauthController {

    private final KakaoServiceImpl kakaoService;
    private final UserSignupServiceImpl signupService;
    private final JwtManager jwtManager;

    @GetMapping("/login")
    @Operation(summary = "로그인 API",
            description = "Access Token 응답")
    public ApiResponse<UserDto.KakaoTokenResponse> callback(@RequestParam("code") String code) {
        UserDto.KakaoTokenResponse accessToken = kakaoService.getAccessToken(code);
        return ApiResponse.onSuccess(accessToken);
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입 API - 인증 필요",
            description = "회원가입 API - 인증 필요",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<UserDto.UserSignUpResponse> signup(HttpServletRequest token, @RequestBody @Valid UserDto.UserSignUpRequest signUpRequest) {
        String aouthtoken = jwtManager.getToken(token);
        return ApiResponse.onSuccess(signupService.signup(aouthtoken, signUpRequest.getDetailRequest(), signUpRequest.getAddInfoRequest()));
    }

}