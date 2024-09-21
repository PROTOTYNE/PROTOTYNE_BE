package com.prototyne.Enterprise.web.dto;

import lombok.Builder;
import lombok.Getter;


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
}
