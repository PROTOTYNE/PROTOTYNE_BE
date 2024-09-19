package com.prototyne.Users.service.TicketService;

import com.prototyne.domain.User;
import com.prototyne.Users.web.dto.TicketDto;

import java.util.List;

public interface TicketService {
    TicketDto.TicketNumberDto getTicketNumber(String accessToken);

    List<TicketDto.TicketListDto> getTicketList(String accessToken);

    List<TicketDto.TicketListDto> getTicketDateList(String accessToken, String startDate, String endDate, boolean used);

    void saveTicket(TicketDto.TicketListDto ticketListDto, User user);

    void buyTicket(String accessToken, int ticketNumber);

    void provideMonthlyTicket();

}
