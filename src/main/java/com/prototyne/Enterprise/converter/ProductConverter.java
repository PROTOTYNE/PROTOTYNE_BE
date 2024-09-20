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
                .build();
    }

    // 시제품 엔티티 형식으로
    public static Product toProduct(ProductDTO.CreateProductRequest request,
                                    Enterprise enterprise,
                                    List<String> imageUrls) {
        // Product 생성
        Product newProduct = Product.builder()
                .name(request.getProductName())
                .contents(request.getContents())
                .reqTickets(request.getReqTickets())
                .notes(request.getNotes())
                .category(request.getCategory())
                .question1(request.getQuestions().getQuestion1())
                .question2(request.getQuestions().getQuestion2())
                .question3(request.getQuestions().getQuestion3())
                .question4(request.getQuestions().getQuestion4())
                .question5(request.getQuestions().getQuestion5())
                .enterprise(enterprise)
                .build();

        // 이미지 업로드한 경우에만 ProductImage 생성
        if (imageUrls != null && !imageUrls.isEmpty()) {
            // imageList 초기화 후, imageUrlsList와 연결
            if (newProduct.getProductImageList() == null) {
                newProduct.setProductImageList(new ArrayList<>());
            }

            List<ProductImage> productImages = imageUrls.stream()
                    .map(url -> ProductImage.builder()
                            .imageUrl(url)
                            .product(newProduct)
                            .build())
                    .toList();

            newProduct.getProductImageList().addAll(productImages);

            // 첫번째 이미지로 썸네일 설정
            newProduct.setThumbnailUrl(productImages.get(0).getImageUrl());
        }

        return newProduct;
    }
}
