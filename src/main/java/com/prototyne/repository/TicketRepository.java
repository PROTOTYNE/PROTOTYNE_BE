package com.prototyne.repository;

import com.prototyne.domain.Ticket;
import com.prototyne.repository.querydsl.TicketRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketRepositoryCustom {
    List<Ticket> findByUserId(Long userId);

    //Ticket findByUserIdAndId(Long userId, Long id);
}
