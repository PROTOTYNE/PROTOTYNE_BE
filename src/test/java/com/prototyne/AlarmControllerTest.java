package com.prototyne;

import com.prototyne.Users.service.AlarmService.AlarmService;
import com.prototyne.Users.web.controller.AlarmController;
import com.prototyne.config.JwtManager;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AlarmControllerTest {

    @InjectMocks
    private AlarmController alarmController;

    @Mock
    private AlarmService alarmService;

    @Mock
    private JwtManager jwtManager;

    @Test
    void subscribe_shouldReturnSseEmitter() {
        String accessToken = "mockAccessToekn";
        SseEmitter mockEmitter = new SseEmitter();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        given(jwtManager.getToken(any(HttpServletRequest.class))).willReturn(accessToken);
        given(alarmService.subscribe(accessToken)).willReturn(mockEmitter);

        SseEmitter res= alarmController.subscribe(mockRequest);

        assertNotNull(res);
        verify(jwtManager).getToken(any(HttpServletRequest.class));
        verify(alarmService).subscribe(accessToken);
    }



}
