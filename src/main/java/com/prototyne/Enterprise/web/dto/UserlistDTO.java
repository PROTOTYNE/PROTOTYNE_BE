package com.prototyne.Enterprise.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class UserlistDTO {

    @Getter
    @Builder
    public static class UserListResponse {

        private String userName;

        private String event_start;

        private String prizeStatus;

        private String deliveryStatus;

        private String reviewStatus;

        private String addInfo;
    }

    @Getter
    @Setter
    public static class DeliveryRequest {
        private Boolean isDelivery;

        private String transportNum;
    }
}
