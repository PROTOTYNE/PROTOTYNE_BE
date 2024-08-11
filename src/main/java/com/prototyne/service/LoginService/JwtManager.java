package com.prototyne.service.LoginService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtManager {
    private final SecretKey key = Jwts.SIG.HS256.key().build();

    public String createJwt(Long id) {
        return Jwts.builder()
                .subject(String.valueOf(id))
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + 3600000))
                .signWith(key)
                .compact();
    }

    public Long validateJwt(String token) {
        try{
            Claims claims = Jwts
                    .parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Long.parseLong(claims.getSubject());
        }
        catch (Exception e) {
            throw new TempHandler(ErrorStatus.TOKEN_UNVALID);
        }
    }

    public String getToken(HttpServletRequest token) {
        if (token.getHeader("Authorization") == null) {
            throw new TempHandler(ErrorStatus.TOKEN_NOT_FOUND);
        }
        return token.getHeader("Authorization").replace("Bearer ", "");
    }
}