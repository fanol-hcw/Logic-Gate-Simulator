package org.example.logicgatesimulator.components;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.Cursor;
import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.inputs.ButtonElement;

public class ButtonComponent extends DraggableGate {
    private final ButtonElement logicButton;
    private final Circle toggleSwitch;

    public ButtonComponent(String name, Workspace workspace) {
        super("button", null, workspace);
        this.logicButton = new ButtonElement();
        this.logicButton.name = name;

        Text label = new Text(name);
        label.setY(-10);

        this.toggleSwitch = new Circle(20, Color.DARKRED);
        this.toggleSwitch.setStroke(Color.BLACK);
        this.toggleSwitch.setCursor(Cursor.HAND);
        // 3. Handle the click event
        this.toggleSwitch.setOnMouseClicked(e -> {
            System.out.println("In button 1");
            System.out.println(isWasControlDown());
            if(e.getButton() == MouseButton.PRIMARY){
                boolean currentState = logicButton.getOutput();
                toggle(!currentState);
            }

        });

        this.getChildren().addAll(label, toggleSwitch);
    }

    public void toggle(boolean isOn) {
        logicButton.setAndPush(isOn);

        toggleSwitch.setFill(isOn ? Color.LIME : Color.DARKRED);
    }

    public ButtonElement getLogic() {
        return logicButton;
    }
}