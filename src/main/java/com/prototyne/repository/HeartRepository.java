package com.prototyne.repository;

import com.prototyne.domain.mapping.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    // 특정 이벤트의 하트 수 카운트
    Integer countHeartsByEventId(@Param("eventId") Long eventId);
}

