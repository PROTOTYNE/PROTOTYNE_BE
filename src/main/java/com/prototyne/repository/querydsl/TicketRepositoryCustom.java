package com.prototyne.repository.querydsl;

public interface TicketRepositoryCustom {
    int getUsedTickets(Long userId);
    int getInvestmentCnt(Long userId);
}
