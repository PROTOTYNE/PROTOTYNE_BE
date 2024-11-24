package com.prototyne.Users.service.AlarmService;

import com.prototyne.Users.web.dto.AlarmDto;
import com.prototyne.config.JwtManager;
import com.prototyne.repository.AlarmRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRespository alarmRespository;
    private final JwtManager jwtManager;
    private final long TIMEOUT_TTL = 600000L;
    private final Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter subscribe(String accessToken){
        Long userId = jwtManager.validateJwt(accessToken);
        SseEmitter sseEmitter = new SseEmitter(TIMEOUT_TTL);
        sseEmitters.put(userId, sseEmitter);
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("connect")
                    .data("Connected successfully"));
        } catch (Exception e){
            sseEmitters.remove(userId);
        }
        sseEmitter.onCompletion(()-> sseEmitters.remove(userId));
        sseEmitter.onTimeout(()-> sseEmitters.remove(userId));
        sseEmitter.onError((e) -> sseEmitters.remove(userId));

        return sseEmitter;
    }

    @Override
    @Async
    public void sendAlarm(String accessToken, String msg){
        Long userId= jwtManager.validateJwt(accessToken);
        SseEmitter emitter = sseEmitters.get(userId);

        if(emitter != null){
            try {
                emitter.send(SseEmitter.event()
                        .name("알림")
                        .data(msg));
                log.info("해당 userId로 알림전송 성공 {}: {}", userId, msg);
            } catch (Exception e){
                log.error("해당 userId로 알림전송 실패 {}: {}", userId, msg);
                sseEmitters.remove(userId);
            }
        }
        else {
            log.warn("해당 userId에 활성화된 SSE Connection이 없음 {}", userId);
        }
    }

    @Override
    public List<AlarmDto> getAlarmList(String accessToken) {
        Long id = jwtManager.validateJwt(accessToken);
        return alarmRespository.findByUserId(id).stream().sorted((o1, o2) -> o2.getStartReview().compareTo(o1.getStartReview())).filter(alarm -> !alarm.getStartReview().isAfter(LocalDate.now())).map(alarm -> AlarmDto.builder()
                        .title(alarm.getTitle())
                        .contents(alarm.getContents())
                        .thumbnailUrl(alarm.getThumbnailUrl())
                        .dateAgo(LocalDateTime.now().getDayOfYear() - alarm.getStartReview().getDayOfYear())
                        .build())
                .collect(Collectors.toList());
    }

}
