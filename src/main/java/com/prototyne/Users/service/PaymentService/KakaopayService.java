package com.prototyne.Users.service.PaymentService;

import com.prototyne.Users.web.dto.KakaoPayDto;
import com.prototyne.domain.enums.TicketOption;

public interface KakaopayService {

    KakaoPayDto.KakaoPayReadyResponse readyToPay(String accessToken, TicketOption ticketOption);
}
