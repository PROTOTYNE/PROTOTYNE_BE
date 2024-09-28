package com.prototyne.Users.service.TokenService;

import com.prototyne.Users.web.dto.JwtTokenDto;
import com.prototyne.Users.web.dto.UserDto;
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
    public JwtTokenDto refreshAccessToken(String refreshToken){
        Long userId = jwtUtil.validateRefreshToken(refreshToken);

        String id = RefreshToken.generateId(userId, false);
        String storedRefreshToken = refreshTokenRepository.findById(id)
                .map(RefreshToken::getRefreshToken)
                .orElseThrow(() -> new TempHandler(ErrorStatus.TOKEN_UNVALID));

        if (!storedRefreshToken.equals(refreshToken)) {
            throw new TempHandler(ErrorStatus.TOKEN_UNVALID);
        }

        // 새로운 Access Token 및 Refresh Token 생성
        String newAccessToken = jwtUtil.createAccessToken(userId);
        String newRefreshToken = jwtUtil.createRefreshToken(userId, false);

        // 새로운 리프레시 토큰 Redis에 저장
        saveRefreshToken(userId, newRefreshToken);

        return new JwtTokenDto(newAccessToken, newRefreshToken);
    }

    @Override
    public void saveRefreshToken(Long ownerId, String refreshToken){
        RefreshToken token = new RefreshToken(ownerId, refreshToken, false);
        refreshTokenRepository.save(token);
    }

    @Override
    public UserDto.KakaoLogoutTokenResponse deleteRefreshToken(Long userId){
        String id = RefreshToken.generateId(userId, false);
        RefreshToken refreshToken = refreshTokenRepository.findById(id)
                .orElseThrow(()-> new TempHandler(ErrorStatus.TOKEN_NOT_FOUND));
        refreshTokenRepository.deleteById(id);
        return new UserDto.KakaoLogoutTokenResponse(refreshToken.getRefreshToken(), userId);
    }

}
