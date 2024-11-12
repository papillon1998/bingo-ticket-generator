package com.mrq.bingo.service;


import com.mrq.bingo.model.Ticket;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class TicketFillService {
    private static final int ROWS = 3;
    private static final int COLUMNS = 9;
    private static final int NUMBERS_PER_ROW = 5;

    private final NumberPoolService numberPoolService;

    public void fillFirstRow(List<Ticket> strip) {
        strip.forEach(ticket -> {
            List<Integer> columns = numberPoolService.getRandomRangeNumbers(NUMBERS_PER_ROW, new ArrayList<>());
            fillRowWithColumns(ticket, 0, columns);
        });
    }

    public void fillMiddleRow(List<Ticket> strip) {
        strip.forEach(ticket -> {
            List<Integer> freeColumns = getFreeColumnsUntilRow(ticket.getGrid());
            List<Integer> columns = numberPoolService.getRandomRangeNumbers(
                    NUMBERS_PER_ROW - freeColumns.size(),
                    freeColumns
            );
            columns.addAll(freeColumns);
            fillRowWithColumns(ticket, 1, columns);
        });
    }

    public void fillLastRow(List<Ticket> strip) {
        strip.forEach(ticket -> {
            List<Integer> columns = numberPoolService.getRandomRangeNumbers(NUMBERS_PER_ROW, new ArrayList<>());
            fillRowWithColumns(ticket, 2, columns);
        });
    }

    public void distributeRemainingNumbers(List<Ticket> strip) {
        Map<Integer, List<Integer>> rangeMap = numberPoolService.getRangeMap();
        for (int i = 1; i < 10; i++) {
            if (!rangeMap.get(i).isEmpty()) {
                for (Integer value : rangeMap.get(i)) {
                    placeRemainingNumber(value, strip);
                }
            }
        }
    }

    private void fillRowWithColumns(Ticket ticket, int row, List<Integer> columns) {
        columns.forEach(column ->
                ticket.getGrid()[row][column-1] = numberPoolService.getRandomNumberByRange(column)
        );
    }

    private List<Integer> getFreeColumnsUntilRow(int[][] ticket) {
        List<Integer> freeColumns = new ArrayList<>();
        for (int i = 0; i < COLUMNS; i++) {
            if (isColumnFree(ticket, i)) {
                freeColumns.add(i + 1);
            }
        }
        return freeColumns;
    }

    private boolean isColumnFree(int[][] ticket, int column) {
        for (int row = 0; row < 1; row++) {
            if (ticket[row][column] != 0) {
                return false;
            }
        }
        return true;
    }

    private void placeRemainingNumber(int value, List<Ticket> strip) {
        int column = value/10 != 9 ? value/10 : 8;
        Ticket incompleteTicket = getFirstIncompleteTicket(strip);
        if (incompleteTicket == null) return;

        int incompleteRow = getIncompleteRow(incompleteTicket);

        for (Ticket ticket : strip) {
            if (tryPlaceNumber(value, column, ticket, incompleteTicket, incompleteRow)) {
                break;
            }
        }
    }

    private boolean tryPlaceNumber(int value, int column, Ticket ticket,
                                   Ticket incompleteTicket, int incompleteRow) {
        for (int row = 0; row < ROWS; row++) {
            int switchableColumn = getSwitchableColumn(row, ticket, incompleteTicket);
            if (ticket.getGrid()[row][column] == 0 && switchableColumn > -1) {
                performSwitch(value, column, row, switchableColumn, ticket, incompleteTicket, incompleteRow);
                return true;
            }
        }
        return false;
    }

    private void performSwitch(int value, int column, int row, int switchableColumn,
                               Ticket ticket, Ticket incompleteTicket, int incompleteRow) {
        ticket.getGrid()[row][column] = value;
        incompleteTicket.getGrid()[incompleteRow][switchableColumn] = ticket.getGrid()[row][switchableColumn];
        ticket.getGrid()[row][switchableColumn] = 0;
    }

    private Ticket getFirstIncompleteTicket(List<Ticket> strip) {
        return strip.stream()
                .filter(ticket -> getIncompleteRow(ticket) > -1)
                .findFirst()
                .orElse(null);
    }

    private int getIncompleteRow(Ticket ticket) {
        for (int row = 0; row < ROWS; row++) {
            if (getNumbersInRow(ticket, row) < NUMBERS_PER_ROW) {
                return row;
            }
        }
        return -1;
    }

    private int getNumbersInRow(Ticket ticket, int row) {
        return (int) Arrays.stream(ticket.getGrid()[row])
                .filter(num -> num != 0)
                .count();
    }

    private int getSwitchableColumn(int row, Ticket ticket, Ticket incompleteTicket) {
        int incompleteRow = getIncompleteRow(incompleteTicket);
        for (int col = 0; col < COLUMNS; col++) {
            if (isSwitchableColumn(row, col, ticket, incompleteTicket, incompleteRow)) {
                return col;
            }
        }
        return -1;
    }

    private boolean isSwitchableColumn(int row, int col, Ticket ticket,
                                       Ticket incompleteTicket, int incompleteRow) {
        return ticket.getGrid()[row][col] != 0 &&
                getNumbersInColumn(ticket, col) < 2 &&
                incompleteTicket.getGrid()[incompleteRow][col] == 0;
    }

    private long getNumbersInColumn(Ticket ticket, int col) {
        return IntStream.range(0, ROWS)
                .map(row -> ticket.getGrid()[row][col])
                .filter(num -> num == 0)
                .count();
    }
}