package com.prototyne.Users.web.controller;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.config.JwtManager;
import com.prototyne.Users.service.EventService.EventService;
import com.prototyne.Users.web.dto.ProductDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/search")
@Tag(name = "${swagger.tag.product-search}")
public class SearchController {
    private final EventService eventService;
    private final JwtManager jwtManager;
    @PostMapping("")
    @Operation(summary = "시제품 검색 조회 & 유저 최근 검색어 리스트에 저장 API - 인증 필요",
            description = "검색어 입력",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<ProductDTO.SearchResponse>> getSearchesList(
            HttpServletRequest request,
            @RequestParam("name") String name) {
        String accessToken = jwtManager.getToken(request);
        List<ProductDTO.SearchResponse> searchList = eventService.getEventsBySearch(accessToken, name);

        return ApiResponse.onSuccess(searchList);
    }

    @GetMapping("/recent")
    @Operation(summary = "최근 검색어 리스트 조회 API - 인증 필요",
            description = "사용자의 최근 검색어 리스트(10개 최신순)을 조회하는 API",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<String>> getRecentSearches(
            HttpServletRequest request) {
        String accessToken = jwtManager.getToken(request);
        List<String> recentSearchList = eventService.getRecentSearches(accessToken);
        return ApiResponse.onSuccess(recentSearchList);
    }

    @DeleteMapping("")
    @Operation(summary = "최근 검색어 1개 삭제 API - 인증 필요",
            description = "사용자의 최근 검색어 목록 중 하나를 삭제하는 API, 반환 값 = 특정 검색어가 삭제된 최근 검색어 리스트",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<String>> deleteOneSearchHistory(
            @RequestParam("name") String name,
            HttpServletRequest request) {
        String accessToken = jwtManager.getToken(request);
        List<String> recentSearchList = eventService.deleteSearchHistory(name, accessToken);
        return ApiResponse.onSuccess(recentSearchList);
    }

    @DeleteMapping("/all")
    @Operation(summary = "최근 검색어 초기화 API - 인증 필요",
            description = "사용자의 최근 검색어 목록을 모두 삭제하는 API, 반환 값 = 초기화된 최근 검색어 리스트(null이어야 함)",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<String>> deleteAllSearchHistory(
            HttpServletRequest request) {
        String accessToken = jwtManager.getToken(request);
        List<String> recentSearchList = eventService.deleteAllSearchHistory(accessToken);
        return ApiResponse.onSuccess(recentSearchList);
    }
}
