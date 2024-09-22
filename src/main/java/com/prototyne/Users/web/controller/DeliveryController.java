package com.prototyne.Users.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.Users.service.DeliveryService.DeliveryService;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.Users.web.dto.DeliveryDto;
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
@RequestMapping("/users/delivery")
@Tag(name = "${swagger.tag.my-delivery}")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final JwtManager jwtManager;

    @GetMapping
    @Operation(summary = "배송지 조회 API - 인증 필요",
            description = "유저 배송지 조회",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<DeliveryDto> GetDelivery(HttpServletRequest token) {
        String aouthtoken = jwtManager.getToken(token);
        DeliveryDto deliveryDto = deliveryService.getDeliveryInfo(aouthtoken);
        return ApiResponse.onSuccess(deliveryDto);
    }

    @PatchMapping
    @Operation(summary = "배송지 수정 API - 인증 필요",
            description = "유저 배송지 수정",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<DeliveryDto> PatchDelivery(HttpServletRequest token, @RequestBody @Valid DeliveryDto newDeliveryDto) {
        String aouthtoken = jwtManager.getToken(token);
        DeliveryDto deliveryDto = deliveryService.patchDeliveryInfo(aouthtoken, newDeliveryDto);
        return ApiResponse.onSuccess(deliveryDto);
    }
}
