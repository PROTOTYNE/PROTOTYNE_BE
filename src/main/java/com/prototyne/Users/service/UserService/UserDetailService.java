package com.prototyne.Users.service.UserService;

import com.prototyne.Users.web.dto.UserDto;

import java.nio.file.attribute.UserPrincipalNotFoundException;

public interface UserDetailService {
    UserDto.UserDetailResponse getUserDetail(String accessToken) throws UserPrincipalNotFoundException;

    UserDto.DetailInfo updateBasicInfo(String accessToken, UserDto.DetailInfo detailInfo) throws UserPrincipalNotFoundException;

    UserDto.AddInfo updateAddInfo(String accessToken, UserDto.AddInfo addInfo) throws UserPrincipalNotFoundException;

    UserDto.MyPageInfo getMyPageInfo(String accessToken) throws UserPrincipalNotFoundException;
}
