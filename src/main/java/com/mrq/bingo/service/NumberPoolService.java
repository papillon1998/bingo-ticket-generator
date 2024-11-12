package com.mrq.bingo.service;


import lombok.Getter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NumberPoolService {

    @Getter
    private final Map<Integer, List<Integer>> rangeMap = new HashMap<>();

    public void initializePool() {
        rangeMap.clear();
        initializeFirstColumn();
        initializeMiddleColumns();
        initializeLastColumn();
    }

    private void initializeFirstColumn() {
        List<Integer> firstRange = IntStream.range(1, 10)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(firstRange);
        rangeMap.put(1, firstRange);
    }

    private void initializeMiddleColumns() {
        IntStream.range(2, 9).forEach(rangeNumber -> {
            List<Integer> range = IntStream.range((rangeNumber-1)*10, (rangeNumber-1)*10 + 10)
                    .boxed()
                    .collect(Collectors.toList());
            Collections.shuffle(range);
            rangeMap.put(rangeNumber, range);
        });
    }

    private void initializeLastColumn() {
        List<Integer> lastRange = IntStream.range(80, 91)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(lastRange);
        rangeMap.put(9, lastRange);
    }

    public int getRandomNumberByRange(int rangeNumber) {
        List<Integer> numbers = rangeMap.get(rangeNumber);
        return numbers.isEmpty() ? 0 : numbers.remove(0);
    }

    public List<Integer> getRandomRangeNumbers(int count, List<Integer> exclude) {
        List<Integer> range = IntStream.range(1, 10)
                .filter(rangeNumber -> !exclude.contains(rangeNumber) && !rangeMap.get(rangeNumber).isEmpty())
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(range);
        return range.stream()
                .limit(count)
                .collect(Collectors.toList());
    }
}