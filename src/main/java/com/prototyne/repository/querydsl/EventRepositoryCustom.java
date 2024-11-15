package com.prototyne.repository.querydsl;

import com.prototyne.domain.Event;
import com.prototyne.domain.enums.ProductCategory;

import java.util.List;

public interface EventRepositoryCustom {
    List<Event> findByCategory(ProductCategory category);
}
