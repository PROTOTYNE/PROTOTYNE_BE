package com.prototyne.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.service.MyProductService.MyProductServiceImpl;
import com.prototyne.web.dto.MyProductDto;
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
@RequestMapping("/myproduct")
public class MyProductController {
    private final MyProductServiceImpl myProductService;

    @Tag(name = "${swagger.tag.myproduct}")
    @GetMapping("/request")
    @Operation(summary = "체험 신청 내역 조회 API - 인증필요", description = "체험 신청 내역 조회 API - 인증필요", security = {@SecurityRequirement(name="session-token")})
    public ApiResponse<List<MyProductDto>> allMyProduct(HttpServletRequest request) throws Exception {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");

        return ApiResponse.onSuccess(myProductService.getAllMyProduct(accessToken));
    }
}
