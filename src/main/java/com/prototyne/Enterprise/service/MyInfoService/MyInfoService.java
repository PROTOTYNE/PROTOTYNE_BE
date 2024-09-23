package com.prototyne.Enterprise.service.MyInfoService;

import com.prototyne.Enterprise.web.dto.MyInfoDTO;

import java.nio.file.attribute.UserPrincipalNotFoundException;

public interface MyInfoService {
    MyInfoDTO getMyInfo(String accessToken) throws UserPrincipalNotFoundException;
}
