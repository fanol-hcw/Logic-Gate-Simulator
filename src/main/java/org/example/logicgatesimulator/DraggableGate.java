package org.example.logicgatesimulator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*
 * hier ist die Basis-Klasse für unsere Gatter.
 * Visuell steht das Ding: Das richtige Bild wird geladen
 * die "Drag & Drop" Logik implementieren. - implementiert
 * Gatter mit der Maus verschieben - implementiert
 * TODO
 * die Logik für das Verkabeln und das Rechnen (AND/OR) einbauen.
 */

public class DraggableGate extends Pane {
    private String componentType;
    private ImageView imageView;
    private SimulatorUI uiContext;

    private double mouseAnchorX;
    private double mouseAnchorY;

    private List<Circle> inputPorts = new ArrayList<>();
    private List<Circle> outputPorts = new ArrayList<>();

    public DraggableGate(String type, SimulatorUI context) {
        this.componentType = type;
        this.uiContext = context;
        this.setPrefSize(80, 60);
        updateImage();
        addPorts();
        enableDrag();
    }

    private void updateImage() {
        String imageName = componentType.toLowerCase() + ".png";
        InputStream is = getClass().getResourceAsStream("/images/" + imageName);
        if (is != null) {
            if (imageView != null) this.getChildren().remove(imageView);
            imageView = new ImageView(new Image(is));
            imageView.setFitWidth(80);
            imageView.setPreserveRatio(true);
            if (this.getChildren().size() > 0) this.getChildren().add(0, imageView);
            else this.getChildren().add(imageView);
        }
    }

    private void addPorts() {
        int inputs = 2;
        if (componentType.equals("Switch") || componentType.equals("CONST0") || componentType.equals("CONST1")) {
            inputs = 0;
        } else if (componentType.equals("BUFFER") || componentType.equals("NOT")) {
            inputs = 1;
        }
        int outputs = 1;
        if (componentType.equals("LIGHT") || componentType.equals("DIGIT")) {
            outputs = 0;
        }

        for (int i = 0; i < inputs; i++) {
            double y = 20 + (i * 20);
            addPort(80, 30, false);
        }
    }

    private void addPort(double x, double y, boolean isIput) {
        Circle port = new Circle(5);
        port.setCenterX(x);
        port.setCenterY(y);
        port.setFill(Color.BLUE);
        port.setStroke(Color.BLACK);

        this.getChildren().add(port);
        if (isIput) inputPorts.add(port);
        else outputPorts.add(port);
    }

    private void enableDrag() {
        this.setOnMousePressed(event -> {
            mouseAnchorX = event.getX();
            mouseAnchorY = event.getY();
            this.toFront();
            event.consume();
        });

        this.setOnMouseDragged(event -> {
            double newX = getLayoutX() + event.getX() - mouseAnchorX;
            double newY = getLayoutY() + event.getY() - mouseAnchorY;
            this.setLayoutX(newX);
            this.setLayoutY(newY);

            event.consume();
        });
    }
}