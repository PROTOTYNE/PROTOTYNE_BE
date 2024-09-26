package com.prototyne.utils;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.RefreshToken;
import com.prototyne.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final SecretKey key = Jwts.SIG.HS256.key().build();
    private final RefreshTokenRepository refreshTokenRepository;


    private static final long ACCESS_TOKEN_EXPIRATION = 3600000;
    private static final long REFRESH_TOKEN_EXPIRATION = 604800000;

    public String createAccessToken(Long id) {
        return Jwts.builder()
                .subject(String.valueOf(id))
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + ACCESS_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long id){
        String refreshToken =  Jwts.builder()
                .subject(String.valueOf(id))
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
        RefreshToken token = new RefreshToken(id, refreshToken);
        refreshTokenRepository.save(token);
        return refreshToken;
    }

    public Long validateToken(String token) {
        try{
            Claims claims = Jwts
                    .parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Long.parseLong(claims.getSubject());
        }
        catch(ExpiredJwtException e){
            throw new TempHandler(ErrorStatus.TOKEN_EXPIRED);
        }
        catch (JwtException e) {
            throw new TempHandler(ErrorStatus.TOKEN_UNVALID);
        }
        catch (Exception e) {
            throw new TempHandler(ErrorStatus.TOKEN_UNKNOWN_ERROR);
        }
    }

    public Long validateAccessToken(String token){
        return validateToken(token);
    }

    public Long validateRefreshToken(String token){
        return validateToken(token);
    }
}
