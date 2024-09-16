package com.prototyne.Enterprise.service.EnterpriseService;

import com.prototyne.Enterprise.web.dto.EnterpriseDto;

public interface EnterpriseService {
    EnterpriseDto.EnterpriseSignupResponse registerEnterprise(EnterpriseDto.EnterpriseSignupRequest enterpriseSignupRequest);
    EnterpriseDto.EnterpriseLoginResponse loginEnterprise(EnterpriseDto.EnterpriseLoginRequest enterpriseLoginRequest);
}
