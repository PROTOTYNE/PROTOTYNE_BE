package com.prototyne.Enterprise.service.TokenService;

import com.prototyne.Enterprise.web.dto.LoginDto;
import com.prototyne.Users.web.dto.JwtTokenDto;

public interface EnterpriseTokenService {
        JwtTokenDto refreshAccessToken(String accessToken);

        void saveRefreshToken(Long enterpriseId, String refreshToken);

        LoginDto.EnterpriseLogoutResponse deleteRefreshToken(Long enterpriseId);
}
