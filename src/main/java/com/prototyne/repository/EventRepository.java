package com.prototyne.repository;

import com.prototyne.domain.Event;

import com.prototyne.domain.enums.ProductCategory;
import com.prototyne.repository.querydsl.EventRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {

    // 검색(제품)명을 포함하는 이벤트
    List<Event> findAllByProductNameContaining(String name);

    // 카테고리에 따른 이벤트
    List<Event> findByProductCategory(ProductCategory category);

    // 기업 ID로 모든 이벤트 조회 (조인)
    @Query("SELECT e FROM Event e JOIN FETCH e.product p WHERE p.enterprise.id = :enterpriseId")
    List<Event> findByEnterpriseId(Long enterpriseId);
}
