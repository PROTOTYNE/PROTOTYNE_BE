package com.prototyne.web.controller;


import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.domain.enums.ProductCategory;
import com.prototyne.service.ProductService.EventService;
import com.prototyne.web.dto.ProductDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Tag(name = "${swagger.tag.product}")
public class ProductController {

    private final EventService eventService;

    @GetMapping("/list")
    @Operation(summary = "시제품 목록 조회 API",
            description = "popular(인기순, 기본), imminent(마감 임박순), new(최신 등록순)")
    public ApiResponse<List<ProductDTO.EventResponse>> getEventsList(
            @RequestParam(value = "type", defaultValue = "popular") String type) {
        List<ProductDTO.EventResponse> eventsList = eventService.getEventsByType(type);
        return ApiResponse.onSuccess(eventsList);
    }

    @GetMapping("/search")
    @Operation(summary = "시제품 검색 조회 API",
            description = "검색어 입력")
    public ApiResponse<List<ProductDTO.SearchResponse>> getSearchesList(
            @RequestParam("name") String name) {
        List<ProductDTO.SearchResponse> searchList = eventService.getEventsBySearch(name);
        return ApiResponse.onSuccess(searchList);
    }

    @GetMapping("/select")
    @Operation(summary = "시제품 카테고리 선택 조회 API",
            description = "카테고리 선택")
    public ApiResponse<List<ProductDTO.SearchResponse>> getCategoriesList(
            @RequestParam(value = "category") String category) {
        List<ProductDTO.SearchResponse> categoriesList = eventService.getEventsByCategory(category);
        return ApiResponse.onSuccess(categoriesList);
    }
}
