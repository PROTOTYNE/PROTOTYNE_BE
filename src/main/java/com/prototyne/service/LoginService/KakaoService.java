package com.prototyne.service.LoginService;

import com.prototyne.web.dto.LoginDto;

public interface KakaoService {
    LoginDto.KakaoTokenResponse getAccessToken(String code);
    LoginDto.UserInfoResponse getUserInfo(String accessToken);
}
