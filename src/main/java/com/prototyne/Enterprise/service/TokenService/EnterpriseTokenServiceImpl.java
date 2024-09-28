package com.prototyne.Enterprise.service.TokenService;

import com.prototyne.Enterprise.web.dto.LoginDto;
import com.prototyne.Users.web.dto.JwtTokenDto;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.RefreshToken;
import com.prototyne.repository.RefreshTokenRepository;
import com.prototyne.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EnterpriseTokenServiceImpl implements EnterpriseTokenService{
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public JwtTokenDto refreshAccessToken(String refreshToken){
        Long enterpriseId = jwtUtil.validateRefreshToken(refreshToken);

        String id = RefreshToken.generateId(enterpriseId, true);
        String storedRefreshToken = refreshTokenRepository.findById(id)
                .map(RefreshToken::getRefreshToken)
                .orElseThrow(() -> new TempHandler(ErrorStatus.TOKEN_UNVALID));

        if (!storedRefreshToken.equals(refreshToken)) {
            throw new TempHandler(ErrorStatus.TOKEN_UNVALID);
        }

        // 새로운 Access Token 및 Refresh Token 생성
        String newAccessToken = jwtUtil.createAccessToken(enterpriseId);
        String newRefreshToken = jwtUtil.createRefreshToken(enterpriseId, true);

        // 새로운 리프레시 토큰 Redis에 저장
        saveRefreshToken(enterpriseId, newRefreshToken);

        return new JwtTokenDto(newAccessToken, newRefreshToken);
    }

    public void saveRefreshToken(Long ownerId, String refreshToken){
        RefreshToken token = new RefreshToken(ownerId, refreshToken, true);
        refreshTokenRepository.save(token);
    }

    @Override
    public LoginDto.EnterpriseLogoutResponse deleteRefreshToken(Long userId){
        String id = RefreshToken.generateId(userId, true);
        RefreshToken refreshToken = refreshTokenRepository.findById(id)
                .orElseThrow(()-> new TempHandler(ErrorStatus.TOKEN_NOT_FOUND));
        refreshTokenRepository.deleteById(id);
        return new LoginDto.EnterpriseLogoutResponse(refreshToken.getOwnerId(), refreshToken.getRefreshToken());
    }
}
