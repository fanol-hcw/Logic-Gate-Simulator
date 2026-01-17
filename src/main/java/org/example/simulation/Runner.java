package org.example.simulation;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Runner {
    private static Runner instance;
    private final Queue<LogicElement> updateQueue = new LinkedList<>();

    public static Runner getInstance() {
        if (instance == null) instance = new Runner();
        return instance;
    }

    public void scheduleUpdates(List<LogicElement> elements) {
        updateQueue.addAll(elements);
    }

    public void scheduleUpdate(LogicElement element) {
        updateQueue.add(element);
    }

    //

    /**
     * Process all pending changes until the circuit stabilizes.
     * Ends when all inputs are processed or when it has run 1000 times (to prevent infinite loops).
     */
    public void step() {
        int safetyLimit = 1000; // Prevents infinite loops in oscillating circuits
        int count = 0;
        System.out.println("Steps are running" );
        while (!updateQueue.isEmpty() && count < safetyLimit) {
            LogicElement element = updateQueue.poll();
            element.onInputChanged();
            count++;
        }
    }
}