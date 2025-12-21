package org.example.logicgatesimulator;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class LogicWire extends Line {
    private DraggableGate endGate; // The gate receiving the signal
    private boolean state = false; // on (true) or off (false)

    public LogicWire(DraggableGate startGate, Circle startPort, DraggableGate endGate, Circle endPort) {
        this.endGate = endGate;

        // Style
        this.setStrokeWidth(3);
        this.setStroke(Color.BLACK);

        // Bind positions to the gates so the wire moves with them
        this.startXProperty().bind(startGate.layoutXProperty().add(startGate.width / 2).add(startPort.getTranslateX()));
        this.startYProperty().bind(startGate.layoutYProperty().add(startGate.height / 2).add(startPort.getTranslateY()));
        this.endXProperty().bind(endGate.layoutXProperty().add(endGate.width / 2).add(endPort.getTranslateX()));
        this.endYProperty().bind(endGate.layoutYProperty().add(endGate.height / 2).add(endPort.getTranslateY()));
    }

    // Turn the wire Red or Black and tell the next gate to update
    public void setSignal(boolean on) {
        this.state = on;
        this.setStroke(on ? Color.LIME : Color.BLACK);
        if (on) {
            this.setEffect(new javafx.scene.effect.DropShadow(10, Color.LIME));
        } else {
            this.setEffect(null);
        }
        endGate.calculateLogic(); // <--- This triggers the chain reaction!
    }

    public boolean getState() {
        return state;
    }
}
