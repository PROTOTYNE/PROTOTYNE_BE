package com.prototyne.Enterprise.web.controller;

import com.prototyne.Enterprise.service.LoginService.LoginService;
import com.prototyne.Enterprise.web.dto.LoginDto;
import com.prototyne.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @PostMapping("/signup")
    @Operation(summary = "기업 회원가입 API",
            description = "기업 회원가입 API")
    public ApiResponse<LoginDto.EnterpriseSignupResponse> enterpriseSignup(@RequestBody @Valid LoginDto.EnterpriseSignupRequest enterpriseSignupRequest) {
        LoginDto.EnterpriseSignupResponse response = loginService.registerEnterprise(enterpriseSignupRequest);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/login")
    @Operation(summary = "기업 로그인 API",
            description = "기업 로그인 API")
    public ApiResponse<LoginDto.EnterpriseLoginResponse> enterpriseLogin(@RequestBody @Valid LoginDto.EnterpriseLoginRequest enterpriseLoginRequest) {
        LoginDto.EnterpriseLoginResponse response = loginService.loginEnterprise(enterpriseLoginRequest);
        return ApiResponse.onSuccess(response);
    }

}
