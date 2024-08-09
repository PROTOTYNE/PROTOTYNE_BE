package com.prototyne.converter;

import com.prototyne.domain.User;
import com.prototyne.web.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
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
                .tickets(user.getTickets())
                .gender(user.getGender())
                .birth(user.getBirth())
                .build();
    }

    public static User toSignedUser(UserDto.UserInfoResponse kakaoUserInfo, UserDto.UserDetailRequest userDetailRequest){
        return User.builder()
                .email(kakaoUserInfo.getKakaoAccount().getEmail())
                .username(kakaoUserInfo.getKakaoAccount().getProfile().getNickName())
                .gender(userDetailRequest.getGender())
                .birth(userDetailRequest.getBirth())
                .familyMember(userDetailRequest.getFamilyMember())
                .build();
    }
}