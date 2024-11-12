package com.mrq.bingo.generator;

import com.mrq.bingo.model.Strip;
import com.mrq.bingo.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BingoStripGeneratorTest {
    private static final int ROWS = 3;
    private static final int COLUMNS = 9;
    private static final int TICKETS_PER_STRIP = 6;
    private static final int NUMBERS_PER_ROW = 5;
    private static final int MIN_NUMBERS_PER_COLUMN = 1;
    private static final int MAX_NUMBERS_PER_COLUMN = 3;
    private static final int TOTAL_NUMBERS = 90;

    private BingoStripGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new BingoStripGenerator();
    }

    @Test
    @DisplayName("Strip should contain exactly 6 tickets")
    void shouldContainSixTickets() {
        Strip strip = generator.generateStrip();
        assertEquals(TICKETS_PER_STRIP, strip.tickets().size(),
                "Strip must contain exactly " + TICKETS_PER_STRIP + " tickets");
    }

    @Test
    @DisplayName("Each row should contain exactly 5 numbers")
    void shouldHaveExactlyFiveNumbersPerRow() {
        Strip strip = generator.generateStrip();

        for (Ticket ticket : strip.tickets()) {
            int[][] grid = ticket.getGrid();
            for (int row = 0; row < ROWS; row++) {
                long numbersInRow = Arrays.stream(grid[row], 0, COLUMNS)
                        .filter(num -> num > 0)
                        .count();
                assertEquals(NUMBERS_PER_ROW, numbersInRow,
                        String.format("Row %d does not have exactly %d numbers", row, NUMBERS_PER_ROW));
            }
        }
    }

    @Test
    @DisplayName("Each column should have 1-3 numbers")
    void shouldHaveOneToThreeNumbersPerColumn() {
        Strip strip = generator.generateStrip();

        for (Ticket ticket : strip.tickets()) {
            int[][] grid = ticket.getGrid();
            for (int col = 0; col < COLUMNS; col++) {
                int finalCol = col;
                long numbersInColumn = IntStream.range(0, ROWS)
                        .map(row -> grid[row][finalCol])
                        .filter(num -> num > 0)
                        .count();
                assertTrue(
                        numbersInColumn >= MIN_NUMBERS_PER_COLUMN &&
                                numbersInColumn <= MAX_NUMBERS_PER_COLUMN,
                        String.format("Column %d has %d numbers, should be between %d and %d",
                                col, numbersInColumn, MIN_NUMBERS_PER_COLUMN, MAX_NUMBERS_PER_COLUMN)
                );
            }
        }
    }

    @Test
    @DisplayName("Numbers should be in correct column ranges")
    void shouldHaveNumbersInCorrectRanges() {
        Strip strip = generator.generateStrip();

        for (Ticket ticket : strip.tickets()) {
            int[][] grid = ticket.getGrid();
            for (int col = 0; col < COLUMNS; col++) {
                int minValue = (col == 0) ? 1 : col * 10;
                int maxValue = (col == COLUMNS - 1) ? TOTAL_NUMBERS : (col + 1) * 10 - 1;

                for (int row = 0; row < ROWS; row++) {
                    int number = grid[row][col];
                    if (number > 0) {
                        assertTrue(
                                number >= minValue && number <= maxValue,
                                String.format("Number %d in column %d is not in range [%d-%d]",
                                        number, col, minValue, maxValue)
                        );
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Numbers in columns should be in ascending order")
    void shouldHaveNumbersInAscendingOrder() {
        Strip strip = generator.generateStrip();

        for (Ticket ticket : strip.tickets()) {
            int[][] grid = ticket.getGrid();
            for (int col = 0; col < COLUMNS; col++) {
                int lastNumber = 0;
                for (int row = 0; row < ROWS; row++) {
                    int currentNumber = grid[row][col];
                    if (currentNumber > 0) {
                        assertTrue(currentNumber > lastNumber,
                                String.format("Numbers in column %d are not in ascending order", col));
                        lastNumber = currentNumber;
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Strip should use all numbers from 1-90 without duplicates")
    void shouldUseAllNumbersWithoutDuplicates() {
        Strip strip = generator.generateStrip();
        Set<Integer> usedNumbers = new HashSet<>();

        for (Ticket ticket : strip.tickets()) {
            int[][] grid = ticket.getGrid();
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLUMNS; col++) {
                    int number = grid[row][col];
                    if (number > 0) {
                        assertTrue(usedNumbers.add(number),
                                String.format("Number %d appears more than once", number));
                    }
                }
            }
        }

        assertEquals(TOTAL_NUMBERS, usedNumbers.size(),
                "Not all numbers from 1-" + TOTAL_NUMBERS + " are used");
    }

    @Test
    @DisplayName("Strip generation should be performant")
    void shouldGenerateManyStripsQuickly() {
        long startTime = System.currentTimeMillis();
        int numStrips = 10000;

        for (int i = 0; i < numStrips; i++) {
            Strip strip = generator.generateStrip();
            verifyBasicRequirements(strip);
        }

        long duration = System.currentTimeMillis() - startTime;
        assertTrue(duration < 1000,
                String.format("Took %dms to generate %d strips", duration, numStrips));
    }

    private void verifyBasicRequirements(Strip strip) {
        assertEquals(TICKETS_PER_STRIP, strip.tickets().size());

        for (Ticket ticket : strip.tickets()) {
            int[][] grid = ticket.getGrid();

            // Verify row counts
            for (int row = 0; row < ROWS; row++) {
                assertEquals(NUMBERS_PER_ROW, java.util.Arrays.stream(grid[row], 0, COLUMNS)
                        .filter(n -> n > 0)
                        .count());
            }

            // Verify column counts
            for (int col = 0; col < COLUMNS; col++) {
                int finalCol = col;
                long count = IntStream.range(0, ROWS)
                        .map(row -> grid[row][finalCol])
                        .filter(n -> n > 0)
                        .count();
                assertTrue(count >= MIN_NUMBERS_PER_COLUMN &&
                        count <= MAX_NUMBERS_PER_COLUMN);
            }
        }
    }
}
