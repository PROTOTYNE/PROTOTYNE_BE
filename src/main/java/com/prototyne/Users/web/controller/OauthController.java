package com.prototyne.Users.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.Users.service.LoginService.KakaoServiceImpl;
import com.prototyne.Users.service.SignupService.UserSignupServiceImpl;
import com.prototyne.Users.web.dto.UserDto;
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
            description = """
                    Body는 하단의 /my/basicinfo , /my/addinfo api의 설명 참고""",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<UserDto.UserSignUpResponse> signup(HttpServletRequest token, @RequestBody @Valid UserDto.UserSignUpRequest signUpRequest) {
        String aouthtoken = jwtManager.getToken(token);
        return ApiResponse.onSuccess(signupService.signup(aouthtoken, signUpRequest.getDetailRequest(), signUpRequest.getAddInfoRequest()));
    }

}