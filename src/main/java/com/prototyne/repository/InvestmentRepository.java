package com.prototyne.repository;

import com.prototyne.domain.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    Optional<Investment> findByUserIdAndEventId(Long userId, Long eventId);


}

