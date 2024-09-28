package com.prototyne.Users.web.controller;

import com.prototyne.Users.service.LoginService.KakaoServiceImpl;
import com.prototyne.Users.service.SignupService.UserSignupServiceImpl;
import com.prototyne.Users.service.TokenService.TokenService;
import com.prototyne.Users.web.dto.UserDto;
import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.apiPayload.config.JwtManager;
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
@RequestMapping("/users/oauth2")
@Tag(name = "${swagger.tag.auth}")
public class OauthController {

    private final KakaoServiceImpl kakaoService;
    private final UserSignupServiceImpl signupService;
    private final TokenService tokenService;
    private final JwtManager jwtManager;

    @GetMapping("/login")
    @Operation(summary = "로그인 API",
            description = "Access Token, Refresh Token 응답")
    public ApiResponse<UserDto.KakaoTokenResponse> callback(@RequestParam("code") String code) {
        UserDto.KakaoTokenResponse accessToken = kakaoService.getAccessToken(code);
        return ApiResponse.onSuccess(accessToken);
    }

    @DeleteMapping("/logout")
    @Operation(summary = "로그아웃 API",
            description = "Delete되는 Refresh Token 과 해당하는 userId 응답",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<UserDto.KakaoLogoutTokenResponse> logout(HttpServletRequest req){
        String accessToken = jwtManager.getToken(req);
        System.out.println("Extracted Access Token: " + accessToken);
        Long userId = jwtManager.validateJwt(accessToken);
        UserDto.KakaoLogoutTokenResponse res = tokenService.deleteRefreshToken(userId);
        return ApiResponse.onSuccess(res);
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입 API - 인증 필요",
            description = """
                    Body는 하단의 /my/basicinfo , /my/addinfo api의 설명 참고""",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<UserDto.UserSignUpResponse> signup(HttpServletRequest req, @RequestBody @Valid UserDto.UserSignUpRequest signUpRequest) {
        String aouthtoken = jwtManager.getToken(req);
        return ApiResponse.onSuccess(signupService.signup(aouthtoken, signUpRequest.getDetailRequest(), signUpRequest.getAddInfoRequest()));
    }

}