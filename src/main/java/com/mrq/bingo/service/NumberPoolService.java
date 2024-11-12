package com.mrq.bingo.service;


import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NumberPoolService {

    private static final int COLUMNS = 9;
    private static final int[][] COLUMN_RANGES = {
            {1, 9},
            {10, 19},
            {20, 29},
            {30, 39},
            {40, 49},
            {50, 59},
            {60, 69},
            {70, 79},
            {80, 90}
    };

    @Getter
    private final Map<Integer, List<Integer>> rangeMap = new HashMap<>();
    private final Random random = new Random();

    public void initializePool() {
        rangeMap.clear();
        for (int i = 0; i < COLUMNS; i++) {
            initializeColumnRange(i + 1, COLUMN_RANGES[i][0], COLUMN_RANGES[i][1]);
        }
    }

    private void initializeColumnRange(int column, int start, int end) {
        List<Integer> range = IntStream.rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(range, random);
        rangeMap.put(column, range);
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