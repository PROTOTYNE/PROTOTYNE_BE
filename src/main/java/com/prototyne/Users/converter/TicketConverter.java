package com.prototyne.Users.converter;

import com.prototyne.domain.Ticket;
import com.prototyne.domain.User;
import com.prototyne.Users.web.dto.TicketDto;
import org.springframework.stereotype.Component;

@Component
public class TicketConverter {

    public Ticket toEntity(TicketDto.TicketListDto ticketListDto, User user) {
        return Ticket.builder()
                .name(ticketListDto.getName())
                .ticketDesc(ticketListDto.getTicketDesc())
                .ticketChange(ticketListDto.getTicketChange())
                .user(user)  // 해당 유저와 연관시킵니다.
                .build();
    }

    public TicketDto.TicketListDto toDto(Ticket ticket) {
        return TicketDto.TicketListDto.builder()
                .createdAt(ticket.getCreatedAt())
                .name(ticket.getName())
                .ticketDesc(ticket.getTicketDesc())
                .ticketChange(ticket.getTicketChange())
                .build();
    }
}
