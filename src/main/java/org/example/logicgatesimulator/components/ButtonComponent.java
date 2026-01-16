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
    static int buttonCount = 0;

    public ButtonComponent(String name, Workspace workspace) {
        super("button", null, workspace);
        buttonCount++;
        this.logicGate = new ButtonElement();

        //this.logicGate.name = name;
        this.logicGate.name = "Button " + buttonCount;


    }



    public LogicElement getLogic() {
        return logicGate;
    }
}