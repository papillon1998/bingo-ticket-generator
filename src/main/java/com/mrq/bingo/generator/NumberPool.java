package com.mrq.bingo.generator;

import com.mrq.bingo.model.ColumnRange;
import lombok.Getter;

import java.util.BitSet;

@Getter
public class NumberPool {
    private final BitSet availableNumbers;
    private final int minNumber;
    private final int maxNumber;

    public NumberPool(int minNumber, int maxNumber) {
        this.minNumber = minNumber;
        this.maxNumber = maxNumber;
        this.availableNumbers = new BitSet(maxNumber - minNumber + 1);
        availableNumbers.set(0, maxNumber - minNumber + 1);
    }

    public boolean isAvailable(int number) {
        validateNumber(number);
        return availableNumbers.get(number - minNumber);
    }

    public void markUsed(int number) {
        validateNumber(number);
        availableNumbers.clear(number - minNumber);
    }

    public void markAvailable(int number) {
        validateNumber(number);
        availableNumbers.set(number - minNumber);
    }

    public int getNextAvailable(ColumnRange range) {
        for (int i = range.getStart(); i <= range.getEnd(); i++) {
            if (isAvailable(i)) {
                return i;
            }
        }
        throw new IllegalStateException(
                String.format("No available numbers in range %d-%d",
                        range.getStart(), range.getEnd())
        );
    }

    private void validateNumber(int number) {
        if (number < minNumber || number > maxNumber) {
            throw new IllegalArgumentException(
                    String.format("Number %d is outside valid range %d-%d",
                            number, minNumber, maxNumber)
            );
        }
    }
}