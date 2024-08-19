package com.prototyne.service.AlarmService;

import com.prototyne.web.dto.AlarmDto;

import java.util.List;

public interface AlarmService {
    List<AlarmDto> getAlarmList(String accessToken);
}
