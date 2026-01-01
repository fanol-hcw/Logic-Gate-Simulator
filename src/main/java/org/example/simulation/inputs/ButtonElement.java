package org.example.simulation.inputs;

import org.example.simulation.LogicElement;

public class ButtonElement extends LogicElement {
    public ButtonElement() { super(0); }

    public void setAndPush(boolean value) {
        if (this.output != value) {
            this.newOutput = value;
            this.onInputChanged();
        }
    }

    @Override
    public void execute() {

    }
}