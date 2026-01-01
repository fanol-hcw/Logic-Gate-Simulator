package org.example.simulation;

import java.util.HashSet;
import java.util.Set;

public class Runner extends Thread {
    private static Runner instance;
    private final Set<LogicElement> pendingUpdates = new HashSet<>();
    private boolean exit = false;

    public static Runner getInstance() {
        if (instance == null) instance = new Runner();
        return instance;
    }

    public void scheduleUpdate(LogicElement element) {
        synchronized(pendingUpdates) {
            pendingUpdates.add(element);
        }
    }

    @Override
    public void run() {
        System.out.println("Logic Engine Running...");
        while(!exit) {
            synchronized(pendingUpdates) {
                if (!pendingUpdates.isEmpty()) {
                    for (LogicElement element : pendingUpdates) {
                        element.onInputChanged();
                    }
                    pendingUpdates.clear();
                }
            }
            try {
                Thread.sleep(50); // High responsiveness
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public void stopThread() { exit = true; }
}
