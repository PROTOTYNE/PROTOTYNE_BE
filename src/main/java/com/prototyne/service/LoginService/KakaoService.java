package com.prototyne.service.LoginService;

import com.prototyne.web.dto.UserDto;

public interface KakaoService {
    UserDto.KakaoTokenResponse getAccessToken(String code);

    UserDto.UserInfoResponse getKakaoInfo(String accessToken);

    UserDto.UserRequest getUserInfo(String accessToken);
}