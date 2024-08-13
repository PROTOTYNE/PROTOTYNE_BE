package com.prototyne.repository;

import com.prototyne.domain.User;
import com.prototyne.domain.Event;
import com.prototyne.domain.mapping.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    List<Heart> findByUser(User user); // 사용자가 누른 좋아요 목록 조회
    Optional<Heart> findByUserAndEvent(User user, Event event);
}
