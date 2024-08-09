package com.prototyne.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
    private LocalDate createdAt;
    private String name;
    private String ticketDesc;
    private Integer ticketChange;
}
