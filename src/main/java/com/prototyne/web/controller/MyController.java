package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.service.UserService.UserDetailServiceImpl;
import com.prototyne.web.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/my")
@Tag(name = "02. 내 정보 - 기본", description = "유저 정보 관련 API")
public class MyController {

    private final UserDetailServiceImpl userDetailService;

    @Tag(name = "${swagger.tag.auth}")
    @GetMapping("/detail")
    @Operation(summary = "유저 정보 조회 API - 인증 필요",
            description = "유저 정보 조회 API - 인증 필요",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<UserDto.UserDetailResponse> getUserDetail(HttpServletRequest request) throws Exception {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        log.info("JWT Token: {}", accessToken);
        UserDto.UserDetailResponse userDetailResponse = userDetailService.getUserDetail(accessToken);
        return ApiResponse.onSuccess(userDetailResponse);
    }

    @PatchMapping("/basicInfo")
    @Operation(summary = "필수 정보 수정 API - 인증 필요",
            description = "유저의 필수 정보를 수정합니다.",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<UserDto.DetailInfo> updateDetailInfo(HttpServletRequest request,
                                                @RequestBody @Valid UserDto.DetailInfo detailInfo) throws UserPrincipalNotFoundException {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        log.info("JWT Token: {}", accessToken);

        return ApiResponse.onSuccess(userDetailService.updateBasicInfo(accessToken, detailInfo));

    }

    @PatchMapping("/addInfo")
    @Operation(summary = "추가 정보 수정 API - 인증 필요",
            description = "유저의 추가 정보를 수정합니다.",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<UserDto.AddInfo> updateAddInfo(HttpServletRequest request,
                                         @RequestBody @Valid UserDto.AddInfo addInfo) throws UserPrincipalNotFoundException {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        log.info("JWT Token: {}", accessToken);

        return ApiResponse.onSuccess(userDetailService.updateAddInfo(accessToken, addInfo));
    }

}
