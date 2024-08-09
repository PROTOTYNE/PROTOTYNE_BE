package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.domain.User;
import com.prototyne.service.LoginService.KakaoServiceImpl;
import com.prototyne.service.SignupService.UserSignupService;
import com.prototyne.service.SignupService.UserSignupServiceImpl;
import com.prototyne.web.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
public class SignupController {
    private final UserSignupServiceImpl signupService;

    @Tag(name = "${swagger.tag.auth}")
    @PostMapping("/signup")
    @Operation(summary = "회원가입 API - 인증 필요",
            description = "회원가입 API - 인증 필요",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<UserDto.UserSignUpResponse> userInfo(HttpServletRequest token,
                                        @RequestBody @Valid UserDto.UserSignUpRequest signUpRequest) {

        String aouthtoken = token.getHeader("Authorization").replace("Bearer ", "");
        User user = signupService.signup(aouthtoken, signUpRequest.getDetailRequest(), signUpRequest.getAddInfoRequest());

        UserDto.UserSignUpResponse signUpResponse = new UserDto.UserSignUpResponse(user.getId(), aouthtoken);
        return ApiResponse.onSuccess(signUpResponse);
    }
}
