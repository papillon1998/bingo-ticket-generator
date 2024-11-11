package com.mrq.bingo.generator;

import static org.junit.jupiter.api.Assertions.*;


import com.mrq.bingo.model.Strip;
import com.mrq.bingo.model.Ticket;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class BingoStripGeneratorTest {
    private final BingoStripGenerator generator = new BingoStripGenerator();

    @Test
    void shouldGenerateStripWithSixTickets() {
        Strip strip = generator.generateStrip();
        assertEquals(6, strip.getTickets().size());
    }

    @Test
    void shouldHaveCorrectNumbersPerRow() {
        Strip strip = generator.generateStrip();
        for (Ticket ticket : strip.getTickets()) {
            for (int row = 0; row < 3; row++) {
                assertEquals(5, ticket.getNumberCountInRow(row));
            }
        }
    }

    @Test
    void shouldHaveNumbersInCorrectRanges() {
        Strip strip = generator.generateStrip();
        for (Ticket ticket : strip.getTickets()) {
            int[][] grid = ticket.getGrid();
            for (int col = 0; col < 9; col++) {
                for (int row = 0; row < 3; row++) {
                    int number = grid[row][col];
                    if (number > 0) {
                        int minValue = col * 10 + 1;
                        int maxValue = col == 8 ? 90 : (col + 1) * 10;
                        assertTrue(number >= minValue && number <= maxValue);
                    }
                }
            }
        }
    }

    @Test
    void shouldHaveOrderedColumnsInTickets() {
        Strip strip = generator.generateStrip();
        for (Ticket ticket : strip.getTickets()) {
            for (int col = 0; col < 9; col++) {
                List<Integer> numbers = ticket.getNumbersInColumn(col);
                List<Integer> sortedNumbers = new ArrayList<>(numbers);
                Collections.sort(sortedNumbers);
                assertEquals(sortedNumbers, numbers);
            }
        }
    }

    @Test
    void shouldHaveNoDuplicateNumbers() {
        Strip strip = generator.generateStrip();
        Set<Integer> usedNumbers = new HashSet<>();

        for (Ticket ticket : strip.getTickets()) {
            int[][] grid = ticket.getGrid();
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 9; col++) {
                    int number = grid[row][col];
                    if (number > 0) {
                        assertTrue(usedNumbers.add(number),
                                "Found duplicate number: " + number);
                    }
                }
            }
        }
    }

    @Test
    void shouldPerformEfficiently() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            generator.generateStrip();
        }
        long duration = System.currentTimeMillis() - startTime;
        assertTrue(duration < 1000,
                "Generated 10k strips in " + duration + "ms");
    }
}