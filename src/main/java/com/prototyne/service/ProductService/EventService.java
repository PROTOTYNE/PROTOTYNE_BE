package com.prototyne.service.ProductService;

import com.prototyne.web.dto.ProductDTO;

import java.util.List;

public interface EventService {
    List<ProductDTO.EventResponse> getEventsByType(String type);
    List<ProductDTO.SearchResponse> getEventsBySearch(String name, String accessToken);
    List<ProductDTO.SearchResponse> getEventsByCategory(String category);
    ProductDTO.EventDetailsResponse getEventDetailsById(String accessToken, Long eventId);
    List<String> getRecentSearches(String accessToken);
    List<String> deleteSearchHistory(String searchTerm, String accessToken);
    List<String> deleteAllSearchHistory(String accessToken);
}
