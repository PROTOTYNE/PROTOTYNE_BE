package com.prototyne.Users.web.controller;

import com.prototyne.Users.service.DeliveryService.DeliveryService;
import com.prototyne.Users.web.dto.DeliveryDto;
import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.config.JwtManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/delivery")
@Tag(name = "${swagger.tag.my-delivery}")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final JwtManager jwtManager;

    @GetMapping
    @Operation(summary = "배송지 목록 조회 API - 인증 필요",
            description = "유저 배송지 목록 조회",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<DeliveryDto.deliveryInfoResponse>> GetDeliveryLists(HttpServletRequest token) {
        String aouthtoken = jwtManager.getToken(token);
        List<DeliveryDto.deliveryInfoResponse> results = deliveryService.getMyDeliveryList(aouthtoken);
        return ApiResponse.onSuccess(results);
    }

    @PostMapping
    @Operation(summary = "배송지 등록 API - 인증 필요",
            description = "유저 새로운 배송지 등록",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<DeliveryDto.deliveryInfoResponse> addNewDeliveryAddress(HttpServletRequest token, DeliveryDto req) {
        String aouthtoken = jwtManager.getToken(token);
        DeliveryDto.deliveryInfoResponse result = deliveryService.addNewDeliveryAddress(aouthtoken, req);
        return ApiResponse.onSuccess(result);
    }

    @GetMapping("/{deliveryId}")
    @Operation(summary = "배송지 상세 조회 API - 인증 필요",
            description = "등록되어있는 배송지 중, 특정 배송지 상세 조회",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<DeliveryDto.deliveryInfoResponse> getOneDeliveryInfo(HttpServletRequest token,
                                                                            @PathVariable("deliveryId") Long deliveryId) {
        String aouthtoken = jwtManager.getToken(token);
        DeliveryDto.deliveryInfoResponse result = deliveryService.getOneDeliveryInfo(aouthtoken, deliveryId);
        return ApiResponse.onSuccess(result);
    }

    @PatchMapping("/{deliveryId}")
    @Operation(summary = "배송지 수정 API - 인증 필요",
            description = "유저 기존 배송지 정보 수정",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<DeliveryDto.deliveryInfoResponse> updateDeliveryInfo(HttpServletRequest token,
                                                                            @PathVariable("deliveryId") Long deliveryId,
                                                                            DeliveryDto req) {
        String aouthtoken = jwtManager.getToken(token);
        DeliveryDto.deliveryInfoResponse result = deliveryService.updateDeliveryInfo(aouthtoken, deliveryId, req);
        return ApiResponse.onSuccess(result);
    }

    @DeleteMapping("/{deliveryId}")
    @Operation(summary = "배송지 삭제 API - 인증 필요",
            description = "유저 기존 배송지 정보 삭제",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<DeliveryDto.deliveryInfoResponse>> deleteDeliveryInfo(HttpServletRequest token,
                                                                            @PathVariable("deliveryId") Long deliveryId)
    {
        String aouthtoken = jwtManager.getToken(token);
        List<DeliveryDto.deliveryInfoResponse> results = deliveryService.deleteDeliveryAddr(aouthtoken, deliveryId);
        return ApiResponse.onSuccess(results);
    }
}