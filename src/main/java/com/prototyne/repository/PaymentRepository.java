package com.prototyne.repository;

import com.prototyne.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByUserIdAndTid(Long userId, String tid);
}
