package com.mrq.bingo.generator;



import com.mrq.bingo.model.Strip;
import com.mrq.bingo.model.Ticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StripGenerator {
    private static final int TOTAL_NUMBERS = 90;
    private static final int COLS = 9;
    private static final int[] COLUMN_RANGES = {9, 10, 10, 10, 10, 10, 10, 10, 11};

    public Strip generate() {
        Strip strip = new Strip();
        Map<Integer, List<Integer>> columnNumbers = initializeColumnNumbers();

        for (int i = 0; i < Strip.TICKETS_PER_STRIP; i++) {
            Ticket ticket = generateTicket(columnNumbers);
            strip.addTicket(ticket);
        }

        return strip;
    }

    private Map<Integer, List<Integer>> initializeColumnNumbers() {
        Map<Integer, List<Integer>> columnNumbers = new HashMap<>();
        int start = 1;

        for (int col = 0; col < COLS; col++) {
            List<Integer> numbers = new ArrayList<>();
            int range = COLUMN_RANGES[col];

            for (int i = 0; i < range; i++) {
                numbers.add(start + i);
            }
            Collections.shuffle(numbers);
            columnNumbers.put(col, numbers);

            start += range;
        }

        return columnNumbers;
    }

    private Ticket generateTicket(Map<Integer, List<Integer>> columnNumbers) {
        Ticket ticket = new Ticket();
        int[] rowCounts = new int[Ticket.ROWS];
        boolean[][] used = new boolean[Ticket.ROWS][Ticket.COLS];

        // First pass: distribute one number per column where possible
        distributeInitialNumbers(ticket, columnNumbers, rowCounts, used);

        // Second pass: fill remaining numbers to complete rows
        fillRemainingNumbers(ticket, columnNumbers, rowCounts, used);

        return ticket;
    }

    private void distributeInitialNumbers(Ticket ticket, Map<Integer, List<Integer>> columnNumbers,
                                          int[] rowCounts, boolean[][] used) {
        for (int col = 0; col < Ticket.COLS; col++) {
            List<Integer> numbers = columnNumbers.get(col);
            if (!numbers.isEmpty()) {
                int row = findBestRow(rowCounts);
                int number = numbers.remove(numbers.size() - 1);
                ticket.setNumber(row, col, number);
                used[row][col] = true;
                rowCounts[row]++;
            }
        }
    }

    private void fillRemainingNumbers(Ticket ticket, Map<Integer, List<Integer>> columnNumbers,
                                      int[] rowCounts, boolean[][] used) {
        for (int row = 0; row < Ticket.ROWS; row++) {
            while (rowCounts[row] < Ticket.NUMBERS_PER_ROW) {
                int col = findAvailableColumn(columnNumbers, used, row);
                if (col != -1) {
                    List<Integer> numbers = columnNumbers.get(col);
                    int number = numbers.remove(numbers.size() - 1);
                    ticket.setNumber(row, col, number);
                    used[row][col] = true;
                    rowCounts[row]++;
                }
            }
        }
    }

    private int findBestRow(int[] rowCounts) {
        int minCount = Integer.MAX_VALUE;
        int bestRow = 0;

        for (int row = 0; row < Ticket.ROWS; row++) {
            if (rowCounts[row] < minCount) {
                minCount = rowCounts[row];
                bestRow = row;
            }
        }

        return bestRow;
    }

    private int findAvailableColumn(Map<Integer, List<Integer>> columnNumbers,
                                    boolean[][] used, int row) {
        List<Integer> availableCols = new ArrayList<>();

        for (int col = 0; col < Ticket.COLS; col++) {
            if (!used[row][col] && !columnNumbers.get(col).isEmpty() &&
                    countNumbersInColumn(used, col) < 3) {
                availableCols.add(col);
            }
        }

        if (availableCols.isEmpty()) {
            return -1;
        }

        return availableCols.get(new Random().nextInt(availableCols.size()));
    }

    private int countNumbersInColumn(boolean[][] used, int col) {
        int count = 0;
        for (int row = 0; row < Ticket.ROWS; row++) {
            if (used[row][col]) count++;
        }
        return count;
    }
}
