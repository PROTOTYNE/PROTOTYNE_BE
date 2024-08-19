package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.service.ApplicationService.ApplicationService;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.web.dto.InvestmentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final JwtManager jwtManager;

    @Tag(name = "${swagger.tag.product-etc}")
    @PostMapping("/{eventId}")
    @Operation(summary="체험 신청 API",security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<InvestmentDTO.ApplicationResponse> Application(HttpServletRequest token, @PathVariable("eventId") Long eventId){
        String aouthtoken = jwtManager.getToken(token);
        InvestmentDTO.ApplicationResponse applicationResponse= applicationService.Application(aouthtoken, eventId);
        return ApiResponse.onSuccess(applicationResponse);
    }
}
