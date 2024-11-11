package com.mrq.bingo;

import com.mrq.bingo.generator.BingoStripGenerator;
import com.mrq.bingo.model.Strip;
import com.mrq.bingo.model.Ticket;

import java.util.HashSet;
import java.util.Set;

// Main class for demonstration
public class BingoDemo {
    public static void main(String[] args) {
        BingoStripGenerator generator = new BingoStripGenerator();
        Strip strip = generator.generateStrip();

        // Print the generated strip
        BingoStripPrinter.printStrip(strip);

        // Optional: Print statistics
        printStripStatistics(strip);
    }

    private static void printStripStatistics(Strip strip) {
        System.out.println("\nStrip Statistics:");
        System.out.println("=================");

        // Count total numbers used
        Set<Integer> uniqueNumbers = new HashSet<>();
        for (Ticket ticket : strip.getTickets()) {
            int[][] grid = ticket.getGrid();
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 9; col++) {
                    if (grid[row][col] > 0) {
                        uniqueNumbers.add(grid[row][col]);
                    }
                }
            }
        }

        System.out.println("Total unique numbers: " + uniqueNumbers.size());
        System.out.println("Numbers per column distribution:");

        // Show distribution per column across all tickets
        for (int col = 0; col < 9; col++) {
            int colCount = 0;
            for (Ticket ticket : strip.getTickets()) {
                colCount += ticket.getNumbersInColumn(col).size();
            }
            System.out.printf("Column %d: %d numbers\n", col + 1, colCount);
        }
    }
}
