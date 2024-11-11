// src/main/java/com/mrq/bingo/generator/NumberPool.java
package com.mrq.bingo.generator;

import com.mrq.bingo.model.ColumnRange;
import lombok.Getter;

import java.util.BitSet;
import java.util.OptionalInt;

@Getter
public class NumberPool {
    private final BitSet availableNumbers;
    private final int min;
    private final int max;

    public NumberPool(int min, int max) {
        this.min = min;
        this.max = max;
        this.availableNumbers = new BitSet(max - min + 1);
        availableNumbers.set(0, max - min + 1);
    }

    public boolean isAvailable(int number) {
        return availableNumbers.get(number - min);
    }

    public void markUsed(int number) {
        availableNumbers.clear(number - min);
    }

    public OptionalInt getNextAvailable(ColumnRange range) {
        for (int i = range.getStart(); i <= range.getEnd(); i++) {
            if (isAvailable(i)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty(); // Return empty if no numbers are available
    }
}
