package com.prototyne.Enterprise.web.controller;

import com.prototyne.Enterprise.service.EventService.EventService;
import com.prototyne.Enterprise.web.dto.EventDTO;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.domain.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enterprise")
@Tag(name = "${swagger.tag.enterprise-event}")
public class EventController {

    private final JwtManager jwtManager;
    private final EventService eventService;

    // 체험 목록 조회
    @GetMapping("/events")
    @Operation(summary = "체험 목록 조회 API - 인증 필수" ,
            description = "시제품 체험 목록 > 체험 목록" +"""
        --- stageAndDate는 stage(단계)와 stageDate(그에 따른 날짜)로 구성

        1: 시작 전              | 시작일             | event_start
        2: 신청자 모집 기간     | 신청자 모집 종료    | event_end
        3: 당첨자 발표 기간     | 당첨자 발표 종료    | release_end
        4: 후기 작성 기간       | 후기 작성 종료      | feedback_end
        5: 종료                 | 종료일             | end_date
    """, security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<EventDTO.EventResponse>> getEventsList(HttpServletRequest token) {
        String oauthToken = jwtManager.getToken(token);
        List<EventDTO.EventResponse> eventList = eventService.getEvents(oauthToken);
        return ApiResponse.onSuccess(eventList);
    }

    // 시제품에 체험 등록
    @PostMapping("/products/{productId}/event")
    @Operation(summary = "체험 등록 API - 인증 필수",
            description = """
                    시제품에 체험 등록(추가) \n
                    productId 입력 \n
                    요청 성공 시, 체험 아이디(event_id) 반환""",
            security = {@SecurityRequirement(name = "session-token")} )
    public ApiResponse<Long> createEvent(HttpServletRequest token,
                                         @PathVariable Long productId,
                                         @Valid @RequestBody EventDTO.EventDate eventRequest) {
        String oauthToken = jwtManager.getToken(token);
        Long eventId = eventService.createEvent(oauthToken, productId, eventRequest);
        return ApiResponse.onSuccess(eventId);
    }

    // 체험 정보 조회
    @GetMapping("/events/{eventId}")
    @Operation(summary = "체험 정보 조회 API - 인증 필수",
            description = """
                    체험 상세 조회 \n 
                    eventId 입력""",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<EventDTO.EventInfo> getEvent(HttpServletRequest token, @PathVariable Long eventId) {
        String oauthToken = jwtManager.getToken(token);
        EventDTO.EventInfo event = eventService.getEventInfo(oauthToken, eventId);
        return ApiResponse.onSuccess(event);
    }

    // 체험 삭제
    @DeleteMapping("/events/{eventId}")
    @Operation(summary = "체험 삭제 API - 인증 필수",
            description = "구현 중",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<String> deleteEvent(HttpServletRequest token, @PathVariable Long eventId) {
        String oauthToken = jwtManager.getToken(token);
        return ApiResponse.onSuccess("삭제 성공");
    }
}
