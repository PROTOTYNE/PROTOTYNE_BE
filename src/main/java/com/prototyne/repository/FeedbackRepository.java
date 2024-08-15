package com.prototyne.repository;

import com.prototyne.domain.Feedback;
import com.prototyne.domain.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Optional<Feedback> findByInvestmentId(Long investmentId);
}
