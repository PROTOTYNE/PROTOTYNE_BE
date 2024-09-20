package com.prototyne.Enterprise.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class UserlistDTO {

    @Getter
    @Builder
    public static class UserListResponse {

        private String userName;

        private LocalDateTime event_start;

        private String prizeStatus;

        private String deliveryStatus;

        private String reviewStatus;

        private String addInfo;
    }
}
