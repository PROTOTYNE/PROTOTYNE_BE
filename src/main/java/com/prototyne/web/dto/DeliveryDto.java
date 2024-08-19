package com.prototyne.web.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    @Size(min = 2, max = 20)
    private String deliveryName;
    @Size(min = 5, max = 20)
    private String deliveryPhone;
    @Size(min = 5, max = 100)
    private String baseAddress;
    @Size(min = 5, max = 100)
    private String detailAddress;
}
