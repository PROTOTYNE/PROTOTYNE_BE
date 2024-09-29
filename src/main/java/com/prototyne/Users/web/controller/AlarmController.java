package com.prototyne.Users.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.Users.service.AlarmService.AlarmService;
import com.prototyne.config.JwtManager;
import com.prototyne.Users.web.dto.AlarmDto;
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
@RequestMapping("/users/alarm")
@Tag(name = "${swagger.tag.alarm}")
public class AlarmController {

    private final JwtManager jwtManager;
    private final AlarmService alarmService;

    @GetMapping
    @Operation(summary = "알림 조회 API - 인증 필요",
            description = "유저 알림 조회",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<AlarmDto>> GetA(HttpServletRequest req) {
        String aouthtoken = jwtManager.getToken(req);
        return ApiResponse.onSuccess(alarmService.getAlarmList(aouthtoken));
    }
}
