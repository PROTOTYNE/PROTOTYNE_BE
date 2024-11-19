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

    // 인기순의 체험 진행 중인 이벤트
    // 인기순 : 북마크 수 + 투자 수 평균
    @Query("SELECT e FROM Event e " +
            "LEFT JOIN e.heartList h " +
            "LEFT JOIN e.investmentList i " +
            "WHERE e.eventStart <= :now AND e.eventEnd >= :now " +
            "GROUP BY e " +
            "ORDER BY (COUNT(h) + COUNT(i)) / 2 DESC")
    List<Event> findAllActiveEventsByPopular(@Param("now") LocalDate now);

    // 마감 임박순의 체험 진행 중인 이벤트
    @Query("SELECT e FROM Event e " +
            "WHERE e.eventStart <= :now AND e.eventEnd >= :now " +
            "ORDER BY e.eventEnd ASC")
    List<Event> findAllEventsByImminent(@Param("now") LocalDate now);

    // 신규 등록순의 체험 진행 중인 이벤트
    @Query("SELECT e FROM Event e " +
            "WHERE e.eventStart <= :now AND e.eventEnd >= :now " +
            "ORDER BY e.eventStart DESC")
    List<Event> findAllEventsByNew(@Param("now") LocalDate now);

    // 검색(제품)명을 포함하는 이벤트
    List<Event> findAllByProductNameContaining(String name);

    // 카테고리에 따른 이벤트
    // List<Event> findByProductCategory(ProductCategory category);
    List<Event> findByCategory(ProductCategory category);

    // 기업 ID로 모든 이벤트 조회 (조인)
    @Query("SELECT e FROM Event e JOIN FETCH e.product p WHERE p.enterprise.id = :enterpriseId")
    List<Event> findByEnterpriseId(Long enterpriseId);

}
