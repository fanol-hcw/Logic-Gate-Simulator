package org.example.simulation.gates;

import org.example.simulation.LogicElement;

public class NotGate extends LogicElement {
    public NotGate() { super(1); }
    @Override
    public void execute() {
        newOutput = !inputs[0].getOutput();
    }
}