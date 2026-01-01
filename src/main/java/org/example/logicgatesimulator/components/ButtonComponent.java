package org.example.logicgatesimulator.components;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.Cursor;
import org.example.simulation.inputs.ButtonElement;

public class ButtonComponent extends VBox {
    private final ButtonElement logicButton;
    private final Circle toggleSwitch;

    public ButtonComponent(String name) {
        this.logicButton = new ButtonElement();
        this.logicButton.name = name;

        Text label = new Text(name);

        this.toggleSwitch = new Circle(20, Color.DARKRED);
        this.toggleSwitch.setStroke(Color.BLACK);
        this.toggleSwitch.setCursor(Cursor.HAND);

        // 3. Handle the click event
        this.toggleSwitch.setOnMouseClicked(e -> {
            boolean currentState = logicButton.getOutput();
            toggle(!currentState);
        });

        this.getChildren().addAll(label, toggleSwitch);
        this.setSpacing(5);
    }

    public void toggle(boolean isOn) {
        logicButton.setAndPush(isOn);

        toggleSwitch.setFill(isOn ? Color.LIME : Color.DARKRED);
    }

    public ButtonElement getLogic() {
        return logicButton;
    }
}