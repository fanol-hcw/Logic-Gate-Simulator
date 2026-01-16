package org.example.simulation.outputs;

import org.example.simulation.LogicElement;

public class LedElement extends LogicElement {

    public LedElement() {
        super(1); // LEDs have exactly one input
    }

    @Override
    public boolean calculateNewOutput() {
        // The LED's state is simply whatever its input is
        return getInputSafe(0);
    }

    @Override
    public void onInputChanged() {
        boolean oldState = this.output;

        // Use the base LogicElement logic to update state and schedule children
        super.onInputChanged();

        // If the state actually changed, print the visual update to the console
        if (this.output != oldState) {
            System.out.println("LED [" + name + "] is now: " + (output ? "ON (亮)" : "OFF"));
        }
    }
}