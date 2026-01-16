package org.example.logicgatesimulator.components;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.example.logicgatesimulator.DraggableGate;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.inputs.Clock;

import java.util.Optional;

public class ClockComponent extends DraggableGate {

    private final Circle toggleSwitch;
    static int clockCount = 0;
    private boolean isEnabled = false;

    public ClockComponent(String type, Workspace workspace) {
        super(type, null, workspace);
        clockCount++;
        this.logicGate = new Clock();
        this.logicGate.name = "Clock " + clockCount;
        Text label = new Text(this.logicGate.name);
        label.setTranslateY(60);
        label.setWrappingWidth(100);
        label.setTextAlignment(TextAlignment.CENTER);
        this.toggleSwitch = new Circle(20, Color.GRAY);
        this.toggleSwitch.setStroke(Color.BLACK);
        this.toggleSwitch.setCursor(Cursor.HAND);
        this.toggleSwitch.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.MIDDLE){
                Optional<Integer> result = showSliderDialog(((Clock) logicGate).getSleepTime());
                result.ifPresent(value -> ((Clock) logicGate).setSleepTime(value));
            }else if(e.getButton() == MouseButton.PRIMARY){
                if(isEnabled){
                    isEnabled = false;
                    ((Clock)logicGate).stop();
                    toggleSwitch.setFill(Color.GRAY);
                }else {
                    isEnabled = true;
                    ((Clock)logicGate).start(this);
                }
            }
        });
        logicGate.setOnUpdate(event -> {
            toggleSwitch.setFill(event ? Color.LIME : Color.DARKRED);
        });

        this.getChildren().addAll(toggleSwitch, label);
    }

    private Optional<Integer> showSliderDialog(int startingPosition) {
        // 1. Create the dialog instance
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Select a Value");
        dialog.setHeaderText("Please move the slider to choose a number.");

        // 2. Set the button types (OK and Cancel)
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // 3. Create the UI components
        Slider slider = new Slider(100, 5000, startingPosition); // Min, Max, Initial value
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(250);

        Label valueLabel = new Label("Value: " + (int) slider.getValue());

        // Update label dynamically as slider moves
        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                valueLabel.setText("Value: " + newVal.intValue()));

        VBox content = new VBox(10, valueLabel, slider);
        content.setPadding(new Insets(20));
        dialog.getDialogPane().setContent(content);

        // 4. Convert the result to the slider value when OK is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return (int) slider.getValue();
            }
            return null;
        });

        return dialog.showAndWait();
    }

}
