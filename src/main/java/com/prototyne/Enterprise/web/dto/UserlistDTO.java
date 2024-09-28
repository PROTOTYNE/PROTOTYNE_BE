package com.prototyne.Enterprise.web.dto;

import com.prototyne.domain.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


public class UserlistDTO {

    @Getter
    @Builder
    public static class UserListResponse {

        private Long userId;

        private String userName;

        private String event_start;

        private String prizeStatus;

        private Boolean deliveryStatus;

        private String reviewStatus;

        //private String addInfo;

        private LocalDate birth;

        private Integer familyMember;

        private Gender gender;

    }

    @Getter
    @Setter
    public static class DeliveryRequest {
        private Boolean isDelivery;

        private String transportNum;
    }
}
