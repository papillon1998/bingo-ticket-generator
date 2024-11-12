package com.mrq.bingo.service;


import com.mrq.bingo.model.Ticket;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketValidationService {
    private static final int ROWS = 3;
    private static final int COLUMNS = 9;

    public void validateAndFixColumns(List<Ticket> strip) {
        for (Ticket ticket : strip) {
            int invalidColumn = findInvalidColumn(ticket);
            if (invalidColumn != -1) {
                fixInvalidColumn(invalidColumn, ticket, strip);
            }
        }
    }

    public void sortAllColumns(List<Ticket> strip) {
        strip.forEach(this::sortTicketColumns);
    }

    private int findInvalidColumn(Ticket ticket) {
        for (int col = 0; col < COLUMNS; col++) {
            if (isEmptyColumn(ticket, col)) {
                return col;
            }
        }
        return -1;
    }

    private boolean isEmptyColumn(Ticket ticket, int col) {
        return IntStream.range(0, ROWS)
                .allMatch(row -> ticket.getGrid()[row][col] == 0);
    }

    private void fixInvalidColumn(int invalidColumn, Ticket invalidTicket, List<Ticket> strip) {
        for (Ticket ticket : strip) {
            if (tryFixColumn(invalidColumn, ticket, invalidTicket)) {
                break;
            }
        }
    }

    private boolean tryFixColumn(int invalidColumn, Ticket ticket, Ticket invalidTicket) {
        for (int row = 0; row < ROWS; row++) {
            if (canFixColumnAtRow(invalidColumn, row, ticket)) {
                performColumnFix(invalidColumn, row, ticket, invalidTicket);
                return true;
            }
        }
        return false;
    }

    private boolean canFixColumnAtRow(int invalidColumn, int row, Ticket ticket) {
        return ticket.getGrid()[row][invalidColumn] != 0 &&
                getNumbersInColumn(ticket, invalidColumn) > 1;
    }

    private void performColumnFix(int invalidColumn, int row, Ticket ticket, Ticket invalidTicket) {
        for (int col = 0; col < COLUMNS; col++) {
            if (canSwapNumbers(row, col, ticket, invalidTicket)) {
                swapNumbers(invalidColumn, row, col, ticket, invalidTicket);
                break;
            }
        }
    }

    private void sortTicketColumns(Ticket ticket) {
        for (int col = 0; col < COLUMNS; col++) {
            sortColumn(ticket, col);
        }
    }

    private void sortColumn(Ticket ticket, int col) {
        List<Integer> numbers = getColumnNumbers(col, ticket);
        if (numbers.size() > 1) {
            Collections.sort(numbers);
            int index = 0;
            for (int row = 0; row < ROWS; row++) {
                if (ticket.getGrid()[row][col] != 0) {
                    ticket.getGrid()[row][col] = numbers.get(index++);
                }
            }
        }
    }

    private List<Integer> getColumnNumbers(int col, Ticket ticket) {
        return IntStream.range(0, ROWS)
                .map(row -> ticket.getGrid()[row][col])
                .filter(num -> num > 0)
                .boxed()
                .collect(Collectors.toList());
    }

    private long getNumbersInColumn(Ticket ticket, int col) {
        return IntStream.range(0, ROWS)
                .map(row -> ticket.getGrid()[row][col])
                .filter(num -> num > 0)
                .count();
    }

    private boolean canSwapNumbers(int row, int col,
                                   Ticket ticket, Ticket invalidTicket) {
        return ticket.getGrid()[row][col] == 0 &&
                invalidTicket.getGrid()[row][col] != 0 &&
                getNumbersInColumn(invalidTicket, col) > 1;
    }

    private void swapNumbers(int invalidColumn, int row, int col,
                             Ticket ticket, Ticket invalidTicket) {
        invalidTicket.getGrid()[row][invalidColumn] = ticket.getGrid()[row][invalidColumn];
        ticket.getGrid()[row][invalidColumn] = 0;
        ticket.getGrid()[row][col] = invalidTicket.getGrid()[row][col];
        invalidTicket.getGrid()[row][col] = 0;
    }
}