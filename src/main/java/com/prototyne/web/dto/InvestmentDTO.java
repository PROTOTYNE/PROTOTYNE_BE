package com.prototyne.web.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

public class InvestmentDTO {
    @Builder
    @Getter
    public static class ApplicationRequest {

        private Integer tickets;

        private Integer reqTickets;

        private DeliveryDto deliveryInfo;
        //DeliveryDto객체를 직접 받아오는 경우
    }

    @Builder
    @Getter
    public static class ApplicationResponse {

        private Boolean apply;
    }
}
