package com.mrq.bingo.model;


import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Getter
public class Strip {
    public static final int TICKETS_PER_STRIP = 6;
    private final List<Ticket> tickets;

    public Strip() {
        this.tickets = new ArrayList<>(TICKETS_PER_STRIP);
    }

    public void addTicket(Ticket ticket) {
        if (tickets.size() >= TICKETS_PER_STRIP) {
            throw new IllegalStateException("Strip already has maximum number of tickets");
        }
        tickets.add(ticket);
    }

    public Set<Integer> getAllNumbers() {
        Set<Integer> numbers = new HashSet<>();
        for (Ticket ticket : tickets) {
            for (int row = 0; row < Ticket.ROWS; row++) {
                for (int col = 0; col < Ticket.COLS; col++) {
                    Cell cell = ticket.getCell(row, col);
                    if (!cell.isEmpty()) {
                        numbers.add(cell.getValue());
                    }
                }
            }
        }
        return numbers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tickets.size(); i++) {
            sb.append("Ticket ").append(i + 1).append(":\n");
            sb.append(tickets.get(i).toString()).append("\n");
        }
        return sb.toString();
    }
}
