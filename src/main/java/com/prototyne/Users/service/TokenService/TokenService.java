package com.prototyne.Users.service.TokenService;

import com.prototyne.Users.web.dto.JwtTokenDto;
import com.prototyne.Users.web.dto.UserDto;

public interface TokenService {

    JwtTokenDto refreshAccessToken(String refreshToken);

    void saveRefreshToken(Long userId, String refreshToken);

    UserDto.KakaoLogoutTokenResponse deleteRefreshToken(Long userId);
}
