package com.prototyne.repository;

import com.prototyne.domain.mapping.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
}
