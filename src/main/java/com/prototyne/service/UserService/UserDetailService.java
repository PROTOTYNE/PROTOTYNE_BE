package com.prototyne.service.UserService;

import com.prototyne.domain.User;
import com.prototyne.web.dto.UserDto;
import jakarta.transaction.Transactional;

import java.nio.file.attribute.UserPrincipalNotFoundException;

public interface UserDetailService {
    UserDto.UserDetailResponse getUserDetail(String accessToken) throws UserPrincipalNotFoundException;

    UserDto.DetailInfo updateBasicInfo(String accessToken, UserDto.DetailInfo detailInfo) throws UserPrincipalNotFoundException;

    UserDto.AddInfo updateAddInfo(String accessToken, UserDto.AddInfo addInfo) throws UserPrincipalNotFoundException;
}
