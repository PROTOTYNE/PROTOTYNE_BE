package com.prototyne.Users.web.dto;

import jakarta.validation.constraints.Size;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    @Size(min = 2, max = 20)
    private String deliveryName;
    @Size(min = 5, max = 20)
    private String deliveryPhone;
    @Size(min = 2, max = 20)
    private String postCode;
    @Size(min = 5, max = 100)
    private String baseAddress;
    @Size(min = 5, max = 100)
    private String detailAddress;
    private boolean isDefault; //기본 배송지 여부

    @Getter
    @Builder
    @AllArgsConstructor
    public static class deliveryInfoResponse {
        private Long id;
        private boolean isDefault;      // 기본 배송지 여부
        private String deliveryName;
        private String deliveryPhone;
        private String postCode;
        private String baseAddress;
        private String detailAddress;
    }
}
