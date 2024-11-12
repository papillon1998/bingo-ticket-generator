package com.mrq.bingo.generator;

import com.mrq.bingo.model.Strip;
import com.mrq.bingo.model.Ticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BingoStripGenerator {
    private static final int ROW = 3;
    private static final int COLUMN = 9;
    Map<Integer, List<Integer>> rangeMap = new HashMap<>();


    public Strip generateStrip() {
        List<Ticket> strip = IntStream.range(0, 6)
                .mapToObj(i -> new Ticket())
                .collect(Collectors.toCollection(() -> new ArrayList<>(6)));

        this.setRangePerColumn();
        this.setRandomColumnValuesOnRow(0, strip);
        this.setValuesInFreeColumns(strip);
        this.setRandomColumnValuesOnRow(2, strip);

        if (!rangeMap.isEmpty()) {
            this.setRemainingNumbers(strip);
        }
        this.getFixForInvalidColumns(strip);
        this.sortColumns(strip);
        return new Strip(strip);
    }

    public void setRangePerColumn() {
        List<Integer> firstRange = IntStream.range(1, 10).boxed().collect(Collectors.toList());
        Collections.shuffle(firstRange);
        rangeMap.put(1, firstRange);
        IntStream.range(2,9).forEach(rangeNumber -> {
            List<Integer> range = IntStream.range((rangeNumber-1)*10, (rangeNumber-1)*10 + 10).boxed().collect(Collectors.toList());
            Collections.shuffle(range);

            rangeMap.put(rangeNumber, range);
        });
        List<Integer> lastRange = IntStream.range(80, 91).boxed().collect(Collectors.toList());
        Collections.shuffle(lastRange);
        rangeMap.put(9, lastRange);

    }

    private void setRandomColumnValuesOnRow(int row, List<Ticket> strip) {
        for (Ticket ticket: strip) {
            List<Integer> columns = this.getRandomRangeNumbers(5, new ArrayList<>());
            columns.forEach(column -> ticket.getGrid()[row][column-1] = this.getRandomNumberByRange(column));
        }
    }

    public int getRandomNumberByRange(int rangeNumber) {
        return rangeMap.get(rangeNumber).remove(0);
    }


    private void setValuesInFreeColumns(List<Ticket> strip) {
        for (Ticket ticket: strip) {
            List<Integer> freeColumns = this.getFreeColumnsUntilRow(ticket.getGrid(), 1);
            List<Integer> columns = this.getRandomRangeNumbers(5 - freeColumns.size(), freeColumns);
            columns.addAll(freeColumns);
            columns.forEach(column -> ticket.getGrid()[1][column-1] = this.getRandomNumberByRange(column));
        }
    }


    public List<Integer> getRandomRangeNumbers(int count, List<Integer> exclude) {
        List<Integer> range = IntStream.range(1, 10)
                .filter(rangeNumber -> !exclude.contains(rangeNumber) && !this.rangeMap.get(rangeNumber).isEmpty()).boxed().collect(Collectors.toList());
        Collections.shuffle(range);
        return range.stream().limit(count).collect(Collectors.toList());
    }


    public List<Integer> getFreeColumnsUntilRow(int[][] ticket, int row) {
        List<Integer> freeColumns = new ArrayList<>();
        boolean isFree;
        for (int i=0; i<COLUMN; i++) {
            isFree = true;
            for (int j=0; j<row; j++) {
                if (ticket[j][i] != 0) {
                    isFree = false;
                    break;
                }
            }
            if (isFree && !this.rangeMap.get(i+1).isEmpty()) {
                freeColumns.add(i+1);
            }
        }
        return freeColumns;
    }

    public void getFixForInvalidColumns(List<Ticket> strip) {

        for (Ticket invalidTicketCandidate:strip) {
            int invalidColumn = this.getInvalidColumn(invalidTicketCandidate);
            if (invalidColumn != -1) {
                for (Ticket ticket: strip) {
                    if (this.fixInvalidColumnEntry(invalidColumn, ticket, invalidTicketCandidate)) {
                        break;
                    }
                }
            }
        }
    }

    public void setRemainingNumbers(List<Ticket> strip) {
        for (int i=1; i<10; i++) {
            if (!rangeMap.get(i).isEmpty()) {
                for (Integer value: rangeMap.get(i)) {
                    this.placeRemainingNumber(value, strip);
                }
            }
        }
    }

    public boolean placeRemainingNumber(int value, List<Ticket> strip) {
        int column = value/10 != 9 ? value/10 : 8;
        Ticket incompleteTicket = this.getFirstIncompleteTicket(strip);
        int incompleteRow = this.getIncompleteRow(incompleteTicket);

        for (Ticket ticket:strip) {
            for (int i=0; i<ROW; i++) {
                int switchableColumn = this.getSwitchableColumn(i, ticket, incompleteTicket);
                if (ticket.getGrid()[i][column] == 0 && switchableColumn > -1) {
                    ticket.getGrid()[i][column] = value;
                    incompleteTicket.getGrid()[incompleteRow][switchableColumn] = ticket.getGrid()[i][switchableColumn];
                    ticket.getGrid()[i][switchableColumn] = 0;
                    return true;
                }
            }
        }
        return false;
    }

    public void sortColumns(List<Ticket> strip) {
        List<Integer> column;
        for (Ticket ticket:strip) {
            for (int i=0; i<COLUMN; i++) {
                column = this.getColumnList(i, ticket);
                if (Collections.frequency(column,0) < 2) {
                    int indexOfZero = column.indexOf(0);
                    if (indexOfZero!= -1) {
                        column.remove(indexOfZero);
                    }
                    Collections.sort(column);
                    if (indexOfZero!= -1) {
                        column.add(indexOfZero, 0);
                    }
                    for (int j=0; j<ROW; j++) {
                        ticket.getGrid()[j][i] = column.get(j);
                    }
                }

            }
        }
    }

    public int getInvalidColumn(Ticket ticket) {
        List<Integer> column;
        for (int i=0; i<COLUMN; i++) {
            column = this.getColumnList(i, ticket);

            if ( Collections.frequency(column, 0) == ROW) {
                return i;
            }
        }
        return -1;
    }


    public List<Integer> getColumnList(int column, Ticket ticket) {
        List<Integer> columnList = new ArrayList<>();
        for (int i=0; i<ROW; i++) {
            columnList.add(ticket.getGrid()[i][column]);
        }
        return columnList;
    }


    public boolean fixInvalidColumnEntry(int invalidColumn, Ticket ticket, Ticket invalidTicket) {
        List<Integer> columnList = this.getColumnList(invalidColumn, ticket);

        for (int i=0; i<ROW; i++) {
            if (ticket.getGrid()[i][invalidColumn] != 0 && Collections.frequency(columnList, 0) <2) {
                for (int j=0; j<COLUMN; j++) {
                    if (ticket.getGrid()[i][j] == 0 && invalidTicket.getGrid()[i][j] != 0
                            && Collections.frequency(this.getColumnList(j, invalidTicket), 0) < 2 ){

                        invalidTicket.getGrid()[i][invalidColumn] = ticket.getGrid()[i][invalidColumn];
                        ticket.getGrid()[i][invalidColumn] = 0;

                        ticket.getGrid()[i][j] = invalidTicket.getGrid()[i][j];
                        invalidTicket.getGrid()[i][j] = 0;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int getSwitchableColumn(int row, Ticket ticket, Ticket incompleteTicket) {
        int incompleteRow = this.getIncompleteRow(incompleteTicket);
        List<Integer> column;
        for (int i=0; i<COLUMN; i++) {
            column = new ArrayList<>();
            for (int j=0; j<ROW; j++) {
                column.add(ticket.getGrid()[j][i]);
            }
            if (ticket.getGrid()[row][i] != 0 && Collections.frequency(column, 0) <2 &&
                    incompleteTicket.getGrid()[incompleteRow][i] == 0) {
                return i;
            }
        }
        return -1;
    }
    public Ticket getFirstIncompleteTicket(List<Ticket> strip) {
        for (Ticket ticket:strip) {
            if (this.getIncompleteRow(ticket) > -1) {
                return ticket;
            }
        }
        return null;
    }

    public int getIncompleteRow(Ticket ticket) {
        int rowNumbers;
        for (int i=0; i<ROW; i++) {
            rowNumbers = 0;
            for (int j=0; j<COLUMN; j++) {
                if (ticket.getGrid()[i][j] != 0) {
                    rowNumbers++;
                }
            }
            if (rowNumbers < 5) {
                return i;
            }
        }
        return -1;
    }

}
