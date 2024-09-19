package com.prototyne.repository;

import com.prototyne.domain.Payment;
import com.prototyne.domain.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStatusAndCreatedAtBefore(PaymentStatus paymentStatus, LocalDateTime timeOutTime);
    Payment findByOrderId(String orderId);
}
