package com.prototyne.service.LoginService;

import org.springframework.beans.factory.annotation.Value;

public interface KakaoService {
    public String getAccessToken(String code);
}
