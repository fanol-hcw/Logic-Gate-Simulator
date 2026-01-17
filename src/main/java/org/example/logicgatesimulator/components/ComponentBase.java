package org.example.logicgatesimulator.components;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.geometry.Pos;
import org.example.logicgatesimulator.Workspace;
import org.example.simulation.LogicElement;
import org.example.simulation.Runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ComponentBase extends ComponentEventHolder {

    protected LogicElement logicGate;

    protected final Workspace workspace;

    protected ArrayList<Line> connectedLines = new ArrayList<>();
    protected ArrayList<Line> incomingConnectedLines = new ArrayList<>();
    protected ArrayList<Line> outcomingConnectedLines = new ArrayList<>();
    private List<Circle> inputPorts = new ArrayList<>();
    private Circle outputPort;
    private final Map<Line, Integer> incomingLineInputIndex = new HashMap<>();

    public ComponentBase(Workspace workspace) {
        super();
        this.workspace = workspace;
        init();
        addPorts();
    }

    private void init(){
        addOnMousePressedEvent(event -> {
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

        this.setFocusTraversable(true);
        setOnMouseEntered(event -> {
            requestFocus();
        });
        setOnMouseExited(event->{
            getParent().requestFocus();
        });
        setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.DELETE){
                workspace.deleteComponent(this);
            }
        });
    }

    public void disconnectLine(Line line){
        incomingConnectedLines.remove(line);
        outcomingConnectedLines.remove(line);
        incomingLineInputIndex.remove(line);
    }

    public void addLine(ComponentBase from, ComponentBase to, int index){
        Line line = workspace.addConnection(from, to, index);
        if(line != null){
            to.logicGate.setInput(index, logicGate);
            addOutcomingConnectedLine(line);
            to.addIncomingConnectedLine(line, index);
            // Signal nach dem Verbinden propagieren
            Runner.getInstance().scheduleUpdate(to.logicGate);
            Runner.getInstance().step();
        }
    }

    public void addLine(ComponentBase from, ComponentBase to){
        if(to.logicGate.getInputCount() > 1){
            ChoiceDialog<String> dialog = new ChoiceDialog<>("1", Integer.toString(to.logicGate.getInputCount()));
            dialog.setTitle("Connect Gates");
            dialog.setContentText("Choose the input index:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(choice -> {
                int index = Integer.parseInt(choice.split(" ")[0]) - 1;
                addLine(from, to, index);
            });
        }else{
            addLine(from, to, 0);
        }
    }

    public void addIncomingConnectedLine(Line line, int inputIndex){
        incomingConnectedLines.add(line);
        incomingLineInputIndex.put(line, inputIndex);
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

    protected int getInputPortCount() {
        return 1;
    }

    protected int getOutputPortCount() {
        return 1;
    }

    protected double getInputPortXOffset() {
        return 0;
    }

    protected double getInputPortSpacing() {
        return 14;
    }

    protected double getOutputPortXOffset() {
        return 0;
    }

    private Circle createPort() {
        Circle port = new Circle(5);
        port.setFill(Color.BLACK);
        port.setStroke(Color.BLACK);
        return port;
    }

    private void addPorts() {
        int inputCount = getInputPortCount();
        if (inputCount > 0) {
            double spacing = getInputPortSpacing();
            double startOffset = inputCount == 1 ? 0 : -((inputCount - 1) / 2.0) * spacing;
            for (int i = 0; i < inputCount; i++) {
                Circle port = createPort();
                StackPane.setAlignment(port, Pos.CENTER_LEFT);
                port.setTranslateX(getInputPortXOffset());
                port.setTranslateY(startOffset + i * spacing);
                inputPorts.add(port);
                this.getChildren().add(port);
            }
        }

        if (getOutputPortCount() > 0) {
            outputPort = createPort();
            StackPane.setAlignment(outputPort, Pos.CENTER_RIGHT);
            outputPort.setTranslateX(getOutputPortXOffset());
            this.getChildren().add(outputPort);
        }
    }

    public Circle getInputPort(int index) {
        if (index < 0 || index >= inputPorts.size()) {
            return null;
        }
        return inputPorts.get(index);
    }

    public Circle getOutputPort() {
        return outputPort;
    }

    public Point2D getInputPortScenePosition(int index) {
        Circle port = getInputPort(index);
        return port != null ? port.localToScene(0, 0) : null;
    }

    public Point2D getOutputPortScenePosition() {
        return outputPort != null ? outputPort.localToScene(0, 0) : null;
    }

    public int getIncomingLineInputIndex(Line line) {
        return incomingLineInputIndex.getOrDefault(line, 0);
    }

}
