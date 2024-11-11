package com.mrq.bingo.model;

import lombok.Value;

@Value
public class Cell {
    Integer value; // nullable for empty cells
    int columnIndex;

    public boolean isEmpty() {
        return value == null;
    }
}
