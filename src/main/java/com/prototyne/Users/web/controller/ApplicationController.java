package com.prototyne.Users.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.Users.service.ApplicationService.ApplicationService;
import com.prototyne.config.JwtManager;
import com.prototyne.Users.web.dto.InvestmentDTO;
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
@RequestMapping("/users/application")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final JwtManager jwtManager;

    @Tag(name = "${swagger.tag.product-etc}")
    @PostMapping("/{eventId}")
    @Operation(summary="체험 신청 API",security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<InvestmentDTO.ApplicationResponse> Application(HttpServletRequest token,
                                                                      @PathVariable("deliveryId") Long deliveryId,
                                                                      @PathVariable("eventId") Long eventId){
        String aouthtoken = jwtManager.getToken(token);
        InvestmentDTO.ApplicationResponse applicationResponse= applicationService.Application(aouthtoken, deliveryId, eventId);
        return ApiResponse.onSuccess(applicationResponse);
    }
}
