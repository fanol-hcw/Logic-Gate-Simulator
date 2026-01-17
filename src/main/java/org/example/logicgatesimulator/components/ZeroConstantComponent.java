package org.example.logicgatesimulator.components;

import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.inputs.ButtonElement;

public class ZeroConstantComponent extends DraggableGate {
    static int zeroCount = 0;

    public ZeroConstantComponent(String name, Workspace workspace) {
        super("ZeroConstantComponent", null, workspace);
        zeroCount++;
        this.logicGate = new ButtonElement();
        this.logicGate.name = "Zero " + zeroCount;

        // Konstante 0 - immer False
        ((ButtonElement)logicGate).setAndPush(false);

        // Leerer Callback - Wert bleibt konstant
        logicGate.setOnUpdate(isOn -> {
            // Konstante ändert sich nie visuell
        });
    }

    @Override
    protected int getInputPortCount() {
        return 0;
    }

    @Override
    protected int getOutputPortCount() {
        return 1;
    }

    @Override
    protected double getOutputPortXOffset() {
        return 6;
    }
}
