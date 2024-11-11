package com.mrq.bingo.model;


import lombok.Value;

@Value
public class ColumnRange {
    int start;
    int end;

    public static ColumnRange forColumn(int columnIndex) {
        if (columnIndex < 0 || columnIndex > 8) {
            throw new IllegalArgumentException("Column index must be between 0 and 8");
        }

        if (columnIndex == 0) return new ColumnRange(1, 9);
        if (columnIndex == 8) return new ColumnRange(80, 90);
        return new ColumnRange(columnIndex * 10, (columnIndex * 10) + 9);
    }

    public boolean contains(int number) {
        return number >= start && number <= end;
    }
}
