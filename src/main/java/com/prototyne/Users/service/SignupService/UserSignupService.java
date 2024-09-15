package com.prototyne.Users.service.SignupService;

import com.prototyne.domain.User;
import com.prototyne.domain.enums.AddsetTitle;
import com.prototyne.Users.web.dto.UserDto;

import java.util.List;

public interface UserSignupService {
    void saveAddSetInfo(User user, AddsetTitle title, List<String> values);

    void saveAdditionalInfo(User user, UserDto.UserAddInfoRequest request);

    UserDto.UserSignUpResponse signup(String aouthToken, UserDto.UserDetailRequest request, UserDto.UserAddInfoRequest addInfoRequest);
}
