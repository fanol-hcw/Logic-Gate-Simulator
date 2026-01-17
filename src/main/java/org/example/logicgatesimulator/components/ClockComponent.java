package org.example.logicgatesimulator.components;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import org.example.logicgatesimulator.ComponentRegistry;
import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.ImageLoader;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.inputs.Clock;

import java.util.Optional;

public class ClockComponent extends DraggableGate {

    static int clockCount = 0;
    private boolean isEnabled = false;

    public ClockComponent(String type, Workspace workspace) {
        super("ClockComponent", null, workspace);
        clockCount++;
        this.logicGate = new Clock();
        this.logicGate.name = "Clock " + clockCount;

        updateImageByState();

        logicGate.setOnUpdate(isOn -> {
            updateImageByState();
        });

        addOnMousePressedEvent(e -> {
            if (e.getButton() == MouseButton.MIDDLE) {
                Optional<Integer> result = showSliderDialog(((Clock) logicGate).getSleepTime());
                result.ifPresent(value -> ((Clock) logicGate).setSleepTime(value));
            } else if (e.getButton() == MouseButton.PRIMARY) {
                if (isEnabled) {
                    isEnabled = false;
                    ((Clock) logicGate).stop();
                } else {
                    isEnabled = true;
                    ((Clock) logicGate).start(this);
                }
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

    private void updateImageByState() {
        javafx.application.Platform.runLater(() -> {
            ComponentRegistry.ComponentMetadata metadata =
                    ComponentRegistry.getMetadata("ClockComponent");
            if (metadata == null) return;

            // Nur beim ersten Mal das Bild laden
            if (imageView == null || imageView.getImage() == null) {
                imageView = ImageLoader.loadImage(metadata.imagePath, 40);
                if (imageView.getImage() != null) {
                    if (this.getChildren().size() > 0 && this.getChildren().get(0) instanceof javafx.scene.image.ImageView) {
                        this.getChildren().set(0, imageView);
                    } else if (this.getChildren().isEmpty()) {
                        this.getChildren().add(0, imageView);
                    }
                }
            }

            // Spiegelung per ScaleX + Verschiebung nach links/rechts
            if (imageView != null) {
                boolean isOn = logicGate.getOutput();
                imageView.setScaleX(isOn ? -1 : 1);
                imageView.setTranslateX(isOn ? 3.5 : -3.5); // 5 Pixel Verschiebung
            }
        });
    }

    private Optional<Integer> showSliderDialog(int startingPosition) {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Select a Value");
        dialog.setHeaderText("Please move the slider to choose a number.");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        Slider slider = new Slider(100, 5000, startingPosition);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(250);

        Label valueLabel = new Label("Value: " + (int) slider.getValue());

        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                valueLabel.setText("Value: " + newVal.intValue()));

        VBox content = new VBox(10, valueLabel, slider);
        content.setPadding(new Insets(20));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return (int) slider.getValue();
            }
            return null;
        });

        return dialog.showAndWait();
    }
}
