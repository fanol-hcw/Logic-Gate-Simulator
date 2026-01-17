package org.example.logicgatesimulator.components;

import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.LogicElement;
import org.example.simulation.gates.XorGate;

public class XorGateComponent extends DraggableGate {

    //private final Rectangle body;

    public XorGateComponent(String name, Workspace workspace) {
        super("xor", null, workspace);
        this.logicGate = new XorGate();
        this.logicGate.name = name;
//
//        this.body = new Rectangle(80, 50, Color.LIGHTGREY);
//        this.body.setStroke(Color.BLACK);
//
//        Text label = new Text(name);
//        this.getChildren().addAll(body, label);
//
//        logicGate.setOnUpdate(event -> {
//            updateUI();
//        });
    }

//    private void updateUI() {
//        javafx.application.Platform.runLater(() -> {
//            if (logicGate.getOutput()) {
//                body.setFill(Color.YELLOW); // "Glowing" when ON
//            } else {
//                body.setFill(Color.LIGHTGREY);
//            }
//        });
//    }
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
    protected double getInputPortSpacing() { //definiert den Abstand zwischen den Verbindungspunkten auf der Eingangsseite
        return 25;
    }
}
