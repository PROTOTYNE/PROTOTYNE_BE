package com.prototyne.Enterprise.service.ProductService;

import com.prototyne.Enterprise.web.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    // 기업 id에 따른 시제품 목록 조회 (토큰 인증 필수)
    List<ProductDTO.ProductResponse> getProducts(String accessToken);
}
