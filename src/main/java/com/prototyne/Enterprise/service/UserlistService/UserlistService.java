package com.prototyne.Enterprise.service.UserlistService;

import com.prototyne.Enterprise.web.dto.UserlistDTO;
import org.springframework.stereotype.Service;

import java.util.List;
//@Service("enterpriseUserlistService")
public interface UserlistService {
    List<UserlistDTO.UserListResponse> getUserList(String token, Long eventId);
}
