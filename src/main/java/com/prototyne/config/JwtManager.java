package com.prototyne.config;

import com.prototyne.Users.service.TokenService.TokenService;
import com.prototyne.Users.web.dto.JwtTokenDto;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.utils.JwtUtil;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtManager {
    private final SecretKey key = Jwts.SIG.HS256.key().build();
    private final JwtUtil jwtUtil;
    @Lazy
    private final TokenService tokenService;

    // Access Token 및 Refresh Token 생성 로직은 JwtUtil을 사용
    public String createAccessToken(Long id) {
        return jwtUtil.createAccessToken(id);
    }

    public String createRefreshToken(Long id, boolean isEnterprise) {
        return jwtUtil.createRefreshToken(id, isEnterprise);
    }

    // JWT 검증 로직도 JwtUtil을 사용
    public Long validateAccessToken(String token) {
        return jwtUtil.validateAccessToken(token);
    }

    public Long validateRefreshToken(String token) {
        return jwtUtil.validateRefreshToken(token);
    }
    public Long validateJwt(String token) {
        Long ownerId;
        try {
            // 1. 액세스 토큰 검증
            ownerId = validateAccessToken(token);
        } catch (TempHandler e) {
            // 2. 예외가 발생했을 때, 예외 코드로 구분하여 처리
            if ("TOKEN4002".equals(e.getCode())) {  // TOKEN_EXPIRED 코드 체크
                // 3. 리프레시 토큰으로 새로운 액세스 토큰 발급
                JwtTokenDto newTokens = tokenService.refreshAccessToken(token);
                ownerId = validateAccessToken(newTokens.getAccessToken());
            } else {
                throw e; // 4. 다른 예외는 그대로 던짐
            }
        }
        return ownerId;
    }

    public String getToken(HttpServletRequest token) {
        if (token.getHeader("Authorization") == null) {
            throw new TempHandler(ErrorStatus.TOKEN_NOT_FOUND);
        }
        return token.getHeader("Authorization").replace("Bearer ", "");
    }
}