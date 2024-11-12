package com.mrq.bingo;

import com.mrq.bingo.generator.BingoStripGenerator;
import com.mrq.bingo.model.Strip;

public class Runner {

    public static void main(String[] args) {
        try {
            int numStrips = parseArgs(args);
            System.out.println("\nGenerating " + numStrips + " strip(s)...\n");

            long startTime = System.currentTimeMillis();

            for (int i = 0; i < numStrips; i++) {
                if (i > 0) {
                    System.out.println("\n" + "=".repeat(60) + "\n");
                }
                System.out.println("Strip Set #" + (i + 1));

                BingoStripGenerator generator = new BingoStripGenerator();
                Strip strip = generator.generateStrip();

                BingoStripPrinter.printStrip(strip);
            }

            long endTime = System.currentTimeMillis();
            double seconds = (endTime - startTime) / 1000.0;

            System.out.printf("%nGenerated %d strip(s) in %.3f seconds%n", numStrips, seconds);
            if (numStrips >= 10000) {
                System.out.printf("Average time per strip: %.3f ms%n", (seconds * 1000) / numStrips);
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static int parseArgs(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Please provide the number of strips to generate");
        }
        try {
            return Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + args[0]);
        }
    }

}