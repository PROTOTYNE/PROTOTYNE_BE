package com.prototyne.service.SignupService;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.converter.UserConverter;
import com.prototyne.domain.ADD_set;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.AddsetTitle;
import com.prototyne.domain.mapping.Additional;
import com.prototyne.repository.AdditionalRepository;
import com.prototyne.repository.UserRepository;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.service.LoginService.KakaoService;
import com.prototyne.service.LoginService.KakaoServiceImpl;
import com.prototyne.web.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserSignupServiceImpl implements UserSignupService {
    private final UserRepository userRepository;
    private final AdditionalRepository additionalRepository;
    @Lazy
    private final KakaoServiceImpl kakaoService;
    private final JwtManager jwtManager;
    @Override
    public User signup(String aouthToken,
                       UserDto.UserDetailRequest userDetailRequest,
                       UserDto.UserAddInfoRequest addInfoRequest){
        UserDto.UserInfoResponse kakaoUserInfo = kakaoService.getKakaoInfo(aouthToken);
        // 카카오 이메일로 기존 회원이 존재하는지 여부 조회
        User existingUser = userRepository.findByEmail(kakaoUserInfo.getKakaoAccount().getEmail());

        if(existingUser != null) {
            throw new RuntimeException("해당 이메일로 이미 가입한 회원이 존재합니다.");
        }

        User newUser = UserConverter.toSignedUser(kakaoUserInfo, userDetailRequest);
        userRepository.save(newUser);

        // 추가 정보 저장
        saveAdditionalInfo(newUser, addInfoRequest);
        return newUser;
    }

    @Override
    public void saveAddSetInfo(User user, AddsetTitle title, String value){
        if (value != null && !value.isEmpty()) {
            ADD_set addSet = ADD_set.builder()
                    .title(title)
                    .value(value)
                    .build();
            additionalRepository.save(new Additional(user, addSet));
        }
    }

    @Override
    public void saveAdditionalInfo(User user, UserDto.UserAddInfoRequest request) {
        saveAddSetInfo(user, AddsetTitle.직업, request.getOccupation());
        saveAddSetInfo(user, AddsetTitle.소득수준, String.valueOf(request.getIncome()));
        saveAddSetInfo(user, AddsetTitle.관심사, String.join(",", request.getInterests()));
        saveAddSetInfo(user, AddsetTitle.가족구성, request.getFamilyComposition());
        saveAddSetInfo(user, AddsetTitle.관심제품유형, String.join(",", request.getProductTypes()));
        saveAddSetInfo(user, AddsetTitle.스마트기기_기종, String.join(",", request.getSmartDevices()));
        saveAddSetInfo(user, AddsetTitle.건강상태, String.valueOf(request.getHealthStatus()));
    }

}
