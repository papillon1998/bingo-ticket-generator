package com.mrq.bingo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Strip {
    private final List<Ticket> tickets;

    public Strip(List<Ticket> tickets) {
        this.tickets = new ArrayList<>(tickets);
    }

    public List<Ticket> getTickets() {
        return Collections.unmodifiableList(tickets);
    }
}