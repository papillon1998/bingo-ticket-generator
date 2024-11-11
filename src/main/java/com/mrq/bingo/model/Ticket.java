package com.mrq.bingo.model;

import lombok.Getter;

@Getter
public class Ticket {
    public static final int ROWS = 3;
    public static final int COLS = 9;
    public static final int NUMBERS_PER_ROW = 5;

    private final Cell[][] grid;

    public Ticket() {
        this.grid = new Cell[ROWS][COLS];
        initializeEmptyGrid();
    }

    private void initializeEmptyGrid() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = new Cell(null, col);
            }
        }
    }

    public void setNumber(int row, int col, Integer value) {
        grid[row][col] = new Cell(value, col);
    }

    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Cell cell = grid[row][col];
                sb.append(cell.isEmpty() ? "   " :
                        String.format("%3d", cell.getValue()));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}