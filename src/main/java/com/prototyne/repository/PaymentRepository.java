package com.prototyne.repository;

import com.prototyne.domain.Payment;
import com.prototyne.domain.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByUserIdAndTid(Long userId, String tid);

    List<Payment> findAllByStatusIn(List<PaymentStatus> statuses);

}
