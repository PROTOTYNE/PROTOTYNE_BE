package com.prototyne.Users.service.ApplicationService;

import com.prototyne.Users.web.dto.InvestmentDTO;

public interface ApplicationService {
    InvestmentDTO.ApplicationResponse Application(String accessToken, Long deliveryId, Long investmentId);
}
