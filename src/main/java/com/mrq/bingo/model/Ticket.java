package com.mrq.bingo.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ticket {
    private final int[][] grid;
    private static final int ROWS = 3;
    private static final int COLS = 9;

    public Ticket() {
        this.grid = new int[ROWS][COLS];
    }

    public void placeNumber(int number, int col) {
        // Find first empty row in column
        for (int row = 0; row < ROWS; row++) {
            if (grid[row][col] == 0) {
                grid[row][col] = number;
                sortColumn(col);
                break;
            }
        }
    }

    private void sortColumn(int col) {
        int[] column = new int[ROWS];
        for (int i = 0; i < ROWS; i++) {
            column[i] = grid[i][col];
        }
        Arrays.sort(column);
        for (int i = 0; i < ROWS; i++) {
            grid[i][col] = column[i];
        }
    }

    public void removeNumber(int row, int col) {
        grid[row][col] = 0;
        sortColumn(col);
    }

    public List<Integer> getNumbersInColumn(int col) {
        return IntStream.range(0, ROWS)
                .map(row -> grid[row][col])
                .filter(num -> num > 0)
                .boxed()
                .collect(Collectors.toList());
    }

    public int getNumberCountInRow(int row) {
        return (int) IntStream.range(0, COLS)
                .map(col -> grid[row][col])
                .filter(num -> num > 0)
                .count();
    }

    public List<Integer> getEmptyColumnsInRow(int row) {
        return IntStream.range(0, COLS)
                .filter(col -> grid[row][col] == 0)
                .boxed()
                .collect(Collectors.toList());
    }

    public List<Integer> getFilledColumnsInRow(int row) {
        return IntStream.range(0, COLS)
                .filter(col -> grid[row][col] > 0)
                .boxed()
                .collect(Collectors.toList());
    }

    public int[][] getGrid() {
        return Arrays.stream(grid)
                .map(int[]::clone)
                .toArray(int[][]::new);
    }
}