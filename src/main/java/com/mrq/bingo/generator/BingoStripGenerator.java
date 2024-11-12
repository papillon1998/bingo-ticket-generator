package com.mrq.bingo.generator;

import com.mrq.bingo.model.Strip;
import com.mrq.bingo.model.Ticket;
import com.mrq.bingo.service.NumberPoolService;
import com.mrq.bingo.service.TicketFillService;
import com.mrq.bingo.service.TicketValidationService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class BingoStripGenerator {
    private static final int TICKETS_PER_STRIP = 6;

    private final NumberPoolService numberPoolService;
    private final TicketFillService ticketFillService;
    private final TicketValidationService validationService;

    public BingoStripGenerator() {
        this.numberPoolService = new NumberPoolService();
        this.ticketFillService = new TicketFillService(numberPoolService);
        this.validationService = new TicketValidationService();
    }

    public Strip generateStrip() {
        // Initialize number pool for new strip
        numberPoolService.initializePool();

        // Create empty tickets
        List<Ticket> strip = createEmptyTickets();

        // Fill tickets following the pattern
        ticketFillService.fillFirstRow(strip);
        ticketFillService.fillMiddleRow(strip);
        ticketFillService.fillLastRow(strip);

        // Handle remaining numbers
        ticketFillService.distributeRemainingNumbers(strip);

        // Validate and fix any issues
        validationService.validateAndFixColumns(strip);

        // Sort all columns
        validationService.sortAllColumns(strip);

        return new Strip(strip);
    }

    private List<Ticket> createEmptyTickets() {
        return IntStream.range(0, TICKETS_PER_STRIP)
                .mapToObj(i -> new Ticket())
                .collect(Collectors.toCollection(() -> new ArrayList<>(TICKETS_PER_STRIP)));
    }
}





