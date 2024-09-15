package com.prototyne.Users.converter;

import com.prototyne.domain.Event;
import com.prototyne.domain.Investment;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.InvestmentStatus;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class InvestmentConverter {
    public Investment toInvestment(User user, Event event, Boolean apply)
    {
        return Investment.builder()
                .user(user)
                .event(event)
                .apply(apply)
                .status(InvestmentStatus.신청)
                .build();
    }
}
