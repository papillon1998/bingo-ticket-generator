package com.mrq.bingo.generator;

import com.mrq.bingo.model.Strip;
import com.mrq.bingo.model.Ticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BingoStripGenerator {
    private static final int TICKETS_PER_STRIP = 6;
    private static final int ROWS_PER_TICKET = 3;
    private static final int COLS_PER_TICKET = 9;
    private static final int NUMBERS_PER_ROW = 5;

    private final Random random;

    public BingoStripGenerator() {
        this.random = new Random();
    }


    BingoStripGenerator(Random random) {
        this.random = random;
    }

    public Strip generateStrip() {
        List<Ticket> tickets = new ArrayList<>(TICKETS_PER_STRIP);
        Set<Integer> usedNumbers = new HashSet<>();

        // Initialize tickets
        for (int i = 0; i < TICKETS_PER_STRIP; i++) {
            tickets.add(new Ticket());
        }

        // Distribute numbers column by column across all tickets
        for (int col = 0; col < COLS_PER_TICKET; col++) {
            distributeColumnNumbers(tickets, col, usedNumbers);
        }

        // Ensure each ticket row has exactly 5 numbers
        tickets.forEach(this::balanceTicketRows);

        return new Strip(tickets);
    }

    private void distributeColumnNumbers(List<Ticket> tickets, int col, Set<Integer> usedNumbers) {
        List<Integer> availableNumbers = generateNumbersForColumn(col);
        availableNumbers.removeAll(usedNumbers);

        // Distribute numbers across tickets for this column
        for (Ticket ticket : tickets) {
            int numbersToPlace = calculateNumbersForColumn();
            placeNumbersInColumn(ticket, col, availableNumbers, numbersToPlace);
            availableNumbers.removeAll(ticket.getNumbersInColumn(col));
        }
    }

    private List<Integer> generateNumbersForColumn(int col) {
        int min = col * 10 + 1;
        int max = (col == 8) ? 90 : (col + 1) * 10;
        return IntStream.rangeClosed(min, max)
                .boxed()
                .collect(Collectors.toList());
    }

    private int calculateNumbersForColumn() {
        // Ensure 1-3 numbers per column
        return random.nextInt(3) + 1;
    }

    private void placeNumbersInColumn(Ticket ticket, int col, List<Integer> numbers, int count) {
        Collections.sort(numbers);
        for (int i = 0; i < count && i < numbers.size(); i++) {
            ticket.placeNumber(numbers.get(i), col);
        }
    }

    private void balanceTicketRows(Ticket ticket) {
        for (int row = 0; row < ROWS_PER_TICKET; row++) {
            balanceRow(ticket, row);
        }
    }

    private void balanceRow(Ticket ticket, int row) {
        int numbersInRow = ticket.getNumberCountInRow(row);

        while (numbersInRow != NUMBERS_PER_ROW) {
            if (numbersInRow < NUMBERS_PER_ROW) {
                addNumberToRow(ticket, row);
                numbersInRow++;
            } else {
                removeNumberFromRow(ticket, row);
                numbersInRow--;
            }
        }
    }

    private void addNumberToRow(Ticket ticket, int row) {
        List<Integer> emptyCols = ticket.getEmptyColumnsInRow(row);
        if (!emptyCols.isEmpty()) {
            int col = emptyCols.get(random.nextInt(emptyCols.size()));
            List<Integer> availableNumbers = generateNumbersForColumn(col);
            availableNumbers.removeAll(ticket.getNumbersInColumn(col));
            if (!availableNumbers.isEmpty()) {
                ticket.placeNumber(availableNumbers.get(0), col);
            }
        }
    }

    private void removeNumberFromRow(Ticket ticket, int row) {
        List<Integer> filledCols = ticket.getFilledColumnsInRow(row);
        if (!filledCols.isEmpty()) {
            int col = filledCols.get(random.nextInt(filledCols.size()));
            ticket.removeNumber(row, col);
        }
    }
}



