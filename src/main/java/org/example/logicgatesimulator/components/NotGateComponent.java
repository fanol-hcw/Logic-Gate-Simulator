package org.example.logicgatesimulator.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.LogicElement;
import org.example.simulation.gates.NotGate;
import org.example.simulation.gates.OrGate;

public class NotGateComponent extends DraggableGate {

    public NotGateComponent(String name, Workspace workspace) {
        super("not", null, workspace);
        this.logicGate = new NotGate();
        this.logicGate.name = name;


        logicGate.setOnUpdate(event -> {
            //visual feedback when interacting with element has to be added here
        });
    }

    public LogicElement getLogic() {
        return logicGate;
    }

}
