package com.mrq.bingo;

import com.mrq.bingo.generator.BingoStripGenerator;
import com.mrq.bingo.model.Strip;

public class BingoDemo {
    public static void main(String[] args) {
        BingoStripGenerator generator = new BingoStripGenerator();
        Strip strip = generator.generateStrip();

        BingoStripPrinter.printStrip(strip);
    }
}
