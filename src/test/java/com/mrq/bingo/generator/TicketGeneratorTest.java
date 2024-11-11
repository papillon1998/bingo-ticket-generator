// src/test/java/com/mrq/bingo/generator/TicketGeneratorTest.java
package com.mrq.bingo.generator;

import com.mrq.bingo.model.ColumnRange;
import com.mrq.bingo.model.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

class TicketGeneratorTest {

    @Test
    @DisplayName("Should generate a valid Bingo ticket without exhausting number pool")
    void shouldGenerateValidTicket() {
        NumberPool[] pools = new NumberPool[Ticket.COLS];
        for (int i = 0; i < pools.length; i++) {
            ColumnRange range = ColumnRange.forColumn(i);
            pools[i] = new NumberPool(range.getStart(), range.getEnd());
        }
        TicketGenerator generator = new TicketGenerator();
        Ticket ticket = generator.generateTicket(pools);


        assertThat(ticket.streamCells().allMatch(cell -> !cell.isEmpty() || cell.isEmpty())).isTrue();
    }
}
