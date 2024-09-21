package com.prototyne.domain.enums;

import java.util.Arrays;

public enum TicketOption {
    FIVE_OPTIONS(5, 5000),
    TEN_OPTIONS(10, 9000),
    TWENTY_OPTIONS(20, 16000);

    private final int ticketNumber;
    private final int price;

    TicketOption(int ticketNumber, int price) {
        this.ticketNumber = ticketNumber;
        this.price = price;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public int getPrice() {
        return price;
    }

    public static TicketOption findByTicketNumber(int ticketNumber) {
        return Arrays.stream(TicketOption.values())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid ticket number: " + ticketNumber));
    }

}
