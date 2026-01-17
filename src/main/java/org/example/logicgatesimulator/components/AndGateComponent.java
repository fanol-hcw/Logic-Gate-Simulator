package org.example.logicgatesimulator.components;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.SimulatorUI;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.ILogicElement;
import org.example.simulation.LogicElement;
import org.example.simulation.gates.AndGate;

public class AndGateComponent extends DraggableGate {

    public AndGateComponent(String name, Workspace workspace) {
        super("and", null, workspace);
        this.logicGate = new AndGate();
        this.logicGate.name = name;

        logicGate.setOnUpdate(event -> {
            //visual feedback when interacting with element has to be added here
        });
    }

    public LogicElement getLogic() {
        return logicGate;
    }

    @Override
    protected int getInputPortCount() {//Anzahl der Eingänge auf der Eingangsseite
        return 2;
    }

    @Override
    protected double getInputPortXOffset() {
        return -6;
    }

    @Override
    protected double getInputPortSpacing() {//definiert den Abstand zwischen den Verbindungspunkten auf der Eingangsseite
        return 20;
    }

}
