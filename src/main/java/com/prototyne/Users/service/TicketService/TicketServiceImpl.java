package com.prototyne.Users.service.TicketService;

import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.Users.converter.TicketConverter;
import com.prototyne.domain.Alarm;
import com.prototyne.domain.Ticket;
import com.prototyne.domain.User;
import com.prototyne.domain.enums.InvestmentStatus;
import com.prototyne.repository.*;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.Users.web.dto.TicketDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final AlarmRespository alarmRespository;
    private final UserRepository userRepository;
    private final MyProductRepository myProductRepository;
    private final FeedbackRepository feedbackRepository;

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

        Long userId = jwtManager.validateJwt(accessToken);
        LocalDateTime now = LocalDateTime.now();

        // usedTicket
        Integer usedTicket = (int) getTicketList(accessToken).stream()
                .filter(ticket -> ticket.getCreatedAt().isBefore(now) && ticket.getTicketChange() < 0)
                .count();

        // appliedNum
        Integer appliedNum = (int) myProductRepository.findByUserId(userId).stream()
                .filter(investment -> investment.getStatus() == InvestmentStatus.신청)
                .filter(investment -> investment.getEvent().getReleaseStart().isAfter(now))
                .count();

        // selectedNum
        Integer selectedNum = (int) myProductRepository.findByUserId(userId).stream()
                .filter(investment -> investment.getStatus() == InvestmentStatus.후기작성)
                .count();

        // ongoingNum
        Integer ongoingNum = (int) myProductRepository.findByUserId(userId).stream()
                .filter(investment -> investment.getStatus() == InvestmentStatus.당첨)
                .count();

        // completedNum
        Integer completedNum = (int) feedbackRepository.findByUserId(userId).stream()
                .filter(feedback -> feedback.getInvestment().getStatus() == InvestmentStatus.종료)
                .count();

        return TicketDto.TicketNumberDto.builder()
                .ticketNumber(ticketNumber.get())
                .usedTicket(usedTicket)
                .appliedNum(appliedNum)
                .selectedNum(selectedNum)
                .ongoingNum(ongoingNum)
                .completedNum(completedNum)
                .build();
    }

    public void saveTicket(TicketDto.TicketListDto ticketListDto, User user) {

        // Converter를 사용하여 DTO를 엔티티로 변환
        Ticket ticket = ticketConverter.toEntity(ticketListDto, user);

        // 변환된 엔티티를 저장
        ticketRepository.save(ticket);
    }

    @Override
    public void buyTicket(String accessToken, int ticketNumber) {
        Long id = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(id).orElseThrow(() -> new TempHandler(ErrorStatus.LOGIN_ERROR_ID));
        ticketRepository.save(Ticket.builder()
                .user(user)
                .name("프로토타인")
                .ticketDesc("티켓 구매")
                .ticketChange(ticketNumber)
                .build());
        alarmRespository.save(Alarm.builder()
                .user(user)
                .title("티켓 구매")
                .contents("티켓 " + ticketNumber + "개 구매 완료")
                .StartReview(LocalDateTime.now())
                .build());
    }

}
