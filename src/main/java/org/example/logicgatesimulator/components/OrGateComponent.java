package org.example.logicgatesimulator.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.LogicElement;
import org.example.simulation.gates.AndGate;
import org.example.simulation.gates.OrGate;

public class OrGateComponent extends DraggableGate {

    public OrGateComponent(String name, Workspace workspace) {
        super("or", null, workspace);
        this.logicGate = new OrGate();
        this.logicGate.name = name;

        logicGate.setOnUpdate(event -> {
            //visual feedback when interacting with element has to be added here
        });
    }

    public LogicElement getLogic() {
        return logicGate;
    }

    @Override
    protected int getInputPortCount() { //Anzahl der Eingänge auf der Eingangsseite
        return 2;
    }

    @Override
    protected double getInputPortXOffset() {
        return -6;
    }

    @Override
    protected double getInputPortSpacing() {//definiert den Abstand zwischen den Verbindungspunkten auf der Eingangsseite
        return 25;
    }

}
