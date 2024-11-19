package com.prototyne.Users.service.ApplicationService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.Users.converter.InvestmentConverter;
import com.prototyne.domain.*;
import com.prototyne.repository.*;
import com.prototyne.config.JwtManager;
import com.prototyne.Users.service.ProductService.EventService;
import com.prototyne.Users.service.TicketService.TicketService;
import com.prototyne.Users.web.dto.InvestmentDTO;
import com.prototyne.Users.web.dto.TicketDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final UserRepository userRepository;
    private final AlarmRespository alarmRepository;
    private final ProductRepository productRepository;
    private final JwtManager jwtManager;
    private final TicketService ticketService;
    private final EventRepository eventRepository;
    private final InvestmentConverter investmentConverter;
    private final EventService eventService;
    private final InvestmentRepository investmentRepository;

    @Override
    public InvestmentDTO.ApplicationResponse Application(String accessToken, Long eventId) {
        Long userId = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new TempHandler(ErrorStatus.LOGIN_ERROR_ID));

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new TempHandler(ErrorStatus.PRODUCT_ERROR_EVENT));


        Long productId = event.getProduct().getId();
        Product product = productRepository.findById(productId).orElseThrow(() -> new TempHandler(ErrorStatus.PRODUCT_ERROR_EVENT));


        String deliveryName = user.getDeliveryName();
        String deliveryPhone = user.getDeliveryPhone();
        String deliveryBaseAddress = user.getBaseAddress();
        String deliveryDetailAddress = user.getDetailAddress();

        String ticketName = product.getName();
        String ticketDesc = product.getEnterprise().getName();


        int userTickets = ticketService.getTicketNumber(accessToken).getTicketNumber();
        int reqTickets = product.getReqTickets();
        boolean apply = userTickets >= reqTickets;

        int eventSpeed= event.getSpeed();
        int userSpeed=user.getSpeed();
        boolean is_speed=userSpeed >=eventSpeed;


        if (investmentRepository.findFirstByUserIdAndEventId(userId, eventId).isPresent()) {
            throw new TempHandler(ErrorStatus.EVENT_USER_EXIST);
        }

        if (apply) {
            if(is_speed) {
                // 변경된 ticket 객체를 저장소에 저장
                TicketDto.TicketListDto ticketListDto = TicketDto.TicketListDto.builder()
                        .createdAt(LocalDateTime.now())
                        .name(ticketName)
                        .ticketDesc(ticketDesc)
                        .ticketChange(-reqTickets)
                        .build();

                Investment investment = investmentConverter.toInvestment(user, event, apply);

                // TicketService를 사용하여 티켓 저장
                ticketService.saveTicket(ticketListDto, user);

                //체험 신청 시, 해당 event의 시속만큼 사용자의 speed(시속) 차감

                // 알람 추가
                Alarm alarm = Alarm.builder()
                        .user(user)
                        .title("[시제품명] " + product.getName())
                        .contents("제품 후기 작성 마감 하루 전입니다!")
                        .thumbnailUrl(product.getThumbnailUrl())
                        .StartReview(event.getFeedbackEnd().minusDays(1))
                        .build();

                alarmRepository.save(alarm);
                alarmRepository.save(Alarm.builder()
                        .user(user)
                        .title("티켓 사용")
                        .contents("티켓 " + reqTickets + "개 사용 완료 - " + product.getName())
                        .thumbnailUrl(product.getThumbnailUrl())
                        .StartReview(LocalDate.now())
                        .build());

                eventService.saveInvestment(investment);
            }
            else{
                throw new TempHandler(ErrorStatus.USER_SPEED__LACK_ERROR);
            }

        } else {
            throw new TempHandler(ErrorStatus.TiCKET_LACK_ERROR);
        }
        return InvestmentDTO.ApplicationResponse.builder()
                .apply(true)
                .deliveryName(deliveryName)
                .deliveryPhone(deliveryPhone)
                .BaseAddress(deliveryBaseAddress)
                .DetailAddress(deliveryDetailAddress)
                .build();

    }

}
