package com.prototyne.service.ProductService;

import com.prototyne.web.dto.ProductDTO;

import java.util.List;

public interface EventService {
    List<ProductDTO.EventResponse> getEventsByType(String type);
}
