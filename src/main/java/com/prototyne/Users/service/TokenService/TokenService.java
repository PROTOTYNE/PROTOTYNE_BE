package com.prototyne.Users.service.TokenService;

import com.prototyne.Users.web.dto.JwtTokenDto;

public interface TokenService {

    JwtTokenDto refreshAccessToken(String accessToken);

    void saveRefreshToken(Long userId, String refreshToken);

    void deleteRefreshToken(Long userId);
}
