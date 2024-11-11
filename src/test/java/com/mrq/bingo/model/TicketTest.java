package com.mrq.bingo.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
class TicketTest {

    @Test
    @DisplayName("New ticket should have all cells empty")
    void newTicketShouldBeEmpty() {
        Ticket ticket = new Ticket();

    }

    @Test
    @DisplayName("Should set and get number correctly")
    void shouldSetAndGetNumber() {
        Ticket ticket = new Ticket();
        int row = 0, col = 0, value = 5;

        ticket.setNumber(row, col, value);

        Cell cell = ticket.getCell(row, col);
        assertThat(cell.getValue()).isEqualTo(value);
        assertThat(cell.getColumnIndex()).isEqualTo(col);
    }

    @Test
    @DisplayName("Should throw exception for invalid position")
    void shouldThrowExceptionForInvalidPosition() {
        Ticket ticket = new Ticket();

        assertThatThrownBy(() -> ticket.setNumber(-1, 0, 5))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> ticket.setNumber(0, -1, 5))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> ticket.setNumber(3, 0, 5))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> ticket.setNumber(0, 9, 5))
                .isInstanceOf(IllegalArgumentException.class);
    }
}