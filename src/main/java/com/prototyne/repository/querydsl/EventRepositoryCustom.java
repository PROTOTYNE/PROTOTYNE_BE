package com.prototyne.repository.querydsl;

import com.prototyne.domain.Event;
import com.prototyne.domain.enums.ProductCategory;

import java.util.List;

public interface EventRepositoryCustom {
    List<Event> findByCategory(ProductCategory category);
    List<Event> findAllEventsByLimit(String type, int limit);
    List<Event> findAllEventsByType(String type, String cursor, int pageSize);
}
