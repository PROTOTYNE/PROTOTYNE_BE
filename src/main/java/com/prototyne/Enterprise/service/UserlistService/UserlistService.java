package com.prototyne.Enterprise.service.UserlistService;

import com.prototyne.Enterprise.web.dto.UserlistDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
//@Service("enterpriseUserlistService")
public interface UserlistService {
    List<UserlistDTO.UserListResponse> getUserList(String token, Long eventId);
    UserlistDTO.UserListResponse updateUserPrize(String token, Long eventId, Long userId, Boolean isPrize);
    UserlistDTO.UserListResponse updateUserDelivery(String token, Long eventId, Long userId, Boolean isDelivery);
}
