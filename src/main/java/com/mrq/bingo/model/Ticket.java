package com.mrq.bingo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@RequiredArgsConstructor
public class Ticket {
    private final int[][] grid;
    private static final int ROWS = 3;
    private static final int COLS = 9;

    public Ticket() {
        this.grid = new int[ROWS][COLS];
    }

    public void placeNumber(int number, int col) {
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
}