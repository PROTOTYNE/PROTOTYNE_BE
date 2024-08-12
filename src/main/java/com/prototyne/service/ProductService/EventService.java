package com.prototyne.service.ProductService;

import com.prototyne.domain.enums.ProductCategory;
import com.prototyne.web.dto.ProductDTO;

import java.util.List;

public interface EventService {
    List<ProductDTO.EventResponse> getEventsByType(String type);
    List<ProductDTO.SearchResponse> getEventsBySearch(String name);
    List<ProductDTO.SearchResponse> getEventsByCategory(String category);
}
