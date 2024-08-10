package com.prototyne.service.UserService;

import com.prototyne.domain.User;
import com.prototyne.web.dto.UserDto;

import java.nio.file.attribute.UserPrincipalNotFoundException;

public interface UserDetailService {
    UserDto.UserDetailResponse getUserDetail(String accessToken) throws UserPrincipalNotFoundException;
}
