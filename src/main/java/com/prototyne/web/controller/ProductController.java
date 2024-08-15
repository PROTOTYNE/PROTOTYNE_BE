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

    @GetMapping("/home")
    @Operation(summary = "홈 화면 조회 API - 인증 필요",
            description = """
                홈 화면 조회 \n
                인기순(3), 마감 임박(2), 신규 등록(2) \n""",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<ProductDTO.HomeResponse> getHome(HttpServletRequest token) {
        String oauthToken = jwtManager.getToken(token);
        ProductDTO.HomeResponse home = eventService.getHomeById(oauthToken);
        return ApiResponse.onSuccess(home);
    }

    @GetMapping("/list")
    @Operation(summary = "시제품 목록 조회 API - 인증 필요" ,
            description = """
                정렬 타입 입력 ("" 없이 입력)\n
                type = "popular"(인기순, 기본) | "imminent"(마감 임박순) | "new"(최신 등록순) \n""",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<ProductDTO.EventResponse>> getEventsList(
            HttpServletRequest token,
            @RequestParam(value = "type", defaultValue = "popular") String type) {
        String oauthToken = jwtManager.getToken(token);
        Long userId = jwtManager.validateJwt(oauthToken);
        List<ProductDTO.EventResponse> eventsList = eventService.getEventsByType(userId, type);
        return ApiResponse.onSuccess(eventsList);
    }

    @GetMapping("/search")
    @Operation(summary = "시제품 검색 조회 API",
            description = "검색어 입력 - 인증 필요")
    public ApiResponse<List<ProductDTO.SearchResponse>> getSearchesList(
            @RequestParam("name") String name) {
        List<ProductDTO.SearchResponse> searchList = eventService.getEventsBySearch(name);
        return ApiResponse.onSuccess(searchList);
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
