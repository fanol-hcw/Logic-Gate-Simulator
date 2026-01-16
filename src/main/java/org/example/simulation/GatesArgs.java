package org.example.simulation;

public class GatesArgs {

    private boolean newValue;
    private boolean oldValue;

    public GatesArgs(boolean newValue, boolean oldValue) {
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    public boolean getNewValue() {
        return newValue;
    }

    public boolean getOldValue() {
        return oldValue;
    }
}
