package org.example.logicgatesimulator.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.Cursor;
import javafx.scene.text.TextAlignment;
import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.LogicElement;
import org.example.simulation.Runner;
import org.example.simulation.inputs.ButtonElement;

public class ButtonComponent extends DraggableGate {
    //private final ButtonElement logicButton;
    private final Circle toggleSwitch;
    static int buttonCount = 0;

    public ButtonComponent(String name, Workspace workspace) {
        super("button", null, workspace);
        buttonCount++;
        this.logicGate = new ButtonElement();
        //this.logicGate.name = name;
        this.logicGate.name = "Button " + buttonCount;
        Text label = new Text(this.logicGate.name);
        label.setTranslateY(60);
        label.setWrappingWidth(100);
        label.setTextAlignment(TextAlignment.CENTER);

        this.toggleSwitch = new Circle(20, Color.DARKRED);
        this.toggleSwitch.setStroke(Color.BLACK);
        this.toggleSwitch.setCursor(Cursor.HAND);
        // 3. Handle the click event
        this.toggleSwitch.setOnMouseClicked(e -> {
            System.out.println("In button 1");
            System.out.println(isWasControlDown());
            if(e.getButton() == MouseButton.PRIMARY){
                boolean currentState = logicGate.getOutput();
                toggle(!currentState);
            }

        });

        this.getChildren().addAll(toggleSwitch, label);
//        setAlignment(toggleSwitch, Pos.CENTER);
//        setAlignment(label, Pos.BOTTOM_CENTER);
//        setMargin(label, new Insets(0, 0, 5, 0));
    }

    public void toggle(boolean isOn) {
        ((ButtonElement)logicGate).setAndPush(isOn);

        toggleSwitch.setFill(isOn ? Color.LIME : Color.DARKRED);
        Runner.getInstance().step();
    }

    public LogicElement getLogic() {
        return logicGate;
    }
}