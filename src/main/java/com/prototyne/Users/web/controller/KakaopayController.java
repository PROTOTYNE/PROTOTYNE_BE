package com.prototyne.Users.web.controller;

import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.Users.service.PaymentService.KakaopayService;
import com.prototyne.Users.web.dto.KakaoPayDto;
import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.domain.enums.TicketOption;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kakaopay")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "${swagger.tag.pay}")
public class KakaopayController {
    private final KakaopayService kakaopayService;
    private final JwtManager jwtManager;

    @Operation(summary= "카카오페이 결제 준비",
                description = "카카오페이 결제 준비를 위해 userId와 ticketCount를 넘기면, " +
                        "결제를 위한 TID와 결제 페이지로 redirect할 URL을 응답으로 받습니다.",
                security = {@SecurityRequirement(name = "session-token")})
    @PostMapping("/ready")
    public ApiResponse<KakaoPayDto.KakaoPayReadyResponse> readyPay(
            HttpServletRequest request, @RequestParam TicketOption ticketOption) {

        String accessToken = jwtManager.getToken(request);
        KakaoPayDto.KakaoPayReadyResponse response = kakaopayService.readyToPay(accessToken, ticketOption);
        return ApiResponse.onSuccess(response);
    }
}