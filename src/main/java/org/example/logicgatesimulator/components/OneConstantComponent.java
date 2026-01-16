package org.example.logicgatesimulator.components;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.inputs.ButtonElement;

public class OneConstantComponent extends DraggableGate {

    public OneConstantComponent(String name, Workspace workspace) {
        super("one", null, workspace);
        this.logicGate = new ButtonElement();
        this.logicGate.name = name;
        ((ButtonElement)logicGate).setAndPush(true);



    }
}
