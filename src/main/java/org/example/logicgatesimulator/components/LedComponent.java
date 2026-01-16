package org.example.logicgatesimulator.components;

import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.example.logicgatesimulator.ComponentRegistry;
import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.outputs.LedElement;
import org.example.logicgatesimulator.ImageLoader;


public class LedComponent extends DraggableGate {

    public LedComponent(String name, Workspace workspace) {
        super(name, null, workspace);
        this.logicGate = new LedElement();

        // ImageView zuerst laden und hinzufügen
        updateImageByState();


        logicGate.setOnUpdate(isOn -> {
            updateImageByState();
        });
    }

    private void updateImageByState() {
        javafx.application.Platform.runLater(() -> {
            ComponentRegistry.ComponentMetadata metadata =
                    ComponentRegistry.getMetadata(this.getClass().getSimpleName());

            if (metadata == null) return;

            String imagePath = logicGate.getOutput()
                    ? metadata.imagePathActive
                    : metadata.imagePath;

            imageView = ImageLoader.loadImage(imagePath, 80);

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
