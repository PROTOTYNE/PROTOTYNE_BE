package com.prototyne.service.SignupService;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.converter.UserConverter;
import com.prototyne.domain.ADD_set;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.AddsetTitle;
import com.prototyne.domain.mapping.Additional;
import com.prototyne.repository.ADD_setRepository;
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

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserSignupServiceImpl implements UserSignupService {
    private final UserRepository userRepository;
    private final AdditionalRepository additionalRepository;
    private final ADD_setRepository addSetRepository;
    @Lazy
    private final KakaoServiceImpl kakaoService;
    private final JwtManager jwtManager;
    @Override
    public UserDto.UserSignUpResponse signup(String aouthToken,
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
        // JWT 토큰 생성
        String jwtToken = jwtManager.createJwt(newUser.getId());

        // 회원가입 완료 후 JWT 토큰 반환
        return new UserDto.UserSignUpResponse(newUser.getId(), jwtToken);
    }

    public void saveAddSetInfo(User user, AddsetTitle title, List<String> values) {
        String value = String.join(",", values);  // List<String>을 콤마로 구분된 문자열로 변환

        ADD_set addSet = addSetRepository.findByTitleAndValue(title, value)
                .orElseGet(() -> {
                    ADD_set newAddSet = ADD_set.builder().title(title).value(value).build();
                    return addSetRepository.save(newAddSet);  // 존재하지 않으면 새로 저장
                });

        additionalRepository.save(new Additional(user, addSet));
    }




    @Override
    public void saveAdditionalInfo(User user, UserDto.UserAddInfoRequest request) {
        saveAddSetInfo(user, AddsetTitle.직업, Collections.singletonList(request.getOccupation()));
        saveAddSetInfo(user, AddsetTitle.소득수준, Collections.singletonList(String.valueOf(request.getIncome())));
        saveAddSetInfo(user, AddsetTitle.관심사, request.getInterests());
        saveAddSetInfo(user, AddsetTitle.가족구성, Collections.singletonList(request.getFamilyComposition()));
        saveAddSetInfo(user, AddsetTitle.관심제품유형, request.getProductTypes());
        saveAddSetInfo(user, AddsetTitle.스마트기기_기종, request.getSmartDevices());
        saveAddSetInfo(user, AddsetTitle.건강상태, Collections.singletonList(String.valueOf(request.getHealthStatus())));
    }


}
