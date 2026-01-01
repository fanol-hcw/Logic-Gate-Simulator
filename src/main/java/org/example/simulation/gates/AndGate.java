package org.example.simulation.gates;

import org.example.simulation.LogicElement;

public class AndGate extends LogicElement {
    public AndGate() { super(2); }
    @Override
    public void execute() {
        newOutput = inputs[0].getOutput() && inputs[1].getOutput();
    }
}