package com.prototyne.Enterprise.converter;

import com.prototyne.Enterprise.web.dto.ProductDTO;
import com.prototyne.domain.Enterprise;
import com.prototyne.domain.Product;
import com.prototyne.domain.ProductImage;

import java.util.ArrayList;
import java.util.List;

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
                .launchedDate(product.getLaunchedDate())
                .build();
    }

    // 시제품 엔티티 형식으로
    public static Product toProduct(ProductDTO.ProductInfo req1,
                                    ProductDTO.Questions req2,
                                    Enterprise enterprise) {
        // Product 생성
        ProductDTO.ProductInfo productInfo = req1;
        ProductDTO.Questions questions = req2;

        Product newProduct = Product.builder()
                .name(productInfo.getProductName())
                .contents(productInfo.getContents())
                .reqTickets(productInfo.getReqTickets())
                .notes(productInfo.getNotes())
                .category(productInfo.getCategory())
                .launchedDate(productInfo.getLaunchedDate())
                .question1(questions.getQuestion1())
                .question2(questions.getQuestion2())
                .question3(questions.getQuestion3())
                .question4(questions.getQuestion4())
                .question5(questions.getQuestion5())
                .enterprise(enterprise)
                .build();

        return newProduct;
    }
}
