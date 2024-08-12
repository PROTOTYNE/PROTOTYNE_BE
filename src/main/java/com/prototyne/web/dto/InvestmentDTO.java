package com.prototyne.web.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

public class InvestmentDTO {
    @Builder
    @Getter
    public static class ApplicationDTO {

        private Long UserId; //User인지 Invest인지..

        private Integer tickets;

        // DeliveryDTO의 필드를 직접 추가
        @Size(min = 2, max = 20)
        private String deliveryName;

        @Size(min = 5, max = 20)
        private String deliveryPhone;

        @Size(min = 5, max = 100)
        private String deliveryAddress;

        //private DeliveryDto deliveryInfo;
        //DeliveryDto객체를 직접 받아오는 경우
    }
}
