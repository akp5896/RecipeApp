package com.example.recipeapp.Utils;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private double total = 0;
    private Random random;

    private static final Double DEFAULT_PROBABILITY = 0.5;

    public RandomCollection() {
        map.put(DEFAULT_PROBABILITY, null);
        random = new Random();
    }

    public void add(double weight, E result) {
        if (weight <= 0 || map.containsValue(result))
            return;
        total += weight;
        map.put(total, result);

    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.ceilingEntry(value).getValue();
    }
}