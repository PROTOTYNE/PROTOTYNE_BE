package com.prototyne.Enterprise.service.ProductService;

import com.prototyne.Enterprise.web.dto.ProductDTO;
import com.prototyne.domain.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    // 기업 id에 따른 시제품 목록 조회 (토큰 인증 필수)
    List<ProductDTO.ProductResponse> getProducts(String accessToken);

    // 기업 id로부터 시제품 등록 (토큰 인증 필수) - id 반환
    Long createProduct(String accessToken, ProductDTO.CreateProductRequest productRequest, List<MultipartFile> images);

    // 기업 id가 가진 시제품 삭제
    void deleteProduct(String accessToken, Long productId);
}
