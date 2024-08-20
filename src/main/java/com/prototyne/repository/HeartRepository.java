package com.prototyne.repository;

import com.prototyne.domain.User;
import com.prototyne.domain.Event;
import com.prototyne.domain.mapping.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    List<Heart> findByUser(User user); // 사용자가 누른 좋아요 목록 조회
    Optional<Heart> findByUserAndEvent(User user, Event event);
    Optional<Heart> findFirstByUserIdAndEvent(Long userId, Event event);
    @Query("SELECT COUNT(h) FROM Heart h WHERE h.event.product.id = :productId")
    Long countByProductId(@Param("productId") Long productId);
}
