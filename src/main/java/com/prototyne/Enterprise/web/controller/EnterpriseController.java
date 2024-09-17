package com.prototyne.Enterprise.web.controller;

import com.prototyne.Enterprise.service.EnterpriseService.EnterpriseService;
import com.prototyne.Enterprise.web.dto.EnterpriseDto;
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
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    @PostMapping("/signup")
    @Operation(summary = "기업 회원가입 API",
            description = "기업 회원가입 API")
    public ApiResponse<EnterpriseDto.EnterpriseSignupResponse> enterpriseSignup(@RequestBody @Valid EnterpriseDto.EnterpriseSignupRequest enterpriseSignupRequest) {
        EnterpriseDto.EnterpriseSignupResponse response = enterpriseService.registerEnterprise(enterpriseSignupRequest);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/login")
    @Operation(summary = "기업 로그인 API",
            description = "기업 로그인 API")
    public ApiResponse<EnterpriseDto.EnterpriseLoginResponse> enterpriseLogin(@RequestBody @Valid EnterpriseDto.EnterpriseLoginRequest enterpriseLoginRequest) {
        EnterpriseDto.EnterpriseLoginResponse response = enterpriseService.loginEnterprise(enterpriseLoginRequest);
        return ApiResponse.onSuccess(response);
    }

}
