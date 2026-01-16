package org.example.logicgatesimulator.components;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.Runner;
import org.example.simulation.inputs.ButtonElement;

public class ClockComponent extends DraggableGate {
    private Timeline clockTimeline;

    public ClockComponent(String name, Workspace workspace) {
        super("clock", null, workspace);
        this.logicGate = new ButtonElement();
        this.logicGate.name = name;

        startClock();
    }

    private void startClock() {

    }

    public void stopClock() {

    }
}
