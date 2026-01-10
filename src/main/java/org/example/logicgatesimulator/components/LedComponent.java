package org.example.logicgatesimulator.components;

import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.inputs.ButtonElement;
import org.example.simulation.outputs.LedElement;

public class LedComponent extends DraggableGate {

    private final Circle toggleSwitch;

    public LedComponent(String name, Workspace workspace) {
        super(name, null, workspace);
        System.out.println("Adding " + this);
        this.logicGate = new LedElement();
        this.logicGate.name = "Led";
        Text label = new Text(this.logicGate.name);
        label.setTranslateY(60);
        label.setWrappingWidth(100);
        label.setTextAlignment(TextAlignment.CENTER);

        this.toggleSwitch = new Circle(20, Color.DARKRED);
        this.toggleSwitch.setStroke(Color.BLACK);
        this.toggleSwitch.setCursor(Cursor.HAND);

        this.getChildren().addAll(toggleSwitch, label);

        logicGate.setOnUpdate(isOn -> {
            toggleSwitch.setFill(isOn ? Color.LIME : Color.DARKRED);
        });

    }


}
