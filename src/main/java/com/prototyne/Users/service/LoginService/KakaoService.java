package com.prototyne.Users.service.LoginService;

import com.prototyne.Users.web.dto.UserDto;

public interface KakaoService {
    UserDto.KakaoTokenResponse getAccessToken(String code);

    UserDto.UserInfoResponse getKakaoInfo(String accessToken);

    UserDto.UserRequest getUserInfo(String accessToken);
}