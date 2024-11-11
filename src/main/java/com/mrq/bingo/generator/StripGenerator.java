// src/main/java/com/mrq/bingo/generator/StripGenerator.java
package com.mrq.bingo.generator;

import com.mrq.bingo.model.ColumnRange;
import com.mrq.bingo.model.Strip;
import com.mrq.bingo.model.Ticket;

public class StripGenerator {

    public Strip generate() {
        Strip strip = new Strip();
        NumberPool[] columnPools = new NumberPool[Ticket.COLS];

        for (int col = 0; col < Ticket.COLS; col++) {
            ColumnRange range = ColumnRange.forColumn(col);
            columnPools[col] = new NumberPool(range.getStart(), range.getEnd());
        }

        TicketGenerator generator = new TicketGenerator();

        for (int i = 0; i < Strip.TICKETS_PER_STRIP; i++) {
            Ticket ticket = generator.generateTicket(columnPools);
            strip.addTicket(ticket);
        }
        return strip;
    }
}
