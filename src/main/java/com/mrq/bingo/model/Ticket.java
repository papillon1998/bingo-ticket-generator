package com.mrq.bingo.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Stream;


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
        validatePosition(row, col);
        grid[row][col] = new Cell(value, col);
    }

    public Cell getCell(int row, int col) {
        validatePosition(row, col);
        return grid[row][col];
    }

    public Stream<Cell> streamCells() {
        return Arrays.stream(grid)
                .flatMap(Arrays::stream);
    }

    private void validatePosition(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            throw new IllegalArgumentException("Invalid position: row=" + row + ", col=" + col);
        }
    }
}