package com.prototyne.service.ApplicationService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.converter.InvestmentConverter;
import com.prototyne.converter.TicketConverter;
import com.prototyne.domain.Event;
import com.prototyne.domain.Investment;
import com.prototyne.domain.Product;
import com.prototyne.domain.User;
import com.prototyne.repository.EventRepository;
import com.prototyne.repository.ProductRepository;
import com.prototyne.repository.TicketRepository;
import com.prototyne.repository.UserRepository;
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
    private final ProductRepository productRepository;
    private final JwtManager jwtManager;
    private final TicketRepository ticketRepository;
    private final TicketConverter ticketConverter;
    private final TicketService ticketService;
    private final EventRepository eventRepository;
    private final InvestmentConverter investmentConverter;
    private final EventService eventService;

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

        Boolean apply = userTickets >= reqTickets;


        if (apply) {
            // 변경된 ticket 객체를 저장소에 저장
            TicketDto.TicketListDto ticketListDto = TicketDto.TicketListDto.builder()
                    .createdAt(LocalDateTime.now())
                    .name(ticketName)
                    .ticketDesc(ticketDesc)
                    .ticketChange(-reqTickets)
                    .build();

            Investment investment = investmentConverter.toInvestment(user, event);

            // TicketService를 사용하여 티켓 저장
            ticketService.saveTicket(ticketListDto, user);

            eventService.saveInvestment(investment);
        }
        return InvestmentDTO.ApplicationResponse.builder()
                .apply(apply)
                .deliveryName(deliveryName)
                .deliveryPhone(deliveryPhone)
                .BaseAddress(deliveryBaseAddress)
                .DetailAddress(deliveryDetailAddress)
                .build();

    }

}
