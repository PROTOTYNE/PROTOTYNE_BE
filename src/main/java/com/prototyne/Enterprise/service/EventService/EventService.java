package com.prototyne.Enterprise.service.EventService;

import com.prototyne.Enterprise.web.dto.ProductDTO;

import java.util.List;

public interface EventService {
    // 기업 id에 따른 체험 목록 조회 (토큰 인증 필수)
    List<ProductDTO.EventResponse> getEvents(String accessToken);
}
