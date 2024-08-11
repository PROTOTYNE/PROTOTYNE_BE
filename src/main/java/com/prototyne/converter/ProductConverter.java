package com.prototyne.converter;

import com.prototyne.domain.Event;
import com.prototyne.domain.Product;
import com.prototyne.web.dto.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {

    // 체험 진행 중인 시제품 목록 형식
    public static ProductDTO.EventResponse toEvent(Event event, Product product, int investCount) {
        return ProductDTO.EventResponse.builder()
                .id(event.getId())
                .name(product.getName())
                .thumbnailUrl(product.getThumbnailUrl())
                .investCount(investCount)
                .reqTickets(product.getReqTickets())
                .build();
    }
}