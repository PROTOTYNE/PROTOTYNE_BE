package com.prototyne.converter;

import com.prototyne.domain.Event;
import com.prototyne.domain.Product;
import com.prototyne.domain.User;
import com.prototyne.domain.mapping.Heart;
import com.prototyne.repository.HeartRepository;
import com.prototyne.web.dto.HeartDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HeartConverter {
    public static HeartDto.HeartResponseDTO toHeartResponseDTO(User user, List<Heart> hearts, HeartRepository heartRepository) {
        List<HeartDto.HeartResponseDTO.ProductInfo> productInfos = hearts.stream()
                .map(heart -> {
                    Event event = heart.getEvent();
                    Product product = event.getProduct();

                    Long count = heartRepository.countByProductId(product.getId());

                    return HeartDto.HeartResponseDTO.ProductInfo.builder()
                            .productId(product.getId())
                            .eventId(event.getId())
                            .name(product.getName())
                            .reqTickets(product.getReqTickets())
                            .thumbnailUrl(product.getThumbnailUrl())
                            .count(count)
                            .build();
                })
                .collect(Collectors.toList());

        return HeartDto.HeartResponseDTO.builder()
                .userId(user.getId())
                .products(productInfos)
                .build();
    }

    public static HeartDto.HeartActionResponseDTO toHeartActionResponseDTO(Long eventId, String message) {
        return HeartDto.HeartActionResponseDTO.builder()
                .eventId(eventId)
                .message(message)
                .build();
    }

    public static Heart toHeartEntity(User user, Event event) {
        return Heart.builder()
                .user(user)
                .event(event)
                .build();
    }
}
