package com.prototyne.Users.service.DeliveryService;

import com.prototyne.Users.web.dto.DeliveryDto;

public interface DeliveryService {
    DeliveryDto getDeliveryInfo(String accessToken);

    DeliveryDto patchDeliveryInfo(String accessToken, DeliveryDto deliveryDto);
}
