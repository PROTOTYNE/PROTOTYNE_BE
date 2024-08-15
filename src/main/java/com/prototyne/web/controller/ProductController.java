package com.prototyne.web.controller;


import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.service.ProductService.EventService;
import com.prototyne.web.dto.ProductDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Tag(name = "${swagger.tag.product}")
public class ProductController {

    private final EventService eventService;
    private final JwtManager jwtManager;

    @GetMapping("/list")
    @Operation(summary = "시제품 목록 조회 API",
            description = """
                정렬 타입 입력 ("" 없이 입력)\n
                type = "popular"(인기순, 기본) | "imminent"(마감 임박순) | "new"(최신 등록순) \n""")
    public ApiResponse<List<ProductDTO.EventResponse>> getEventsList(
            @RequestParam(value = "type", defaultValue = "popular") String type) {
        List<ProductDTO.EventResponse> eventsList = eventService.getEventsByType(type);
        return ApiResponse.onSuccess(eventsList);
    }

    @PostMapping("/search")
    @Operation(summary = "시제품 검색 조회 & 유저 최근 검색어 리스트에 저장 API - 인증 필요",
            description = "검색어 입력",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<ProductDTO.SearchResponse>> getSearchesList(
            @RequestParam("name") String name,
            HttpServletRequest request) {
        String accessToken = jwtManager.getToken(request);
        List<ProductDTO.SearchResponse> searchList = eventService.getEventsBySearch(name, accessToken);
        return ApiResponse.onSuccess(searchList);
    }

    @GetMapping("/search/recent")
    @Operation(summary = "최근 검색어 리스트 조회 API - 인증 필요",
            description = "사용자의 최근 검색어 리스트(10개 최신순)을 조회하는 API",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<String>> getRecentSearches(
            HttpServletRequest request) {
        String accessToken = jwtManager.getToken(request);
        List<String> recentSearchList = eventService.getRecentSearches(accessToken);
        return ApiResponse.onSuccess(recentSearchList);
    }

    @DeleteMapping("/search")
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

    @DeleteMapping("/search/all")
    @Operation(summary = "최근 검색어 초기화 API - 인증 필요",
            description = "사용자의 최근 검색어 목록을 모두 삭제하는 API, 반환 값 = 초기화된 최근 검색어 리스트(null이어야 함)",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<String>> deleteAllSearchHistory(
            HttpServletRequest request) {
        String accessToken = jwtManager.getToken(request);
        List<String> recentSearchList = eventService.deleteAllSearchHistory(accessToken);
        return ApiResponse.onSuccess(recentSearchList);
    }

    @GetMapping("/detail/{eventId}")
    @Operation(summary = "시제품 상세보기 API - 인증 필요",
            description = "이벤트 시제품 id 입력",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<ProductDTO.EventDetailsResponse> getEventDetails(
            HttpServletRequest token,
            @PathVariable("eventId") Long eventId) {
        String oauthToken = jwtManager.getToken(token);
        ProductDTO.EventDetailsResponse eventDetails = eventService.getEventDetailsById(oauthToken, eventId);
        return ApiResponse.onSuccess(eventDetails);
    }

    @GetMapping("/select")
    @Operation(summary = "시제품 카테고리 선택 조회 API",
            description = """
                카테고리 입력 ("" 없이 입력) \n
                category = "뷰티" | "스포츠" | "식품" | "의류" | "전자기기" | "장난감" \n""")
    public ApiResponse<List<ProductDTO.SearchResponse>> getCategoriesList(
            @RequestParam(value = "category") String category) {
        List<ProductDTO.SearchResponse> categoriesList = eventService.getEventsByCategory(category);
        return ApiResponse.onSuccess(categoriesList);
    }
}
