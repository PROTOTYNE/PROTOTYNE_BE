package com.prototyne.Users.service.HeartService;

import com.prototyne.Users.web.dto.HeartDto;

import java.nio.file.attribute.UserPrincipalNotFoundException;

public interface HeartService {
    HeartDto.HeartResponseDTO getLikeList(String accessToken) throws UserPrincipalNotFoundException;
    HeartDto.HeartActionResponseDTO likeEvent(Long eventId, String accessToken);
    HeartDto.HeartActionResponseDTO unlikeEvent(Long eventId, String accessToken);

}
