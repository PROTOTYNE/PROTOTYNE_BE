package com.prototyne.service.UserService;

import com.prototyne.converter.UserConverter;
import com.prototyne.domain.User;
import com.prototyne.domain.mapping.Additional;
import com.prototyne.repository.AdditionalRepository;
import com.prototyne.repository.UserRepository;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.web.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailService {

    private final UserRepository userRepository;
    private final AdditionalRepository additionalRepository;
    private final JwtManager jwtManager;
    private final UserConverter userConverter;


    @Override
    public UserDto.UserDetailResponse getUserDetail(String accessToken) throws UserPrincipalNotFoundException {
        Long userId = jwtManager.validateJwt(accessToken);

        // 추출한 사용자 ID를 사용하여 사용자 정보를 조회합니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserPrincipalNotFoundException(userId + "에 해당하는 회원이 없습니다."));

        List<Additional> additionalInfo = (List<Additional>) additionalRepository.findByUserId(userId);
        if(additionalInfo.isEmpty()){
            throw new UserPrincipalNotFoundException(userId + "에 해당하는 추가 정보가 없습니다.");
        }

        UserDto.AddInfo convertedAddInfo = convertToAddInfo(additionalInfo);

        return userConverter.toUserDetailResponse(user, convertedAddInfo);

    }

    private UserDto.AddInfo convertToAddInfo(List<Additional> additionalInfo) {
        UserDto.AddInfo addInfo = new UserDto.AddInfo();
        for (Additional additional : additionalInfo) {
            switch (additional.getAddSet().getTitle()) {
                case 직업:
                    addInfo.setOccupation(additional.getAddSet().getValue());
                    break;
                case 소득수준:
                    addInfo.setIncome(Integer.parseInt(additional.getAddSet().getValue()));
                    break;
                case 관심사:
                    addInfo.setInterests(Arrays.asList(additional.getAddSet().getValue().split(",")));
                    break;
                case 가족구성:
                    addInfo.setFamilyComposition(additional.getAddSet().getValue());
                    break;
                case 관심제품유형:
                    addInfo.setProductTypes(Arrays.asList(additional.getAddSet().getValue().split(",")));
                    break;
                case 스마트기기_기종:
                    addInfo.setSmartDevices(Arrays.asList(additional.getAddSet().getValue().split(",")));
                    break;
                case 건강상태:
                    addInfo.setHealthStatus(Integer.parseInt(additional.getAddSet().getValue()));
                    break;
            }
        }
        return addInfo;
    }


}
