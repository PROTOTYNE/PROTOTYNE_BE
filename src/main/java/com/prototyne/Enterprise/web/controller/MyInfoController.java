package com.prototyne.Enterprise.web.controller;

import com.prototyne.Enterprise.service.MyInfoService.MyInfoService;
import com.prototyne.Enterprise.web.dto.MyInfoDTO;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/enterprise")
@Tag(name = "${swagger.tag.enterprise-my}")
public class MyInfoController {
    private final MyInfoService myInfoService;
    private final JwtManager jwtManager;

    @GetMapping("/my")
    @Operation(summary="기업 마이페이지 API", description = "기업 마이페이지 API", security = {@SecurityRequirement(name="session-token")})
    public ApiResponse<MyInfoDTO> getMyInfo(HttpServletRequest token)throws Exception{
        String accessToken = jwtManager.getToken(token);

        MyInfoDTO myInfoDTO = myInfoService.getMyInfo(accessToken);
        return ApiResponse.onSuccess(myInfoDTO);
    }
}
