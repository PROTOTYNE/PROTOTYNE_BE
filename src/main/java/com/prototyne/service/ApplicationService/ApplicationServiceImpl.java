package com.prototyne.service.ApplicationService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.converter.TicketConverter;
import com.prototyne.domain.*;
import com.prototyne.repository.*;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.service.TicketService.TicketService;
import com.prototyne.web.dto.InvestmentDTO;
import com.prototyne.web.dto.TicketDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ApplicationServiceImpl implements ApplicationService{

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final JwtManager jwtManager;
    private final TicketRepository ticketRepository;
    private final TicketConverter ticketConverter;
    private final TicketService ticketService;

    @Override
    public InvestmentDTO.ApplicationResponse Application(String accessToken,Long productId, String ticketName, String ticketDesc) {
        Long userId = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new TempHandler(ErrorStatus.LOGIN_ERROR_ID));

        Product product =productRepository.findById(productId).orElseThrow(() -> new TempHandler(ErrorStatus.PRODUCT_ERROR_EVENT));
        //List<Ticket> tickets=ticketRepository.findByUserId(userId);

        String deliveryName=user.getDeliveryName();
        String deliveryPhone=user.getDeliveryPhone();
        String deliveryAddress=user.getDeliveryAddress();
        //Integer ticketChange=ticket.getTicketChange();


        if(deliveryName==null || deliveryPhone==null || deliveryAddress==null) {
            throw new TempHandler(ErrorStatus.DELIVERY_ERROR_NAME);
        }

        int userTickets=user.getTickets();
        int reqTickets=product.getReqTickets();

        Boolean apply = userTickets >= reqTickets;


        if(apply){
            //tickets.setTicketChange(ticketChange-reqTickets);// 변경된 티켓 수를 설정
            // 변경된 ticket 객체를 저장소에 저장
            //Ticket ticket=ticketConverter.toEntity(ticketListDto, user);
            TicketDto.TicketListDto ticketListDto = TicketDto.TicketListDto.builder()
                    .createdAt(LocalDateTime.now())
                    .name(ticketName)
                    .ticketDesc(ticketDesc)
                    .ticketChange(-reqTickets)
                    .build();
            // TicketService를 사용하여 티켓 저장
            ticketService.saveTicket(ticketListDto, user);
        }
        return InvestmentDTO.ApplicationResponse.builder()
                .apply(apply)
                .build();

    }

}
