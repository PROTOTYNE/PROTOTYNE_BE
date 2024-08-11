package com.prototyne.web.controller;


import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.service.ProductService.EventService;
import com.prototyne.web.dto.ProductDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final EventService eventService;

    @Tag(name = "${swagger.tag.product}")
    @GetMapping("/list")
    @Operation(summary = "시제품 목록 조회 API",
            description = "popular(인기순, 기본), imminent(마감 임박순), new(최신 등록순)")
    public ApiResponse<List<ProductDTO.EventResponse>> getEventsList(
            @RequestParam(value = "type", defaultValue = "popular") String type) {
        List<ProductDTO.EventResponse> eventsList = eventService.getEventsByType(type);
        return ApiResponse.onSuccess(eventsList);
    }

}
