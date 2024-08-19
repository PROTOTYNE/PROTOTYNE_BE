package com.prototyne.converter;

import com.prototyne.domain.Event;
import com.prototyne.domain.Investment;
import com.prototyne.domain.User;
import com.prototyne.web.dto.InvestmentDTO;
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
                .build();
    }
}
