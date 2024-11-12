package com.mrq.bingo.generator;

import com.mrq.bingo.BingoStripPrinter;
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

        strip.tickets().forEach(ticket ->
                IntStream.range(0, ROWS).forEach(row -> {
                    long numbersInRow = Arrays.stream(ticket.getGrid()[row])
                            .filter(num -> num > 0)
                            .count();
                    assertEquals(NUMBERS_PER_ROW, numbersInRow,
                            String.format("Row %d does not have exactly %d numbers", row, NUMBERS_PER_ROW));
                })
        );
    }

    @Test
    @DisplayName("Each column should have 1-3 numbers")
    void shouldHaveOneToThreeNumbersPerColumn() {
        Strip strip = generator.generateStrip();

        strip.tickets().forEach(ticket ->
                IntStream.range(0, COLUMNS).forEach(col -> {
                    long numbersInColumn = getNumbersInColumn(ticket, col);
                    assertTrue(
                            numbersInColumn >= MIN_NUMBERS_PER_COLUMN &&
                                    numbersInColumn <= MAX_NUMBERS_PER_COLUMN,
                            String.format("Column %d has %d numbers, should be between %d and %d",
                                    col, numbersInColumn, MIN_NUMBERS_PER_COLUMN, MAX_NUMBERS_PER_COLUMN)
                    );
                })
        );
    }

    @Test
    @DisplayName("Numbers should be in correct column ranges")
    void shouldHaveNumbersInCorrectRanges() {
        Strip strip = generator.generateStrip();
        BingoStripPrinter.printStrip(strip);

        strip.tickets().forEach(ticket ->
                IntStream.range(0, COLUMNS).forEach(col -> {
                    int minValue = (col == 0) ? 1 : col * 10;
                    int maxValue = (col == COLUMNS - 1) ? TOTAL_NUMBERS : (col + 1) * 10 - 1;

                    IntStream.range(0, ROWS)
                            .map(row -> ticket.getGrid()[row][col])
                            .filter(number -> number > 0)
                            .forEach(number ->
                                    assertTrue(
                                            number >= minValue && number <= maxValue,
                                            String.format("Number %d in column %d is not in range [%d-%d]",
                                                    number, col, minValue, maxValue)
                                    )
                            );
                })
        );
    }

    @Test
    @DisplayName("Numbers in columns should be in ascending order")
    void shouldHaveNumbersInAscendingOrder() {
        Strip strip = generator.generateStrip();

        strip.tickets().forEach(ticket ->
                IntStream.range(0, COLUMNS).forEach(col -> {
                    int[] columnNumbers = getColumnNumbers(ticket, col);
                    IntStream.range(1, columnNumbers.length)
                            .forEach(i ->
                                    assertTrue(
                                            columnNumbers[i] > columnNumbers[i-1],
                                            String.format("Numbers in column %d are not in ascending order", col)
                                    )
                            );
                })
        );
    }

    @Test
    @DisplayName("Strip should use all numbers from 1-90 without duplicates")
    void shouldUseAllNumbersWithoutDuplicates() {
        Strip strip = generator.generateStrip();
        Set<Integer> usedNumbers = new HashSet<>();

        strip.tickets().stream()
                .map(Ticket::getGrid)
                .flatMap(Arrays::stream)
                .flatMapToInt(Arrays::stream)
                .filter(number -> number > 0)
                .forEach(number ->
                        assertTrue(usedNumbers.add(number),
                                String.format("Number %d appears more than once", number))
                );

        assertEquals(TOTAL_NUMBERS, usedNumbers.size(),
                "Not all numbers from 1-" + TOTAL_NUMBERS + " are used");
    }

    @Test
    @DisplayName("Strip generation should be performant")
    void shouldGenerateManyStripsQuickly() {
        long startTime = System.currentTimeMillis();
        int numStrips = 10000;

        IntStream.range(0, numStrips)
                .mapToObj(i -> generator.generateStrip())
                .forEach(this::verifyBasicRequirements);

        long duration = System.currentTimeMillis() - startTime;
        assertTrue(duration < 1000,
                String.format("Took %dms to generate %d strips", duration, numStrips));
    }

    private void verifyBasicRequirements(Strip strip) {
        assertEquals(TICKETS_PER_STRIP, strip.tickets().size());

        strip.tickets().forEach(ticket -> {
            // Verify row counts
            IntStream.range(0, ROWS).forEach(row ->
                    assertEquals(NUMBERS_PER_ROW,
                            Arrays.stream(ticket.getGrid()[row])
                                    .filter(n -> n > 0)
                                    .count())
            );

            // Verify column counts
            IntStream.range(0, COLUMNS).forEach(col -> {
                long count = getNumbersInColumn(ticket, col);
                assertTrue(count >= MIN_NUMBERS_PER_COLUMN &&
                        count <= MAX_NUMBERS_PER_COLUMN);
            });
        });
    }

    private long getNumbersInColumn(Ticket ticket, int col) {
        return IntStream.range(0, ROWS)
                .map(row -> ticket.getGrid()[row][col])
                .filter(num -> num > 0)
                .count();
    }

    private int[] getColumnNumbers(Ticket ticket, int col) {
        return IntStream.range(0, ROWS)
                .map(row -> ticket.getGrid()[row][col])
                .filter(num -> num > 0)
                .toArray();
    }
}