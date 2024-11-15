package com.prototyne.Users.web.controller;

import com.prototyne.Users.service.AlarmService.AlarmService;
import com.prototyne.Users.web.dto.AlarmDto;
import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.config.JwtManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/alarm")
@Tag(name = "${swagger.tag.alarm}")
public class AlarmController {

    private final JwtManager jwtManager;
    private final AlarmService alarmService;
    public static Map<Long, SseEmitter> sseEmmiters = new ConcurrentHashMap<>();

    @GetMapping("/subscribe")
    @Operation(summary = "알림 실시간 구독 API - 인증 필요",
            description = "유저가 SSE(Server-Sent Events) 방식으로 실시간 알림을 구독하는 API입니다." +
                    "이 API를 호출하면, 실시간 알림을 수신할 수 있습니다.",
            security = {@SecurityRequirement(name = "session-token")})
    public SseEmitter subscribe(HttpServletRequest req){
        String accessToken= jwtManager.getToken(req);
        return alarmService.subscribe(accessToken);
    }

    @GetMapping
    @Operation(summary = "알림 조회 API - 인증 필요",
            description = "유저 알림 조회",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<AlarmDto>> GetA(HttpServletRequest req) {
        String aouthtoken = jwtManager.getToken(req);
        return ApiResponse.onSuccess(alarmService.getAlarmList(aouthtoken));
    }


}
