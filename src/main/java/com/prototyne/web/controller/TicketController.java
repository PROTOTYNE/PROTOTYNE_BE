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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

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
    @GetMapping("/all")
    @Operation(summary = "티켓 전체 내역 조회 API - 인증 필요",
            description = """
                    티켓 전체 내역 조회
                    - startDate: 조회 시작일 (yyyy-MM-dd)
                    - endDate: 조회 종료일 (yyyy-MM-dd)
                    """,
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<TicketDto.TicketListDto>> getTicketList(HttpServletRequest token, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        String aouthtoken = token.getHeader("Authorization").replace("Bearer ", "");
        List<TicketDto.TicketListDto> ticketList = ticketService.getTicketList(aouthtoken).stream()
                .filter(ticket -> {
                    LocalDate ticketDate = ticket.getCreatedAt().toLocalDate();
                    return !ticketDate.isBefore(startDate) && !ticketDate.isAfter(endDate);
                })
                .toList();
        return ApiResponse.onSuccess(ticketList);
    }

    @Tag(name = "${swagger.tag.my-etc}")
    @GetMapping("/used")
    @Operation(summary = "티켓 사용 내역 조회 API - 인증 필요",
            description = """
                    티켓 사용 내역 조회
                    - startDate: 조회 시작일 (yyyy-MM-dd)
                    - endDate: 조회 종료일 (yyyy-MM-dd)
                    """,
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<TicketDto.TicketListDto>> getTicketListUsed(HttpServletRequest token, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        String aouthtoken = token.getHeader("Authorization").replace("Bearer ", "");
        List<TicketDto.TicketListDto> ticketList = ticketService.getTicketList(aouthtoken).stream()
                .filter(ticket -> ticket.getTicketChange() < 0)
                .filter(ticket -> {
                    LocalDate ticketDate = ticket.getCreatedAt().toLocalDate();
                    return !ticketDate.isBefore(startDate) && !ticketDate.isAfter(endDate);
                })
                .toList();
        return ApiResponse.onSuccess(ticketList);
    }
}
