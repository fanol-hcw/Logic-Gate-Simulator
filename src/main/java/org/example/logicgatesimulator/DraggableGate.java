package org.example.logicgatesimulator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import java.io.InputStream;

/*
 * hier ist die Basis-Klasse für unsere Gatter.
 * Visuell steht das Ding: Das richtige Bild wird geladen
 * ABER: Aktuell sind die Bauteile noch am Boden festgetackert!
 * TODO..
 * die "Drag & Drop" Logik implementieren.
 * Gatter mit der Maus verschieben
 * die Logik für das Verkabeln und das Rechnen (AND/OR) einbauen.
 */

public class DraggableGate extends StackPane {
    private String componentType;
    private ImageView imageView;
    private SimulatorUI uiContext;

    public DraggableGate(String type, SimulatorUI context) {
        this.componentType = type;
        this.uiContext = context;
        updateImage();
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
}