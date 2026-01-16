package org.example.simulation.gates;

import org.example.simulation.LogicElement;

public class NotGate extends LogicElement {
    public NotGate() { super(1); }
    @Override
    public boolean calculateNewOutput() {
        return !getInputSafe(0);
    }
}