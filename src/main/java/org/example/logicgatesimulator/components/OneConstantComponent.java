package org.example.logicgatesimulator.components;

import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.inputs.ButtonElement;

public class OneConstantComponent extends DraggableGate {
    static int oneCount = 0;

    public OneConstantComponent(String name, Workspace workspace) {
        super("OneConstantComponent", null, workspace);
        oneCount++;
        this.logicGate = new ButtonElement();
        this.logicGate.name = "One " + oneCount;

        // Konstante 1 - immer True
        ((ButtonElement)logicGate).setAndPush(true);

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
