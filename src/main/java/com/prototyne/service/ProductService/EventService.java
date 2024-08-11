package com.prototyne.service.ProductService;

import com.prototyne.domain.enums.ProductCategory;
import com.prototyne.web.dto.ProductDTO;

import java.util.List;

public interface EventService {
    List<ProductDTO.EventResponse> getEventsByType(String type);
//    List<ProductDTO.EventResponse> getEventsBySearch(String type);
    List<ProductDTO.SearchResponse> getEventsByCategory(ProductCategory category);
}
