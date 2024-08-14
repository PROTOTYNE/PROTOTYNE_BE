package com.prototyne.service.SignupService;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.domain.ADD_set;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.AddsetTitle;
import com.prototyne.domain.mapping.Additional;
import com.prototyne.repository.UserRepository;
import com.prototyne.web.dto.UserDto;

import java.util.List;

public interface UserSignupService {
    void saveAddSetInfo(User user, AddsetTitle title, List<String> values);

    void saveAdditionalInfo(User user, UserDto.UserAddInfoRequest request);

    UserDto.UserSignUpResponse signup(String aouthToken, UserDto.UserDetailRequest request, UserDto.UserAddInfoRequest addInfoRequest);
}
