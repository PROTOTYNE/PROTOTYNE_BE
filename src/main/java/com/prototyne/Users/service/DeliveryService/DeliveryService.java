package com.prototyne.Users.service.DeliveryService;

import com.prototyne.Users.web.dto.DeliveryDto;

import java.util.List;

public interface DeliveryService {

    DeliveryDto.deliveryInfoResponse addNewDeliveryAddress(String accessToken, DeliveryDto req);
    List<DeliveryDto.deliveryInfoResponse> getMyDeliveryList(String accessToken);
    DeliveryDto.deliveryInfoResponse getOneDeliveryInfo(String accessToken, Long deliveryId);
    DeliveryDto.deliveryInfoResponse updateDeliveryInfo(String accessToken, Long deliveryId, DeliveryDto req);
    List<DeliveryDto.deliveryInfoResponse> deleteDeliveryAddr(String accessToken, Long deliveryId);
}
