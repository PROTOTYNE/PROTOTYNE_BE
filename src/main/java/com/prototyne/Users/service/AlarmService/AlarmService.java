package com.prototyne.Users.service.AlarmService;

import com.prototyne.Users.web.dto.AlarmDto;

import java.util.List;

public interface AlarmService {
    List<AlarmDto> getAlarmList(String accessToken);
}
