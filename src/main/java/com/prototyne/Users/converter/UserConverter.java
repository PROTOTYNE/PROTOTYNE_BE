package com.prototyne.Users.converter;

import com.prototyne.Users.web.dto.UserDto;
import com.prototyne.domain.User;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Builder
public class UserConverter {
    public static User toUser(UserDto.UserInfoResponse userinfo) {
        return User.builder()
                .id(userinfo.getId())
                .email(userinfo.getKakaoAccount().getEmail())
                .username(userinfo.getKakaoAccount().getProfile().getNickName())
                .profileUrl(userinfo.getKakaoAccount().getProfile().getProfileImageUrl())
                .build();
    }

    public static UserDto.UserRequest toUserInfoDto(User user) {
        return UserDto.UserRequest.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .profileUrl(user.getProfileUrl())
                .gender(user.getGender())
                .birth(user.getBirth())
                .build();
    }

    public static User toSignedUser(User user, UserDto.UserDetailRequest userDetailRequest) {
        return User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profileUrl(user.getProfileUrl())
                .signupComplete(true)
                .gender(userDetailRequest.getGender())
                .birth(LocalDate.from(userDetailRequest.getBirth()))
                .familyMember(userDetailRequest.getFamilyMember())
                .build();
    }

    public UserDto.UserDetailResponse toUserDetailResponse(User user, UserDto.AddInfo additionalInfo) {
        // DetailInfo 생성
        UserDto.DetailInfo detailInfo = UserDto.DetailInfo.builder()
                .familyMember(user.getFamilyMember())
                .gender(user.getGender().toString())
                .birth(user.getBirth().toString())
                .build();

        // AddInfo 생성
        UserDto.AddInfo addInfo = (additionalInfo != null) ? UserDto.AddInfo.builder()
                .occupation(additionalInfo.getOccupation())
                .income(additionalInfo.getIncome())
                .interests(additionalInfo.getInterests())
                .familyComposition(additionalInfo.getFamilyComposition())
                .productTypes(additionalInfo.getProductTypes())
                .phones(additionalInfo.getPhones())
                .healthStatus(additionalInfo.getHealthStatus())
                .build() : new UserDto.AddInfo();

        // UserDetailResponse 생성
        return UserDto.UserDetailResponse.builder()
                .username(user.getUsername())
                .detailInfo(detailInfo)
                .addInfo(addInfo)
                .build();
    }

    public static UserDto.MyPageInfo toMyPageInfo(User user, int usedtickets, int investmentCnt) {
        UserDto.UserSpeed userSpeed = UserDto.UserSpeed.builder()
                .username(user.getUsername())
                .profileUrl(user.getProfileUrl())
                .speed(user.getSpeed())
                .build();

        return UserDto.MyPageInfo.builder()
                .userSpeed(userSpeed)
                .usedTicket(usedtickets)
                .investmentCnt(investmentCnt)
                .build();
    }
}