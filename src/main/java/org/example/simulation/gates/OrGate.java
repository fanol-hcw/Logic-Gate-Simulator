package org.example.simulation.gates;

import org.example.simulation.LogicElement;

public class OrGate extends LogicElement {
    public OrGate() { super(2); }
    @Override
    public void execute() {
        newOutput = inputs[0].getOutput() || inputs[1].getOutput();
    }
}