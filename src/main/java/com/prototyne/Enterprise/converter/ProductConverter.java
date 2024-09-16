package com.prototyne.Enterprise.converter;

import com.prototyne.Enterprise.web.dto.ProductDTO;
import com.prototyne.domain.Product;

public class ProductConverter {
    // 시제품 목록 조회 응답 형식으로
    public static ProductDTO.ProductResponse toProductResponse(Product product) {
        return ProductDTO.ProductResponse.builder()
                .productId(product.getId())
                .thumbnailUrl(product.getThumbnailUrl())
                .productName(product.getName())
                .reqTickets(product.getReqTickets())
                .createdDate(product.getCreatedAt().toLocalDate())
                .category(product.getCategory())
                // 진행 중인 체험이 없다면 0
                .eventCount(product.getEventList() != null ? product.getEventList().size() : 0)
                .build();
    }
}
