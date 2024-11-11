package com.mrq.bingo.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StripTest {

    @Test
    @DisplayName("Should add tickets up to maximum")
    void shouldAddTicketsUpToMaximum() {
        Strip strip = new Strip();

        for (int i = 0; i < Strip.TICKETS_PER_STRIP; i++) {
            strip.addTicket(new Ticket());
        }

        assertThat(strip.getTickets()).hasSize(Strip.TICKETS_PER_STRIP);
        assertThatThrownBy(() -> strip.addTicket(new Ticket()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Should collect all numbers from tickets")
    void shouldCollectAllNumbers() {
        Strip strip = new Strip();
        Ticket ticket = new Ticket();
        ticket.setNumber(0, 0, 5);
        ticket.setNumber(1, 1, 15);
        strip.addTicket(ticket);

        Set<Integer> numbers = strip.getAllNumbers();

        assertThat(numbers)
                .hasSize(2)
                .contains(5, 15);
    }
}