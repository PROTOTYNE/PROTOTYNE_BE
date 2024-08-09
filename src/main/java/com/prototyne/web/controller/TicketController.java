package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.service.TicketService.TicketService;
import com.prototyne.web.dto.TicketDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;

    @Tag(name = "${swagger.tag.my-etc}")
    @GetMapping
    @Operation(summary = "티켓 개수 조회 API - 인증 필요",
            description = "티켓 개수 조회",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<TicketDto.TicketNumberDto> getTicket(HttpServletRequest token) {
        String aouthtoken = token.getHeader("Authorization").replace("Bearer ", "");
        TicketDto.TicketNumberDto ticketNumber = ticketService.getTicketNumber(aouthtoken);
        return ApiResponse.onSuccess(ticketNumber);
    }

    @Tag(name = "${swagger.tag.my-etc}")
    @GetMapping("/list")
    public ApiResponse<String> getTicketList(HttpServletRequest token) {
        String aouthtoken = token.getHeader("Authorization").replace("Bearer ", "");

        return ApiResponse.onSuccess("success");
    }
}
