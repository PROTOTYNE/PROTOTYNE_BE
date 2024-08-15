package com.prototyne.service.TicketService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.converter.TicketConverter;
import com.prototyne.domain.Ticket;
import com.prototyne.domain.User;
import com.prototyne.repository.TicketRepository;
import com.prototyne.service.LoginService.JwtManager;
import com.prototyne.web.dto.TicketDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final JwtManager jwtManager;
    private final TicketRepository ticketRepository;
    private final TicketConverter ticketConverter;

    @Override
    public List<TicketDto.TicketListDto> getTicketList(String accessToken) {
        Long id = jwtManager.validateJwt(accessToken);

        return ticketRepository.findByUserId(id).stream().sorted((o2, o1) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .map(ticket -> TicketDto.TicketListDto.builder()
                        .name(ticket.getName())
                        .createdAt(ticket.getCreatedAt())
                        .ticketDesc(ticket.getTicketDesc())
                        .ticketChange(ticket.getTicketChange())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDto.TicketListDto> getTicketDateList(String accessToken, String startDate, String endDate, boolean used) {
        List<TicketDto.TicketListDto> ticketList = getTicketList(accessToken);
        LocalDate start;
        LocalDate end;
        try {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
        } catch (DateTimeParseException e) {
            throw new TempHandler(ErrorStatus.DATE_FORMAT_ERROR);
        }
        return ticketList.stream()
                .filter(ticket -> {
                    LocalDate ticketDate = ticket.getCreatedAt().toLocalDate();
                    return !ticketDate.isBefore(start) && !ticketDate.isAfter(end);
                })
                .filter(ticket -> !used || ticket.getTicketChange() < 0)
                .collect(Collectors.toList());
    }

    @Override
    public TicketDto.TicketNumberDto getTicketNumber(String accessToken) {
        AtomicReference<Integer> ticketNumber = new AtomicReference<>(0);
        getTicketList(accessToken).forEach(ticket -> ticketNumber.updateAndGet(v -> v + ticket.getTicketChange()));
        return TicketDto.TicketNumberDto.builder()
                .ticketNumber(ticketNumber.get())
                .build();
    }

    public void saveTicket(TicketDto.TicketListDto ticketListDto, User user) {

        // Converter를 사용하여 DTO를 엔티티로 변환
        Ticket ticket = ticketConverter.toEntity(ticketListDto, user);

        // 변환된 엔티티를 저장
        ticketRepository.save(ticket);
    }

}
