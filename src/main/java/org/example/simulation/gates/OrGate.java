package org.example.simulation.gates;

import org.example.simulation.LogicElement;

public class OrGate extends LogicElement {
    public OrGate() { super(2); }

    @Override
    public boolean calculateNewOutput() {
        return false;
    }
}