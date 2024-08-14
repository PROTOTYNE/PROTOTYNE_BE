package com.prototyne.service.HeartService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.converter.HeartConverter;
import com.prototyne.domain.Event;
import com.prototyne.domain.User;
import com.prototyne.domain.mapping.Heart;
import com.prototyne.repository.EventRepository;
import com.prototyne.repository.HeartRepository;
import com.prototyne.repository.UserRepository;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.web.dto.HeartDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeartServiceImpl implements HeartService{
    private final HeartRepository heartRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final JwtManager jwtManager;

    @Override
    public HeartDto.HeartResponseDTO getLikeList(String accessToken) throws UserPrincipalNotFoundException{
        Long userId = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserPrincipalNotFoundException(userId + "에 해당하는 회원이 없습니다."));

        // 사용자가 좋아요 누른 이벤트 목록 조회
        List<Heart> hearts = heartRepository.findByUser(user);

        log.info("accessToken in Service",accessToken);

        return HeartConverter.toHeartResponseDTO(user, hearts, heartRepository);
    }

    @Transactional
    @Override
    public HeartDto.HeartActionResponseDTO likeEvent(Long eventId, String accessToken) {
        Long userId = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event ID: " + eventId));

        // 이미 좋아요를 누른 경우 처리
        if (heartRepository.findByUserAndEvent(user, event).isPresent()) {
            throw new IllegalArgumentException("이미 좋아요를 누른 이벤트입니다.");
        }

        Heart heart = HeartConverter.toHeartEntity(user, event);
        heartRepository.save(heart);
        return HeartConverter.toHeartActionResponseDTO(eventId, "북마크가 성공적으로 등록되었습니다.");
    }

    @Transactional
    @Override
    public HeartDto.HeartActionResponseDTO unlikeEvent(Long eventId, String accessToken) {
        Long userId = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.HEART_ERROR_ID));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.HEART_ERROR_EVENT));

        Heart heart = heartRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new IllegalArgumentException("해제할 북마크가 없습니다."));

        heartRepository.delete(heart); // 북마크 삭제
        return HeartConverter.toHeartActionResponseDTO(eventId, "북마크가 성공적으로 해제되었습니다.");
    }
}
