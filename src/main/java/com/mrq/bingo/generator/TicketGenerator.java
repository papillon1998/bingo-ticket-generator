// src/main/java/com/mrq/bingo/generator/TicketGenerator.java
package com.mrq.bingo.generator;

import com.mrq.bingo.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TicketGenerator {

    public Ticket generateTicket(NumberPool[] columnPools) {
        Ticket ticket = new Ticket();
        List<Integer>[] columns = new List[Ticket.COLS];

        for (int col = 0; col < Ticket.COLS; col++) {
            List<Integer> columnNumbers = getRandomNumbersForColumn(columnPools[col], col);
            columns[col] = columnNumbers;
        }

        fillTicketGrid(ticket, columns);
        return ticket;
    }

    private List<Integer> getRandomNumbersForColumn(NumberPool pool, int col) {
        List<Integer> numbers = new ArrayList<>();
        int maxNumbers = col == 0 || col == 8 ? 1 : 2;

        for (int i = 0; i < maxNumbers; i++) {
            pool.getNextAvailable(ColumnRange.forColumn(col)).ifPresent(number -> {
                pool.markUsed(number);
                numbers.add(number);
            });
        }
        return numbers;
    }

    private void fillTicketGrid(Ticket ticket, List<Integer>[] columns) {
        for (int col = 0; col < Ticket.COLS; col++) {
            List<Integer> columnValues = columns[col];
            int filledRows = 0;

            for (Integer value : columnValues) {
                for (int row = 0; row < Ticket.ROWS && filledRows < columnValues.size(); row++) {
                    if (ticket.getCell(row, col).isEmpty()) {
                        ticket.setNumber(row, col, value);
                        filledRows++;
                        break;
                    }
                }
            }
        }
    }

    public List<Strip> generateStrips(int numberOfStrips) {
        return IntStream.range(0, numberOfStrips)
                .parallel()
                .mapToObj(i -> generateStrip())
                .toList();
    }

    public Strip generateStrip() {
        Strip strip = new Strip();
        NumberPool[] pools = new NumberPool[Ticket.COLS];

        for (int i = 0; i < pools.length; i++) {
            ColumnRange range = ColumnRange.forColumn(i);
            pools[i] = new NumberPool(range.getStart(), range.getEnd());
        }

        for (int i = 0; i < Strip.TICKETS_PER_STRIP; i++) {
            strip.addTicket(generateTicket(pools));
        }
        return strip;
    }
}
