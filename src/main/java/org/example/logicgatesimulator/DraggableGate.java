package org.example.logicgatesimulator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.geometry.Point2D;
import org.example.logicgatesimulator.components.ComponentBase;


/*
 * hier ist die Basis-Klasse für unsere Gatter.
 * Visuell steht das Ding: Das richtige Bild wird geladen
 */

public class DraggableGate extends ComponentBase {
    private String componentType;
    public ImageView imageView;
    private SimulatorUI uiContext;


    private double mouseAnchorX;
    private double mouseAnchorY;

    boolean wasControlDown = false;



    public DraggableGate(String type, SimulatorUI context, Workspace workspace) {
        super(workspace);
        this.componentType = type;
        this.uiContext = context;
        init();
        updateImage();
    }

    private void init(){

        addOnMousePressedEvent(event -> {
            if(event.isControlDown() && event.getButton() == MouseButton.SECONDARY){
                mouseAnchorX = event.getSceneX() - getLayoutX();
                mouseAnchorY = event.getSceneY() - getLayoutY();
            }
        });

        addOnMouseDraggedEvent(event -> {
            if(event.isControlDown() && event.getButton() == MouseButton.SECONDARY){
                wasControlDown = true;
            }
            if(wasControlDown && event.getButton() == MouseButton.SECONDARY){
                setLayoutX(event.getSceneX() - mouseAnchorX);
                setLayoutY(event.getSceneY() - mouseAnchorY);

                for (Line line : incomingConnectedLines){
                    int inputIndex = getIncomingLineInputIndex(line);
                    Point2D inputPos = getInputPortScenePosition(inputIndex);
                    if (inputPos != null) {
                        line.setEndX(inputPos.getX() - workspace.getLayoutX());
                        line.setEndY(inputPos.getY() - workspace.getLayoutY());
                    }
                }

                Point2D outputPos = getOutputPortScenePosition();
                if (outputPos != null) {
                    for (Line line : outcomingConnectedLines){
                        line.setStartX(outputPos.getX() - workspace.getLayoutX());
                        line.setStartY(outputPos.getY() - workspace.getLayoutY());
                    }
                }
            }
        });

        addOnMouseReleasedEvent(event -> {
            if(event.getButton() == MouseButton.SECONDARY){
                wasControlDown = false;
            }
        });
    }

    private void updateImage() {
        ComponentRegistry.ComponentMetadata metadata = ComponentRegistry.getMetadata(this.getClass().getSimpleName());
        String imagePath = metadata != null ? metadata.imagePath : componentType.toLowerCase() + ".png";

        double size = metadata != null ? metadata.iconSize : 40;
        imageView = ImageLoader.loadImage(imagePath, size);
        if (imageView.getImage() != null) {
            if (this.getChildren().size() > 0 && this.getChildren().get(0) instanceof javafx.scene.image.ImageView) {
                this.getChildren().set(0, imageView);
            } else {
                this.getChildren().add(0, imageView);
            }
        }
    }

    // Neue Methode: Bild basierend auf Zustand wechseln
    protected void updateImageBasedOnState() {
        ComponentRegistry.ComponentMetadata metadata = ComponentRegistry.getMetadata(this.getClass().getSimpleName());

        if (metadata == null) return;

        String imagePath = logicGate.getOutput() ? metadata.imagePathActive : metadata.imagePath;

        double size = metadata.iconSize;
        imageView = ImageLoader.loadImage(imagePath, size);
        if (imageView.getImage() != null) {
            if (this.getChildren().size() > 0 && this.getChildren().get(0) instanceof javafx.scene.image.ImageView) {
                this.getChildren().set(0, imageView);
            } else {
                this.getChildren().add(0, imageView);
            }
        }
    }

    protected boolean isWasControlDown() {
        return wasControlDown;
    }
}