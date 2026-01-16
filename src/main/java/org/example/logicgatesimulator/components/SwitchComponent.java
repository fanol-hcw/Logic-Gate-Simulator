package org.example.logicgatesimulator.components;

import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.Runner;
import org.example.simulation.inputs.ButtonElement;

public class SwitchComponent extends DraggableGate {

    public SwitchComponent(String name, Workspace workspace) {
        super("switch", null, workspace);
        this.logicGate = new ButtonElement();
        this.logicGate.name = name;


        this.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.PRIMARY){
                boolean currentState = logicGate.getOutput();
                toggle(!currentState);
            }
        });
    }

    public void toggle(boolean isOn) {
        ((ButtonElement)logicGate).setAndPush(isOn);
        Runner.getInstance().step();
    }
}
