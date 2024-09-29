package com.prototyne.Enterprise.web.controller;



import com.prototyne.Enterprise.service.UserlistService.UserlistService;
import com.prototyne.Enterprise.web.dto.UserlistDTO;
import com.prototyne.config.JwtManager;
import com.prototyne.apiPayload.ApiResponse;
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
@RequestMapping("/enterprise")
@Tag(name = "${swagger.tag.enterprise-userlist}")
public class UserlistController {

    private final UserlistService userlistService;

    private final JwtManager jwtManager;

    @GetMapping("/user/list/{eventId}")
    @Operation(summary = "신청자 목록 조회 API - 인증 필수")
    @SecurityRequirement(name = "session-token")
    public ApiResponse<List<UserlistDTO.UserListResponse>> getUserList(HttpServletRequest token, @PathVariable Long eventId) {
        String accessToken = jwtManager.getToken(token);
        List<UserlistDTO.UserListResponse> userList = userlistService.getUserList(accessToken, eventId);
        return ApiResponse.onSuccess(userList);
    }

    @PatchMapping("/user/prize/{eventId}")
    @Operation(summary = "신청자 당첨 여부 수정 API -인증 필수")
    @SecurityRequirement(name = "session-token")
    public ApiResponse<UserlistDTO.UserListResponse> updateUserPrize(HttpServletRequest token, @PathVariable Long eventId, @RequestParam("userId") Long userId, @RequestParam("isPrize") Boolean isPrize) {
        String accessToken = jwtManager.getToken(token);
        UserlistDTO.UserListResponse userlist = userlistService.updateUserPrize(accessToken, eventId, userId, isPrize);
        return ApiResponse.onSuccess(userlist);
    }

    @PatchMapping("/user/delivery/{eventId}")
    @Operation(summary = "신청자 배송 정보 수정 API - 인증 필수")
    @SecurityRequirement(name = "session-token")
    public ApiResponse<UserlistDTO.UserListResponse> updateUserDelivery(HttpServletRequest token, @PathVariable Long eventId, @RequestParam("userId") Long userId, @RequestParam("deliveryCompany") String deliveryCompany, @RequestParam("transportNum") String transportNum) {
        String accessToken = jwtManager.getToken(token);
        UserlistDTO.UserListResponse userlist = userlistService.updateUserDelivery(accessToken, eventId, userId, deliveryCompany, transportNum);
        return ApiResponse.onSuccess(userlist);

    }
}
