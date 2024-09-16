package com.prototyne.Enterprise.service.EnterpriseService;

import com.prototyne.Enterprise.web.dto.EnterpriseDto;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.Enterprise;
import com.prototyne.repository.EnterpriseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnterpriseServiceImpl implements EnterpriseService {
    private final EnterpriseRepository enterpriseRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtManager jwtManager;


    @Override
    public EnterpriseDto.EnterpriseSignupResponse registerEnterprise(EnterpriseDto.EnterpriseSignupRequest enterpriseSignupRequest) {
        // 중복 사용자 체크
        enterpriseRepository.findByUsername(enterpriseSignupRequest.getUsername())
                .ifPresent(enterprise -> {throw new TempHandler(ErrorStatus.SIGNUP_DUPLICATE);
                });

        Enterprise enterprise=Enterprise.builder()
                .username(enterpriseSignupRequest.getUsername())
                .password(passwordEncoder.encode(enterpriseSignupRequest.getPassword()))
                .name(enterpriseSignupRequest.getName())
                .regNumber(enterpriseSignupRequest.getRegNumber())
                .phone(enterpriseSignupRequest.getPhone())
                .email(enterpriseSignupRequest.getEmail())
                .address(enterpriseSignupRequest.getAddress())
                .category(enterpriseSignupRequest.getCategory())
                .size(enterpriseSignupRequest.getSize())
                .status(enterpriseSignupRequest.getStatus())
                .build();
        Enterprise savedEnterprise = enterpriseRepository.save(enterprise);

        return EnterpriseDto.EnterpriseSignupResponse.builder()
                .enterpriseId(savedEnterprise.getId())
                .msg("회원가입이 완료되었습니다.")
                .build();
    }

    @Override
    public EnterpriseDto.EnterpriseLoginResponse loginEnterprise(EnterpriseDto.EnterpriseLoginRequest enterpriseLoginRequest) {
        // 사용자 조회
        Enterprise enterprise = enterpriseRepository.findByUsername(enterpriseLoginRequest.getUsername())
                .orElseThrow(()->new TempHandler(ErrorStatus.LOGIN_ERROR_ID));
        // 비밀번호 확인
        if(!passwordEncoder.matches(enterpriseLoginRequest.getPassword(), enterprise.getPassword())){
            throw new TempHandler(ErrorStatus.LOGIN_ERROR_PW);
        }

        // JWT 토큰 생성
        String accessToken = jwtManager.createJwt(enterprise.getId());

        return EnterpriseDto.EnterpriseLoginResponse.builder()
                .id(enterprise.getId())
                .name(enterprise.getName())
                .access_token(accessToken)
                .build();
    }
}