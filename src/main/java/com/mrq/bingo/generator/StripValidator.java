package com.mrq.bingo.generator;


import com.mrq.bingo.model.Cell;
import com.mrq.bingo.model.Strip;
import com.mrq.bingo.model.Ticket;

public class StripValidator {

    public boolean isValid(Strip strip) {
        return hasCorrectNumberOfTickets(strip) &&
                hasUniqueNumbers(strip) &&
                allTicketsAreValid(strip);
    }

    private boolean hasCorrectNumberOfTickets(Strip strip) {
        return strip.getTickets().size() == Strip.TICKETS_PER_STRIP;
    }

    private boolean hasUniqueNumbers(Strip strip) {
        return strip.getAllNumbers().size() == 90;
    }

    private boolean allTicketsAreValid(Strip strip) {
        return strip.getTickets().stream().allMatch(this::isTicketValid);
    }

    private boolean isTicketValid(Ticket ticket) {
        return hasCorrectNumbersPerRow(ticket) &&
                hasValidColumnOrder(ticket) &&
                hasValidColumnRanges(ticket);
    }

    private boolean hasCorrectNumbersPerRow(Ticket ticket) {
        for (int row = 0; row < Ticket.ROWS; row++) {
            int count = 0;
            for (int col = 0; col < Ticket.COLS; col++) {
                if (!ticket.getCell(row, col).isEmpty()) {
                    count++;
                }
            }
            if (count != Ticket.NUMBERS_PER_ROW) {
                return false;
            }
        }
        return true;
    }

    private boolean hasValidColumnOrder(Ticket ticket) {
        for (int col = 0; col < Ticket.COLS; col++) {
            Integer previousNumber = null;
            for (int row = 0; row < Ticket.ROWS; row++) {
                Cell cell = ticket.getCell(row, col);
                if (!cell.isEmpty()) {
                    if (previousNumber != null && cell.getValue() <= previousNumber) {
                        return false;
                    }
                    previousNumber = cell.getValue();
                }
            }
        }
        return true;
    }

    private boolean hasValidColumnRanges(Ticket ticket) {
        for (int col = 0; col < Ticket.COLS; col++) {
            for (int row = 0; row < Ticket.ROWS; row++) {
                Cell cell = ticket.getCell(row, col);
                if (!cell.isEmpty()) {
                    int value = cell.getValue();
                    if (!isInValidRange(value, col)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isInValidRange(int number, int column) {
        int start = column == 0 ? 1 : column * 10;
        int end = column == 8 ? 90 : (column + 1) * 10 - 1;
        return number >= start && number <= end;
    }
}