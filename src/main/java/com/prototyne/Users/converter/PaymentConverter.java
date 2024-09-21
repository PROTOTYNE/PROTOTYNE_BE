package com.prototyne.Users.converter;

import com.prototyne.Users.web.dto.KakaoPayDto;
import com.prototyne.domain.Payment;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.PaymentStatus;
import com.prototyne.domain.enums.TicketOption;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class PaymentConverter {
    public Payment toEntity(User user, KakaoPayDto.KakaoPayRequest request){
        return Payment.builder()
                .partnerOrderId(request.getPartnerOrderId())
                .user(user)
                .quantity(request.getQuantity())
                .status(PaymentStatus.결제대기)
                .amount(request.getTotalAmount())
                .productName(request.getItemName())
                .ticketOption(TicketOption.valueOf(request.getTicketOption()))
                .build();
    }
}
