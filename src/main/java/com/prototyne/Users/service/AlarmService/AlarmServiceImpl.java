package com.prototyne.Users.service.AlarmService;

import com.prototyne.repository.AlarmRespository;
import com.prototyne.config.JwtManager;
import com.prototyne.Users.web.dto.AlarmDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRespository alarmRespository;
    private final JwtManager jwtManager;

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
