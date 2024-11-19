package com.prototyne.Users.converter;

import com.prototyne.Users.web.dto.DeliveryDto;
import com.prototyne.domain.User;
import com.prototyne.domain.mapping.DeliveryAddress;

public class DeliveryConverter {
    public static DeliveryAddress toNewDeliveryInfo(User user, DeliveryDto req){
        return DeliveryAddress.builder()
                .user(user)
                .deliveryName(req.getDeliveryName())
                .deliveryPhone(req.getDeliveryPhone())
                .postCode(req.getPostCode())
                .baseAddress(req.getBaseAddress())
                .detailAddress(req.getDetailAddress())
                .isDefault(req.isDefault())
                .build();
    }

    public static DeliveryDto.deliveryInfoResponse toDeliveryInfo(DeliveryAddress delivery){
        return DeliveryDto.deliveryInfoResponse.builder()
                .id(delivery.getId())
                .isDefault(delivery.isDefault())
                .deliveryName(delivery.getDeliveryName())
                .deliveryPhone(delivery.getDeliveryPhone())
                .postCode(delivery.getPostCode())
                .baseAddress(delivery.getBaseAddress())
                .detailAddress(delivery.getDetailAddress())
                .build();
    }
}
