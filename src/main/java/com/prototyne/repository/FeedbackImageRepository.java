package com.prototyne.repository;

import com.prototyne.domain.FeedbackImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackImageRepository extends JpaRepository<FeedbackImage, Long> {
    FeedbackImage findByFeedbackId(Long feedbackId);
}
