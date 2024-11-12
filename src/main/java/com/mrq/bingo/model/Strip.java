package com.mrq.bingo.model;

import java.util.List;

public record Strip(List<Ticket> tickets) {
    public Strip(List<Ticket> tickets) {
        this.tickets = List.copyOf(tickets);
    }
}