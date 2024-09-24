package com.prototyne.Enterprise.service.MyInfoService;

import com.prototyne.Enterprise.converter.MyInfoConverter;
import com.prototyne.Enterprise.web.dto.MyInfoDTO;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.domain.Enterprise;
import com.prototyne.repository.EnterpriseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyInfoServiceImpl implements MyInfoService{
    private final EnterpriseRepository enterpriseRepository;
    private final JwtManager jwtManager;

    @Override
    public MyInfoDTO getMyInfo(String accessToken) throws UserPrincipalNotFoundException {
        Long enterpriseId = jwtManager.validateJwt(accessToken);
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(()->new UserPrincipalNotFoundException(enterpriseId+"에 해당하는 기업이 없습니다."));

        return MyInfoConverter.toMyInfoResponseDTO(enterprise);
    }
}
