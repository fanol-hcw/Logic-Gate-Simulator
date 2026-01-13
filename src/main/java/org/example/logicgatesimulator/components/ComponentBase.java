package org.example.logicgatesimulator.components;

import javafx.geometry.Bounds;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Line;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.LogicElement;

import java.util.ArrayList;
import java.util.Optional;

public class ComponentBase extends ComponentEventHolder {

    protected LogicElement logicGate;

    protected final Workspace workspace;

    protected ArrayList<Line> connectedLines = new ArrayList<>();
    protected ArrayList<Line> incomingConnectedLines = new ArrayList<>();
    protected ArrayList<Line> outcomingConnectedLines = new ArrayList<>();

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
                    workspace.getChildren().remove(workspace.getCurrentLine());
                    workspace.setCurrentLine(null);
                    addLine(this, target);
                    break;
                }
            }
        });

    }

    public void addLine(ComponentBase from, ComponentBase to){
        Line line = workspace.addConnection(from, to);
        if(line != null){
            if(to.logicGate.getInputCount() > 1){
                ChoiceDialog<String> dialog = new ChoiceDialog<>("1", Integer.toString(to.logicGate.getInputCount()));
                dialog.setTitle("Connect Gates");
                dialog.setContentText("Choose the input index:");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(choice -> {
                    int index = Integer.parseInt(choice.split(" ")[0]) - 1;
                    to.logicGate.setInput(index, logicGate);
                    addOutcomingConnectedLine(line);
                    to.addIncomingConnectedLine(line);
                });
            }else{
                to.logicGate.setInput(0, logicGate);
                addOutcomingConnectedLine(line);
                to.addIncomingConnectedLine(line);
            }
        }
    }

    public void addIncomingConnectedLine(Line line){
        incomingConnectedLines.add(line);
    }
    public void addOutcomingConnectedLine(Line line){
        outcomingConnectedLines.add(line);
    }

    //Getter Methoden für den JSONExport
    public String getLogicGateName() {
        return logicGate.name;
    }

    public boolean getLogicGateOutput() {
        return logicGate.getOutput();
    }

    public int getInputIndexOf(LogicElement inputElement) {
        return logicGate.getInputIndex(inputElement);
    }

    public LogicElement getLogicGate() {
        return logicGate;
    }
}
