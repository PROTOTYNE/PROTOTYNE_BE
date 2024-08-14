package com.prototyne.converter;

import com.prototyne.domain.Event;
import com.prototyne.domain.Investment;
import com.prototyne.domain.Product;
import com.prototyne.web.dto.MyProductDto;
import org.springframework.stereotype.Component;


@Component
public class MyProductConverter {

    public MyProductDto.CommonDto toCommonDto(Investment investment) {
        Event event = investment.getEvent();
        Product product = event.getProduct();

        return MyProductDto.CommonDto.builder()
                .investmentId(investment.getId())
                .eventId(investment.getEvent().getId())
                .productId(investment.getEvent().getProduct().getId())
                .name(product.getName())
                .thumbnailUrl(product.getThumbnailUrl())
                .status(investment.getStatus())
                .createdAt(investment.getCreatedAt())
                .build();
    }

    public MyProductDto.OngoingDto toOngoingDto(Investment investment) {
        MyProductDto.CommonDto commonInfo = toCommonDto(investment);

        return MyProductDto.OngoingDto.builder()
                .commonInfo(commonInfo)
                .shipping(investment.getShipping())
                .transportNum(investment.getTransportNum())
                .feedbackStart(investment.getEvent().getFeedbackStart())
                .feedbackEnd(investment.getEvent().getFeedbackEnd())
                .build();
    }
}
