package com.prototyne.Users.web.controller;

import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.Users.service.PaymentService.KakaopayService;
import com.prototyne.Users.web.dto.KakaoPayDto;
import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.enums.TicketOption;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kakaopay")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "${swagger.tag.pay}")
public class KakaopayController {
    private final KakaopayService kakaopayService;
    private final JwtManager jwtManager;

    @Operation(summary = "카카오페이 결제 준비 API",
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

    @Operation(summary = "카카오페이 결제 성공 API",
            description = "카카오페이 결제 승인을 위해, 받은 pg_token값을 넘겨주면, approval_url로 리다이렉트 되며 결제에 성공.",
            security = {@SecurityRequirement(name = "session-token")})
    @PostMapping("/success")
    public ApiResponse<KakaoPayDto.KakaoPayApproveResponse> approvePay(
            HttpServletRequest request,
            @RequestParam String pgToken) {
        String accessToken = jwtManager.getToken(request);
        KakaoPayDto.KakaoPayApproveResponse response = kakaopayService.approvePayment(accessToken,
                 pgToken);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "카카오페이 결제 취소 API",
            description = "카카오페이 결제 진행 중 클라이언트가 결제를 취소했을 때 쓰이는 API",
            security = {@SecurityRequirement(name = "session-token")})
    @GetMapping("/cancel")
    public void cancel(){
        throw new TempHandler(ErrorStatus.PAYMENT_APPROVE_CANCEL);
    }

    @Operation(summary = "카카오페이 결제 실패 API",
            description = "카카오페이 결제 진행 중 결제에 실패하였을 때 쓰이는 API",
            security = {@SecurityRequirement(name = "session-token")})
    @GetMapping("/fail")
    public void fail(){
        throw new TempHandler(ErrorStatus.PAYMENT_APPROVE_FAILURE);
    }
}
