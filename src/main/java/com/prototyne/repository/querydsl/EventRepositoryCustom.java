package com.prototyne.repository.querydsl;

import com.prototyne.domain.Event;
import com.prototyne.domain.enums.ProductCategory;

import java.time.LocalDate;
import java.util.List;

public interface EventRepositoryCustom {
    // 카테고리별 최신순 정렬 (기본)
    List<Event> findByCategoryNew(ProductCategory category, LocalDate now);
    // 카테고리별 인기순 정렬
    List<Event> findByCategoryPopular(ProductCategory category, LocalDate now);
    // 카테고리별 가격 낮은순 정렬
    List<Event> findByCategoryLowPrice(ProductCategory category, LocalDate now);
    // 카테고리별 가격 높은순 정렬
    List<Event> findByCategoryHighPrice(ProductCategory category, LocalDate now);
}
