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
}
