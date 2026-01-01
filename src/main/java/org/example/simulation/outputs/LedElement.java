package org.example.simulation.outputs;

import org.example.simulation.LogicElement;

public class LedElement extends LogicElement {
    public LedElement() { super(1); }

    @Override
    public void onInputChanged() {
        execute();
        if (this.output != this.newOutput) {
            this.output = this.newOutput;
            System.out.println("LED [" + name + "] is now: " + (output ? "ON (LIGHT)" : "OFF"));
        }
    }

    @Override public void execute() { newOutput = inputs[0].getOutput(); }
}