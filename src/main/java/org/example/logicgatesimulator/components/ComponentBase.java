package org.example.logicgatesimulator.components;

import javafx.geometry.Bounds;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Line;
import org.example.logicgatesimulator.Workspace;

import java.util.ArrayList;

public class ComponentBase extends ComponentEventHolder {

    protected final Workspace workspace;

    protected ArrayList<Line> connectedLines = new ArrayList<>();

    public ComponentBase(Workspace workspace) {
        super();
        this.workspace = workspace;
        init();
    }

    private void init(){
        addOnMousePressedEvent(event -> {
            System.out.println("Starting movement from " + this);
            if(event.getButton() == MouseButton.SECONDARY && !event.isControlDown()){
                workspace.setCurrentLine(new Line(event.getSceneX() - workspace.getLayoutX(), event.getSceneY() - workspace.getLayoutY(), event.getSceneX() - workspace.getLayoutX(), event.getSceneY() - workspace.getLayoutY()));
                workspace.getChildren().add(workspace.getCurrentLine());
            }
        });

        addOnMouseDraggedEvent(event -> {
            if (workspace.getCurrentLine() != null) {
                workspace.getCurrentLine().setEndX(event.getSceneX() - workspace.getLayoutX());
                workspace.getCurrentLine().setEndY(event.getSceneY() - workspace.getLayoutY());
            }
        });

        addOnMouseReleasedEvent(event -> {
            double screenX = event.getScreenX();
            double screenY = event.getScreenY();
            for (ComponentBase target : workspace.getComponents()) {
                Bounds boundsInScreen = target.localToScreen(target.getBoundsInLocal());
                if (boundsInScreen != null && boundsInScreen.contains(screenX, screenY)) {
                    Line line = workspace.addConnection(this, target);
                    connectedLines.add(line);
                    break;
                }
            }
            workspace.getChildren().remove(workspace.getCurrentLine());
            workspace.setCurrentLine(null);
        });

    }

}
