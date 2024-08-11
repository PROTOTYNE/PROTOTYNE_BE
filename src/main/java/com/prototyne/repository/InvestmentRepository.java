package com.prototyne.repository;

import com.prototyne.domain.Investment;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    // 특정 이벤트의 투자 수 카운트
    Integer countInvestmentByEventId(@Param("eventId") Long eventId);
}