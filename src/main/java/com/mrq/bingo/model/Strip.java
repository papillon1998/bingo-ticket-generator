package com.mrq.bingo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Strip(List<Ticket> tickets) {
    public Strip(List<Ticket> tickets) {
        this.tickets = Collections.unmodifiableList(new ArrayList<>(tickets));
    }
}