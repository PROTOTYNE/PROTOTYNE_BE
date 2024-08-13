package com.prototyne.repository;

import com.prototyne.domain.Feedback;
import com.prototyne.domain.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Feedback findByInvestmentId(Long investmentId);
}
