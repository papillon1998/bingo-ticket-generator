package com.mrq.bingo;

import com.mrq.bingo.model.Strip;
import com.mrq.bingo.model.Ticket;

public class BingoStripPrinter {
    // ANSI colors for better visualization
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_GREEN = "\u001B[32m";

    public static void printStrip(Strip strip) {
        int ticketNumber = 1;
        for (Ticket ticket : strip.getTickets()) {
            printTicket(ticket, ticketNumber++);
            System.out.println(); // Space between tickets
        }
    }

    private static void printTicket(Ticket ticket, int ticketNumber) {
        // Print ticket header
        System.out.println(ANSI_GREEN + "=".repeat(45));
        System.out.println("Ticket #" + ticketNumber);
        System.out.println("=".repeat(45) + ANSI_RESET);

        // Print column headers
        System.out.println("   1-9  10-19 20-29 30-39 40-49 50-59 60-69 70-79 80-90");
        System.out.println("   ---  ----- ----- ----- ----- ----- ----- ----- -----");

        // Print ticket content
        int[][] grid = ticket.getGrid();
        for (int row = 0; row < 3; row++) {
            System.out.print(row + 1 + ") ");
            for (int col = 0; col < 9; col++) {
                String number = grid[row][col] == 0 ? "  " : String.format("%2d", grid[row][col]);
                System.out.print(ANSI_BLUE + number + ANSI_RESET + "    ");
            }
            System.out.println();
        }
    }
}

