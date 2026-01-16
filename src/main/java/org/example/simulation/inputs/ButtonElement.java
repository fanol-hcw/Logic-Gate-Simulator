package org.example.simulation.inputs;

import org.example.simulation.LogicElement;
import org.example.simulation.Runner;

import java.util.ArrayList;
import java.util.Arrays;

public class ButtonElement extends LogicElement {

    public ButtonElement() {
        super(0); // Buttons represent the start of a circuit, so they have 0 inputs
    }

    public void setAndPush(boolean value) {
        if (this.output != value) {
            this.output = value;
            System.out.println("Button [" + name + "] set to: " + value);

            // Instead of calling onInputChanged directly, we tell the engine
            // to update everything connected to this button
            System.out.println(Arrays.toString(outputConnections.toArray()));
            Runner.getInstance().scheduleUpdates(outputConnections);
        }
    }

    @Override
    public boolean calculateNewOutput() {
        // Since there are no inputs, the output only changes via setAndPush
        return this.output;
    }
}