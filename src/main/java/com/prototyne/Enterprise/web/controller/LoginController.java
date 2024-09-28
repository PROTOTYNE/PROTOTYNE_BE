package com.prototyne.Enterprise.web.controller;

import com.prototyne.Enterprise.service.LoginService.LoginService;
import com.prototyne.Enterprise.service.TokenService.EnterpriseTokenService;
import com.prototyne.Enterprise.web.dto.LoginDto;
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

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/enterprise")
@Tag(name = "${swagger.tag.enterprise-auth}")
public class LoginController {

    private final LoginService loginService;
    private final EnterpriseTokenService tokenService;
    private final JwtManager jwtManager;

    @PostMapping("/signup")
    @Operation(summary = "기업 회원가입 API",
            description = "기업 회원가입 API")
    public ApiResponse<LoginDto.EnterpriseSignupResponse> enterpriseSignup(@RequestBody @Valid LoginDto.EnterpriseSignupRequest enterpriseSignupRequest) {
        LoginDto.EnterpriseSignupResponse response = loginService.registerEnterprise(enterpriseSignupRequest);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/login")
    @Operation(summary = "기업 로그인 API",
            description = "기업 로그인 API- Access Token, Refresh Token 응답")
    public ApiResponse<LoginDto.EnterpriseLoginResponse> enterpriseLogin(@RequestBody @Valid LoginDto.EnterpriseLoginRequest enterpriseLoginRequest) {
        LoginDto.EnterpriseLoginResponse response = loginService.loginEnterprise(enterpriseLoginRequest);
        return ApiResponse.onSuccess(response);
    }

    @DeleteMapping("/logout")
    @Operation(summary = "기업 로그아웃 API",
            description = "Delete되는 Refresh Token 과 해당하는 enterpriseId 응답",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<LoginDto.EnterpriseLogoutResponse> enterpriseLogout(HttpServletRequest req){
        String accessToken = jwtManager.getToken(req);
        System.out.println("accessToken: "+accessToken);
        Long enterpriseId = jwtManager.validateJwt(accessToken);
        LoginDto.EnterpriseLogoutResponse res= tokenService.deleteRefreshToken(enterpriseId);
        return ApiResponse.onSuccess(res);
    }
}
