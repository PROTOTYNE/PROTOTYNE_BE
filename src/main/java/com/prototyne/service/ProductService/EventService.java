package com.prototyne.service.ProductService;

import com.prototyne.web.dto.ProductDTO;

import java.util.List;

public interface EventService {
    ProductDTO.HomeResponse getHomeById(String accessToken);
    List<ProductDTO.EventResponse> getEventsByType(Long userId, String type);
    List<ProductDTO.SearchResponse> getEventsBySearch(String name);
    List<ProductDTO.SearchResponse> getEventsByCategory(String category);
    ProductDTO.EventDetailsResponse getEventDetailsById(String accessToken, Long eventId);
}
