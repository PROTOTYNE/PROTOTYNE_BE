package com.prototyne.converter;

import com.prototyne.domain.Event;
import com.prototyne.domain.Investment;
import com.prototyne.domain.Product;
import com.prototyne.web.dto.MyProductDto;
import org.springframework.stereotype.Component;


@Component
public class MyProductConverter {

    public MyProductDto toResponseDto(Investment investment) {
        Event event = investment.getEvent();
        Product product = event.getProduct();

        return MyProductDto.builder()
                .investmentId(investment.getId())
                .eventId(investment.getEvent().getId())
                .productId(investment.getEvent().getProduct().getId())
                .name(product.getName())
                .thumbnailUrl(product.getThumbnailUrl())
                .status(investment.getStatus())
                .createdAt(investment.getCreatedAt())
                .build();
    }
}
