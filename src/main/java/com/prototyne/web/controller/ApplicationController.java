package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.service.ApplicationService.ApplicationService;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.web.dto.InvestmentDTO;
import com.prototyne.web.dto.TicketDto;
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
@RequestMapping("/application")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final JwtManager jwtManager;

    @Tag(name = "${swagger.tag.product-etc}")
    @PostMapping
    @Operation(summary="체험 신청 API",security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<InvestmentDTO.ApplicationResponse> Application(HttpServletRequest token,Long productId, String ticketName, String ticketDesc){
        String aouthtoken = jwtManager.getToken(token);
        InvestmentDTO.ApplicationResponse applicationResponse= applicationService.Application(aouthtoken, productId, ticketName, ticketDesc);
        return ApiResponse.onSuccess(applicationResponse);
        //컨트롤러 파라미터 값 TicketDto.TicketListDto ticketListDto ??
    }
}
