package com.prototyne.service.LoginService;

import com.prototyne.web.dto.LoginDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoServiceImpl {

//    private String clientId;
//    //    private String KAUTH_USER_URL_HOST;
//
//    @Autowired
//    public void KakaoService(@Value("d6a11050f565f605ac69c90bc3f26ec8") String clientId) {
//        this.clientId = clientId;
////        KAUTH_TOKEN_URL_HOST ="https://kauth.kakao.com";
////        KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
//    }

    public LoginDto.KakaoTokenResponse getAccessToken(String code) {

        String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
        LoginDto.KakaoTokenResponse kakaoTokenResponseDto = (LoginDto.KakaoTokenResponse) WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", "d6a11050f565f605ac69c90bc3f26ec8")
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .bodyToMono(LoginDto.KakaoTokenResponse.class)
                .block();

        log.info(" [Kakao Service] Access Token ------> {}", Objects.requireNonNull(kakaoTokenResponseDto).getAccessToken());
        log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());

        return kakaoTokenResponseDto;
    }
    public LoginDto.UserInfoResponse getUserInfo(String accessToken) {

        LoginDto.UserInfoResponse userInfo = WebClient.create("https://kapi.kakao.com")
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .bodyToMono(LoginDto.UserInfoResponse.class)
                .block();

        log.info("[ Kakao Service ] Auth ID ---> {} ", Objects.requireNonNull(userInfo).getId());
        log.info("[ Kakao Service ] NickName ---> {} ", userInfo.getKakaoAccount().getProfile().getNickName());
        log.info("[ Kakao Service ] ProfileImageUrl ---> {} ", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());

        return userInfo;
    }

}


