package org.example.logicgatesimulator.components;

import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.example.logicgatesimulator.ComponentRegistry;
import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.ImageLoader;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.Runner;
import org.example.simulation.inputs.ButtonElement;

public class SwitchComponent extends DraggableGate {
    static int switchCount = 0;

    public SwitchComponent(String name, Workspace workspace) {
        super("SwitchComponent", null, workspace);
        switchCount++;
        this.logicGate = new ButtonElement();
        this.logicGate.name = "Switch " + switchCount;

        updateImageByState();

        logicGate.setOnUpdate(isOn -> {
            updateImageByState();
        });

        addOnMousePressedEvent(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                boolean currentState = logicGate.getOutput();
                ((ButtonElement)logicGate).setAndPush(!currentState);
                Runner.getInstance().step();
            }
        });
    }

    @Override
    protected int getInputPortCount() {
        return 0;
    }

    @Override
    protected int getOutputPortCount() {
        return 1;
    }

    @Override
    protected double getOutputPortXOffset() {
        return 6;
    }

    private void updateImageByState() {
        javafx.application.Platform.runLater(() -> {
            ComponentRegistry.ComponentMetadata metadata =
                    ComponentRegistry.getMetadata("SwitchComponent");
            if (metadata == null) return;
            String imagePath = logicGate.getOutput()
                    ? metadata.imagePathActive
                    : metadata.imagePath;
            imageView = ImageLoader.loadImage(imagePath, 40);
            if (imageView.getImage() != null) {
                if (this.getChildren().size() > 0 && this.getChildren().get(0) instanceof javafx.scene.image.ImageView) {
                    this.getChildren().set(0, imageView);
                } else if (this.getChildren().isEmpty()) {
                    this.getChildren().add(0, imageView);
                }
            }
        });
    }
}
