package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.service.AlarmService.AlarmService;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.web.dto.AlarmDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/alarm")
@Tag(name = "${swagger.tag.alarm}")
public class AlarmController {

    private final JwtManager jwtManager;
    private final AlarmService alarmService;

    @GetMapping
    @Operation(summary = "알림 조회 API - 인증 필요",
            description = "유저 알림 조회",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<AlarmDto>> GetA(HttpServletRequest token) {
        String aouthtoken = jwtManager.getToken(token);
        return ApiResponse.onSuccess(alarmService.getAlarmList(aouthtoken));
    }
}
