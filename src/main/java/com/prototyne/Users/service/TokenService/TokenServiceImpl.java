package com.prototyne.Users.service.TokenService;

import com.prototyne.Users.web.dto.JwtTokenDto;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.RefreshToken;
import com.prototyne.repository.RefreshTokenRepository;
import com.prototyne.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public JwtTokenDto refreshAccessToken(String accessToken){
        Long userId = jwtUtil.validateRefreshToken(accessToken);

        String storedRefreshToken = refreshTokenRepository.findById(userId)
                .map(RefreshToken::getRefreshToken)
                .orElseThrow(() -> new TempHandler(ErrorStatus.TOKEN_UNVALID));

        if (!storedRefreshToken.equals(accessToken)) {
            throw new TempHandler(ErrorStatus.TOKEN_UNVALID);
        }

        // 새로운 Access Token 및 Refresh Token 생성
        String newAccessToken = jwtUtil.createAccessToken(userId);
        String newRefreshToken = jwtUtil.createRefreshToken(userId);

        // 새로운 리프레시 토큰 Redis에 저장
        saveRefreshToken(userId, newRefreshToken);

        return new JwtTokenDto(newAccessToken, newRefreshToken);
    }

    @Override
    public void saveRefreshToken(Long userId, String refreshToken){
        RefreshToken token = new RefreshToken(userId, refreshToken);
        refreshTokenRepository.save(token);
    }

    @Override
    public void deleteRefreshToken(Long userId){
        refreshTokenRepository.deleteById(userId);
    }

}
