package com.prototyne.service.ApplicationService;

import com.prototyne.web.dto.InvestmentDTO;
import com.prototyne.web.dto.TicketDto;

public interface ApplicationService {
    InvestmentDTO.ApplicationResponse Application(String accessToken,Long investmentId);
}
