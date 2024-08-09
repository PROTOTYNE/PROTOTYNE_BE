package com.prototyne.service.SignupService;

import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.domain.ADD_set;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.AddsetTitle;
import com.prototyne.domain.mapping.Additional;
import com.prototyne.repository.UserRepository;
import com.prototyne.web.dto.UserDto;

public interface UserSignupService {
    void saveAddSetInfo(User user, AddsetTitle title, String value);

    void saveAdditionalInfo(User user, UserDto.UserAddInfoRequest request);

    User signup(String aouthToken, UserDto.UserDetailRequest request, UserDto.UserAddInfoRequest addInfoRequest);
}
