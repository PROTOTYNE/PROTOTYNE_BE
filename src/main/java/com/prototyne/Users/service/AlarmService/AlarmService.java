package com.prototyne.Users.service.AlarmService;

import com.prototyne.Users.web.dto.AlarmDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface AlarmService {

    SseEmitter subscribe(String accessToken);
    //void sendAlarm(String accessToken, String msg);
    List<AlarmDto> getAlarmList(String accessToken);
}
