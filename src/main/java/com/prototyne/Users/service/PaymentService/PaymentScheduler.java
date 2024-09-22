package com.prototyne.Users.service.PaymentService;


import com.prototyne.domain.Payment;
import com.prototyne.domain.enums.PaymentStatus;
import com.prototyne.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentScheduler {
    private final PaymentRepository paymentRepository;

    @Scheduled(cron = "0 * * * * ?") // 1분마다 실행
    @Transactional
    public void deleteOngoingPayments(){
        List<Payment> nonSucceedPayments = paymentRepository.findAllByStatusIn(
                List.of(PaymentStatus.결제대기,
                        PaymentStatus.결제진행
                )
        );
        paymentRepository.deleteAll(nonSucceedPayments);
    }
}
