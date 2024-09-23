package com.prototyne.Enterprise.service.LoginService;

import com.prototyne.Enterprise.web.dto.LoginDto;

public interface LoginService {
    LoginDto.EnterpriseSignupResponse registerEnterprise(LoginDto.EnterpriseSignupRequest enterpriseSignupRequest);
    LoginDto.EnterpriseLoginResponse loginEnterprise(LoginDto.EnterpriseLoginRequest enterpriseLoginRequest);
}
