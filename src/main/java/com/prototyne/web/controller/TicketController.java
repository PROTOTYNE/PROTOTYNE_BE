package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
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

    @Tag(name = "${swagger.tag.my-etc}")
    @GetMapping
    public ApiResponse<String> getTicket(HttpServletRequest token) {
        String aouthtoken = token.getHeader("Authorization").replace("Bearer ", "");

        return ApiResponse.onSuccess("success");
    }

    @Tag(name = "${swagger.tag.my-etc}")
    @GetMapping("/list")
    public ApiResponse<String> getTicketList(HttpServletRequest token) {
        String aouthtoken = token.getHeader("Authorization").replace("Bearer ", "");

        return ApiResponse.onSuccess("success");
    }
}
