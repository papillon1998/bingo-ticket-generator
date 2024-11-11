package com.mrq.bingo.generator;

import com.mrq.bingo.model.Ticket;
import lombok.RequiredArgsConstructor;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class TicketGenerator {
    private static final int ROWS = 3;
    private static final int COLS = 9;
    private static final int NUMBERS_PER_ROW = 5;
    private static final int MAX_NUMBERS_PER_COL = 3;

    private final List<List<Integer>> columnPools;
    private final int[][] grid = new int[ROWS][COLS];
    private final boolean[][] used = new boolean[ROWS][COLS];

    public Ticket generateTicket() {
        // Step 1: Distribute numbers across columns
        int[] columnCounts = distributeNumbersToColumns();

        // Step 2: Fill columns with numbers
        fillColumns(columnCounts);

        // Step 3: Ensure row constraints
        balanceRows();

        // Step 4: Create ticket from grid
        return createTicket();
    }

    private int[] distributeNumbersToColumns() {
        int[] columnCounts = new int[COLS];
        int numbersToDistribute = NUMBERS_PER_ROW * ROWS;

        // First, ensure each column has at least one number
        for (int col = 0; col < COLS; col++) {
            if (!columnPools.get(col).isEmpty()) {
                columnCounts[col] = 1;
                numbersToDistribute--;
            }
        }

        // Distribute remaining numbers
        while (numbersToDistribute > 0) {
            int col = ThreadLocalRandom.current().nextInt(COLS);
            if (columnCounts[col] < MAX_NUMBERS_PER_COL &&
                    columnCounts[col] < columnPools.get(col).size()) {
                columnCounts[col]++;
                numbersToDistribute--;
            }
        }

        return columnCounts;
    }




    private void fillColumns(int[] columnCounts) {
        for (int col = 0; col < COLS; col++) {
            if (columnCounts[col] > 0) {
                List<Integer> numbers = new ArrayList<>();
                List<Integer> pool = columnPools.get(col);

                // Take required numbers from pool
                for (int i = 0; i < columnCounts[col]; i++) {
                    if (!pool.isEmpty()) {
                        numbers.add(pool.remove(pool.size() - 1));
                    }
                }

                // Sort numbers for this column
                Collections.sort(numbers);

                // Distribute numbers to rows
                List<Integer> availableRows = new ArrayList<>();
                for (int row = 0; row < ROWS; row++) {
                    availableRows.add(row);
                }
                Collections.shuffle(availableRows, ThreadLocalRandom.current());

                for (int i = 0; i < numbers.size(); i++) {
                    int row = availableRows.get(i);
                    grid[row][col] = numbers.get(i);
                    used[row][col] = true;
                }
            }
        }
    }

//    private void balanceRows() {
//        while (!areRowsBalanced()) {
//            for (int row = 0; row < ROWS; row++) {
//                int count = countNumbersInRow(row);
//                if (count > NUMBERS_PER_ROW) {
//                    removeNumberFromRow(row);
//                } else if (count < NUMBERS_PER_ROW) {
//                    addNumberToRow(row);
//                }
//            }
//        }
//    }
private void balanceRows() {
    for (int row = 0; row < ROWS; row++) {
        while (countNumbersInRow(row) < NUMBERS_PER_ROW) {
            addNumberToRow(row);
        }
    }
}

    private boolean areRowsBalanced() {
        for (int row = 0; row < ROWS; row++) {
            if (countNumbersInRow(row) != NUMBERS_PER_ROW) {
                return false;
            }
        }
        return true;
    }

    private int countNumbersInRow(int row) {
        int count = 0;
        for (int col = 0; col < COLS; col++) {
            if (used[row][col]) count++;
        }
        return count;
    }

    private void removeNumberFromRow(int row) {
        List<Integer> filledCols = new ArrayList<>();
        for (int col = 0; col < COLS; col++) {
            if (used[row][col]) {
                filledCols.add(col);
            }
        }

        if (!filledCols.isEmpty()) {
            int col = filledCols.get(ThreadLocalRandom.current().nextInt(filledCols.size()));
            columnPools.get(col).add(grid[row][col]);
            used[row][col] = false;
        }
    }

    private void addNumberToRow(int row) {
        List<Integer> availableCols = new ArrayList<>();
        for (int col = 0; col < COLS; col++) {
            if (!used[row][col] && !columnPools.get(col).isEmpty() &&
                    countNumbersInColumn(col) < MAX_NUMBERS_PER_COL) {
                availableCols.add(col);
            }
        }

        if (!availableCols.isEmpty()) {
            int col = availableCols.get(ThreadLocalRandom.current().nextInt(availableCols.size()));
            List<Integer> pool = columnPools.get(col);
            int number = pool.remove(pool.size() - 1);
            grid[row][col] = number;
            used[row][col] = true;
        }
    }

    private int countNumbersInColumn(int col) {
        int count = 0;
        for (int row = 0; row < ROWS; row++) {
            if (used[row][col]) count++;
        }
        return count;
    }

    private Ticket createTicket() {
        Ticket ticket = new Ticket();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (used[row][col]) {
                    ticket.setNumber(row, col, grid[row][col]);
                }
            }
        }
        return ticket;
    }
}