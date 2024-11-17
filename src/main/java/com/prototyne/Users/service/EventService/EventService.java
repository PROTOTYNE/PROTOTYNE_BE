package com.prototyne.Users.service.EventService;

import com.prototyne.domain.Investment;
import com.prototyne.Users.web.dto.ProductDTO;

import java.util.List;

public interface EventService {
    ProductDTO.HomeResponse getHomeById(String accessToken);
    List<ProductDTO.EventDTO> getEventsByType(Long userId, String type, String cursor, Integer pageSize);
    String getNextCursor(List<ProductDTO.EventDTO> events, String type);
    List<ProductDTO.SearchResponse> getEventsBySearch(String accessToken, String name);
    List<ProductDTO.SearchResponse> getEventsByCategory(String accessToken, String category);
    ProductDTO.EventDetailsResponse getEventDetailsById(String accessToken, Long eventId);

    List<String> getRecentSearches(String accessToken);
    List<String> deleteSearchHistory(String searchTerm, String accessToken);
    List<String> deleteAllSearchHistory(String accessToken);
    public void saveInvestment(Investment investment);

}
