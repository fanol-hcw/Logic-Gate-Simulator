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
import org.example.logicgatesimulator.ComponentRegistry;
import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.LogicElement;
import org.example.simulation.Runner;
import org.example.simulation.inputs.ButtonElement;

public class ButtonComponent extends DraggableGate {
    static int buttonCount = 0;
    // Bildpfade werden aus ComponentRegistry bezogen
    private javafx.scene.image.ImageView imageView;

    public ButtonComponent(String name, Workspace workspace) {
        super("ButtonComponent", null, workspace);
        buttonCount++;
        this.logicGate = new ButtonElement();
        this.logicGate.name = "Button " + buttonCount;

        updateImageByState();

        logicGate.setOnUpdate(isOn -> {
            updateImageByState();
        });

        addOnMousePressedEvent(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                ((ButtonElement)logicGate).setAndPush(true);
                Runner.getInstance().step();
            }
        });
        addOnMouseReleasedEvent(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                ((ButtonElement)logicGate).setAndPush(false);
                Runner.getInstance().step();
            }
        });
    }

    private void updateImageByState() {
        javafx.application.Platform.runLater(() -> {
            ComponentRegistry.ComponentMetadata metadata =
                    ComponentRegistry.getMetadata("ButtonComponent");
            if (metadata == null) return;
            String imagePath = logicGate.getOutput()
                    ? metadata.imagePathActive
                    : metadata.imagePath;
            imageView = org.example.logicgatesimulator.ImageLoader.loadImage(imagePath, metadata.iconSize);
            if (imageView.getImage() != null) {
                if (this.getChildren().size() > 0 && this.getChildren().get(0) instanceof javafx.scene.image.ImageView) {
                    this.getChildren().set(0, imageView);
                } else if (this.getChildren().isEmpty()) {
                    this.getChildren().add(0, imageView);
                }
            }
        });
    }

    public LogicElement getLogic() {
        return logicGate;
    }
}