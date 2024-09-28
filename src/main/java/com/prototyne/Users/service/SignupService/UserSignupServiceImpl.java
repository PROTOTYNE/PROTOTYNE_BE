package com.prototyne.Users.service.SignupService;

import com.prototyne.Users.service.TokenService.TokenService;
import com.prototyne.Users.web.dto.JwtTokenDto;
import com.prototyne.Users.web.dto.UserDto;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.config.JwtManager;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.ADD_set;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.AddsetTitle;
import com.prototyne.domain.mapping.Additional;
import com.prototyne.repository.ADD_setRepository;
import com.prototyne.repository.AdditionalRepository;
import com.prototyne.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final JwtManager jwtManager;
    private final TokenService tokenService;

    @Override
    public UserDto.UserSignUpResponse signup(String aouthToken,
                       UserDto.UserDetailRequest userDetailRequest,
                       UserDto.UserAddInfoRequest addInfoRequest){
        Long userId;
        try {
            userId = jwtManager.validateAccessToken(aouthToken);
        } catch (TempHandler e) {
            JwtTokenDto newTokens = tokenService.refreshAccessToken(aouthToken);
            userId = jwtManager.validateAccessToken(newTokens.getAccessToken());
        }
        User existingUser = userRepository.findById(userId).orElse(null);

        if(existingUser != null && existingUser.getSignupComplete()) {
            throw new TempHandler(ErrorStatus.SIGNUP_DUPLICATE);
        }else if(existingUser == null){
            throw new TempHandler(ErrorStatus.SIGNUP_LOGIN_ERROR);
        }

        // 회원가입 로직: 카카오 로그인 -> 필수 정보 입력 -> 추가 정보 입력 -> 그 후에 회원가입 완료(db에 저장)
        existingUser.setDetail(userDetailRequest);
        existingUser.setSignupComplete(true);
//        User newUser = UserConverter.toSignedUser(existingUser, userDetailRequest);
        userRepository.save(existingUser);

        saveAdditionalInfo(existingUser, addInfoRequest);

        return new UserDto.UserSignUpResponse(existingUser.getId(), "회원가입이 완료되었습니다.");
    }

    public void saveAddSetInfo(User user, AddsetTitle title, List<String> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
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
        if (request.getOccupation() != null) {
            saveAddSetInfo(user, AddsetTitle.직업, Collections.singletonList(request.getOccupation()));
        }
        if (request.getIncome() != null) {
            saveAddSetInfo(user, AddsetTitle.소득수준, Collections.singletonList(String.valueOf(request.getIncome())));
        }
        if (request.getInterests() != null && !request.getInterests().isEmpty()) {
            saveAddSetInfo(user, AddsetTitle.관심사, request.getInterests());
        }
        if (request.getFamilyComposition() != null) {
            saveAddSetInfo(user, AddsetTitle.가족구성, Collections.singletonList(request.getFamilyComposition()));
        }
        if (request.getProductTypes() != null && !request.getProductTypes().isEmpty()) {
            saveAddSetInfo(user, AddsetTitle.관심제품유형, request.getProductTypes());
        }
        if (request.getPhones() != null && !request.getPhones().isEmpty()) {
            saveAddSetInfo(user, AddsetTitle.스마트기기_기종, request.getPhones());
        }
        if (request.getHealthStatus() != null) {
            saveAddSetInfo(user, AddsetTitle.건강상태, Collections.singletonList(String.valueOf(request.getHealthStatus())));
        }
    }


}
