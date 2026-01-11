package org.example.logicgatesimulator.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.LogicElement;
import org.example.simulation.gates.AndGate;
import org.example.simulation.gates.OrGate;

public class OrGateComponent extends DraggableGate {
    private final Rectangle body;

    public OrGateComponent(String name, Workspace workspace) {
        super("or", null, workspace);
        this.logicGate = new OrGate();
        this.logicGate.name = name;

        this.body = new Rectangle(80, 50, Color.LIGHTGREY);
        this.body.setStroke(Color.BLACK);

        Text label = new Text("OR");
        this.getChildren().addAll(body, label);

        logicGate.setOnUpdate(event -> {
            updateUI();
        });
    }

    private void updateUI() {
        javafx.application.Platform.runLater(() -> {
            if (logicGate.getOutput()) {
                body.setFill(Color.YELLOW); // "Glowing" when ON
            } else {
                body.setFill(Color.LIGHTGREY);
            }
        });
    }

    public LogicElement getLogic() {
        return logicGate;
    }

}
