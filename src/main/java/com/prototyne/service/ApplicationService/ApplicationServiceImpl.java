package com.prototyne.service.ApplicationService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.converter.InvestmentConverter;
import com.prototyne.domain.*;
import com.prototyne.repository.*;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.service.ProductService.EventService;
import com.prototyne.service.TicketService.TicketService;
import com.prototyne.web.dto.InvestmentDTO;
import com.prototyne.web.dto.TicketDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        if(investmentRepository.findByUserIdAndEventIdAndApply(userId, eventId,apply) != null){
            throw new TempHandler(ErrorStatus.EVENT_USER_EXIST);
        }

        if (apply) {
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

            // 알람 추가
            Alarm alarm = Alarm.builder()
                    .user(user)
                    .title("[시제품명] " + product.getName())
                    .contents("제품 후기 작성 마감 하루 전입니다!")
                    .thumbnailUrl(product.getThumbnailUrl())
                    .StartReview(event.getFeedbackEnd().minusDays(1))
                    .build();

            alarmRepository.save(alarm);

            eventService.saveInvestment(investment);
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
