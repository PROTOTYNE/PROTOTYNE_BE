package com.prototyne.Users.service.UserService;

import com.prototyne.Users.converter.UserConverter;
import com.prototyne.domain.ADD_set;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.AddsetTitle;
import com.prototyne.domain.enums.Gender;
import com.prototyne.domain.mapping.Additional;
import com.prototyne.repository.ADD_setRepository;
import com.prototyne.repository.AdditionalRepository;
import com.prototyne.repository.UserRepository;
import com.prototyne.apiPayload.config.JwtManager;
import com.prototyne.Users.web.dto.UserDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailService {

    private final UserRepository userRepository;
    private final AdditionalRepository additionalRepository;
    private final ADD_setRepository addSetRepository;
    private final JwtManager jwtManager;
    private final UserConverter userConverter;


    @Override
    public UserDto.UserDetailResponse getUserDetail(String accessToken) throws UserPrincipalNotFoundException {
        Long userId = jwtManager.validateJwt(accessToken);

        // 추출한 사용자 ID를 사용하여 사용자 정보를 조회합니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserPrincipalNotFoundException(userId + "에 해당하는 회원이 없습니다."));

        List<Additional> additionalInfo = additionalRepository.findByUserId(userId);

        UserDto.AddInfo convertedAddInfo = convertToAddInfo(additionalInfo);

        return userConverter.toUserDetailResponse(user, convertedAddInfo);

    }

    private UserDto.AddInfo convertToAddInfo(List<Additional> additionalInfo) {
        UserDto.AddInfo addInfo = new UserDto.AddInfo();

        for (Additional additional : additionalInfo) {
            String[] values = additional.getAddSet().getValue().split(",");
            List<String> valueList = Arrays.asList(values);

            switch (additional.getAddSet().getTitle()) {
                case 직업:
                    addInfo.setOccupation(valueList.isEmpty()? null : valueList.get(0));  // 직업은 단일값
                    break;
                case 소득수준:
                    addInfo.setIncome(valueList.isEmpty()? 0 : Integer.parseInt(valueList.get(0)));  // 소득수준은 단일값
                    break;
                case 관심사:
                    addInfo.setInterests(valueList);  // 관심사는 리스트
                    break;
                case 가족구성:
                    addInfo.setFamilyComposition(valueList.isEmpty()? null : valueList.get(0));  // 가족구성은 단일값
                    break;
                case 관심제품유형:
                    addInfo.setProductTypes(valueList);  // 관심제품유형은 리스트
                    break;
                case 스마트기기_기종:
                    addInfo.setPhones(valueList);  // 스마트기기_기종은 리스트
                    break;
                case 건강상태:
                    addInfo.setHealthStatus(valueList.isEmpty()? null : Integer.parseInt(valueList.get(0)));  // 건강상태는 단일값
                    break;
            }
        }

        return addInfo;
    }

    @Transactional
    @Override
    public UserDto.DetailInfo updateBasicInfo(String accessToken, UserDto.DetailInfo detailInfo) throws UserPrincipalNotFoundException {
        Long userId = jwtManager.validateJwt(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserPrincipalNotFoundException(userId + "에 해당하는 회원이 없습니다."));

        // 업데이트할 정보를 설정합니다.
        user.setFamilyMember(detailInfo.getFamilyMember());
        user.setGender(Gender.valueOf(detailInfo.getGender()));
        user.setBirth(LocalDate.parse(detailInfo.getBirth()));

        userRepository.save(user);

        return userConverter.toUserDetailResponse(user, null).getDetailInfo();  // 추가 정보가 없으므로 null 전달
    }

    @Transactional
    @Override
    public UserDto.AddInfo updateAddInfo(String accessToken, UserDto.AddInfo addInfo) throws UserPrincipalNotFoundException {
        Long userId = jwtManager.validateJwt(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserPrincipalNotFoundException(userId + "에 해당하는 회원이 없습니다."));

        // addInfo(dto)의 필드값들에 대하여, 값이 존재하는 경우에만 업로드해준다.
        if(addInfo.getOccupation() != null){
            updateAddSetInfo(user ,AddsetTitle.직업, addInfo.getOccupation());
        }
        if (addInfo.getIncome() != null) {
            updateAddSetInfo(user, AddsetTitle.소득수준, String.valueOf(addInfo.getIncome()));
        }
        if (addInfo.getInterests() != null) {
            updateAddSetInfo(user, AddsetTitle.관심사, String.join(",", addInfo.getInterests()));
        }
        if (addInfo.getFamilyComposition() != null) {
            updateAddSetInfo(user, AddsetTitle.가족구성, addInfo.getFamilyComposition().toString());
        }
        if (addInfo.getProductTypes() != null) {
            updateAddSetInfo(user, AddsetTitle.관심제품유형, String.join(",", addInfo.getProductTypes()));
        }
        if (addInfo.getPhones() != null) {
            updateAddSetInfo(user, AddsetTitle.스마트기기_기종, String.join(",", addInfo.getPhones()));
        }
        if (addInfo.getHealthStatus() != null) {
            updateAddSetInfo(user, AddsetTitle.건강상태, String.valueOf(addInfo.getHealthStatus()));
        }
        return userConverter.toUserDetailResponse(user, addInfo).getAddInfo();
    }

    private void updateAddSetInfo(User user, AddsetTitle title, String newValue) {
        if (newValue != null && !newValue.isEmpty()) {
            // 기존 ADD_set 엔티티를 찾는다.
            ADD_set existingAddSet = additionalRepository.findByUserAndAddSet_Title(user, title)
                    .map(Additional::getAddSet)
                    .orElse(null);

            if (existingAddSet != null) {
                additionalRepository.deleteByUserAndAddSet_Title(user, title);
                addSetRepository.delete(existingAddSet);
            }

            ADD_set newAddSet = addSetRepository.save(ADD_set.builder()
                    .title(title)
                    .value(newValue)
                    .build());

            additionalRepository.save(new Additional(user, newAddSet));  // 새로운 관계 저장
        }
    }
}
