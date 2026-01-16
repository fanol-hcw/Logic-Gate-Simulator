package org.example.simulation.gates;

import org.example.simulation.LogicElement;

public class XorGate extends LogicElement {
    public XorGate() {
        super(2);
    }

    @Override
    public boolean calculateNewOutput() {
        return getInputSafe(0) ^ getInputSafe(1);
    }
}
