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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResponse<DeliveryDto.deliveryInfoResponse> addNewDeliveryAddress(HttpServletRequest token, DeliveryDto req){
        String aouthtoken = jwtManager.getToken(token);
        DeliveryDto.deliveryInfoResponse result = deliveryService.addNewDeliveryAddress(aouthtoken, req);
        return ApiResponse.onSuccess(result);
   }
}
