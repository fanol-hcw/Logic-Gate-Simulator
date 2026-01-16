package org.example.logicgatesimulator;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.example.logicgatesimulator.components.ComponentBase;
import org.example.logicgatesimulator.dto.ComponentDTO;
import org.example.logicgatesimulator.dto.ConnectionDTO;
import org.example.logicgatesimulator.dto.WorkspaceDTO;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

import static org.example.logicgatesimulator.SimulatorUI.GRID_SIZE;

public class Workspace extends Pane {

    private class Connection {
        public Line line;
        public ComponentBase from;
        public ComponentBase to;
    }

    private ArrayList<ComponentBase> components;
    private ArrayList<Connection> connections;
    private Line currentLine;


    public Workspace() {
        init();
    }

    private void init() {
        components = new ArrayList<>();
        connections = new ArrayList<>();

        setStyle("-fx-background-color: white;");
        getChildren().add(drawGrid(3000, 2000));
        setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        setOnDragDropped(this::addComponent);

    }

    private void addComponent(String type, double x, double y){
        System.out.println(type);
        Object gate;
        try {
            gate = Class.forName(type).getDeclaredConstructor(String.class, Workspace.class).newInstance(type, this);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(!(gate instanceof DraggableGate)){
            throw new RuntimeException("Node " + type + " is not of type DragableGate.");
        }
        ((DraggableGate)gate).setLayoutX(x);
        ((DraggableGate)gate).setLayoutY(y);
        /// Löscht die ausgewählte Komponente
        ((DraggableGate)gate).setOnContextMenuRequested(event -> {
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Lösch die Komponente");
            deleteItem.setOnAction(e -> {
                deleteComponent((ComponentBase) gate);
            });
            contextMenu.getItems().add(deleteItem);
            contextMenu.show(((DraggableGate)gate), event.getScreenX(), event.getScreenY());
            event.consume();;
        });
        getChildren().add((DraggableGate)gate);
        components.add((DraggableGate) gate);
    }

    private void addComponent(DragEvent event){
        if (event.getGestureSource() != this && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        Dragboard db = event.getDragboard();
        if (db.hasString()) {
            addComponent(db.getString(), event.getX(), event.getY());
            event.setDropCompleted(true);
        }
        event.consume();
    }

    public void deleteComponent(ComponentBase componentToDelete){
        ArrayList<Connection> connectionsToDelete = new ArrayList<>();
        for(Connection connection : connections){
            if(connection.to == componentToDelete || connection.from == componentToDelete){
                connection.to.disconnectLine(connection.line);
                connection.from.disconnectLine(connection.line);
                connectionsToDelete.add(connection);
                getChildren().remove(connection.line);
            }
        }
        connections.removeAll(connectionsToDelete);
        components.remove(componentToDelete);
        getChildren().remove(componentToDelete);
    }

    public Line addConnection(ComponentBase from, ComponentBase to) {
        // check if exists
        if (from == to) {
            return null;
        }
        for (var connection : connections) {
            if ((connection.from == from && connection.to == to) || (connection.to == from && connection.from == to)) {
                return null;
            }
        }
        // add new connection
        Circle outPort  = from.getConnectingPort();
        Circle inPort = to.getConnectingPort();

        Point2D start = outPort.localToScene(0,0 );
        Point2D end = inPort.localToScene(0,0);

        Line line = new Line(
                start.getX() - getLayoutX(),
                start.getY() - getLayoutY(),
                end.getX() - getLayoutX(),
                end.getY() - getLayoutY());
        Connection c = new Connection();
        c.from = from;
        c.to = to;
        c.line = line;
        connections.add(c);
        getChildren().add(line);
        return line;
    }

    private Canvas drawGrid(double width, double height) {
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.web("#E0E0E0"));
        gc.setLineWidth(1);
        for (double x = 0; x < width; x += GRID_SIZE) gc.strokeLine(x, 0, x, height);
        for (double y = 0; y < height; y += GRID_SIZE) gc.strokeLine(0, y, width, y);
        return canvas;
    }

    public Line getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(Line currentLine) {
        this.currentLine = currentLine;
    }

    public ArrayList<ComponentBase> getComponents() {
        return components;
    }

    public void clearAll() {
        getChildren().removeIf(node -> node instanceof ComponentBase || node instanceof Line);
        components.clear();
        connections.clear();
        currentLine = null;
    }

    public void fromWorkspaceDTO(WorkspaceDTO workspaceDTO) {
        for(ComponentBase component : components){
            getChildren().remove(component);
        }
        for(Connection connection : connections){
            getChildren().remove(connection);
        }
        components.clear();
        connections.clear();
        HashMap<String, ComponentBase> loadedComponents = new HashMap();

        for(ComponentDTO componentDTO : workspaceDTO.getComponents()){
            addComponent("org.example.logicgatesimulator.components." + componentDTO.getComponentType(), componentDTO.getX(), componentDTO.getY());
            loadedComponents.put(componentDTO.getId(), components.getLast());
        }
        for(ConnectionDTO connectionDTO : workspaceDTO.getConnections()){
            ComponentBase from = loadedComponents.get(connectionDTO.getFromId());
            ComponentBase to = loadedComponents.get(connectionDTO.getToId());
            from.addLine(from, to, connectionDTO.getInputIndex());
            //components.get(connectionDTO.from).addLine(components.get(connectionDTO.from), components.get(connectionDTO.to), savedConnection.inputIndex);
        }
    }

    // JSON Export
    public WorkspaceDTO toWorkspaceDTO() {

        WorkspaceDTO workspaceDTO = new WorkspaceDTO();

        //Map für die Zuordnung von ComponentBase über eindeutige ID

        Map<ComponentBase, String> componentIdMap = new HashMap<>();
        List<ComponentDTO> workspaceComponents = new ArrayList<>();
        int counter = 1;

        for (ComponentBase component : components) {
            ComponentDTO compDTO = new ComponentDTO();

            String componentId = "Comp" + counter++;
            componentIdMap.put(component, componentId);

            compDTO.setId(componentId);
            compDTO.setComponentType(component.getClass().getSimpleName());
            compDTO.setName(component.getLogicGateName());
            compDTO.setX(component.getLayoutX());
            compDTO.setY(component.getLayoutY());
            compDTO.setState(component.getLogicGateOutput());

            workspaceComponents.add(compDTO);
        }

        List<ConnectionDTO> workspaceConnections = new ArrayList<>();
        for (Connection connection : connections) {
            ConnectionDTO conDTO = new ConnectionDTO();

            conDTO.setFromId(componentIdMap.get(connection.from));
            conDTO.setToId(componentIdMap.get(connection.to));
            conDTO.setInputIndex(connection.to.getInputIndexOf(connection.from.getLogicGate()));
            conDTO.setStartX(connection.line.getStartX());
            conDTO.setStartY(connection.line.getStartY());
            conDTO.setEndX(connection.line.getEndX());
            conDTO.setEndY(connection.line.getEndY());

            workspaceConnections.add(conDTO);
        }

        workspaceDTO.setComponents(workspaceComponents);
        workspaceDTO.setConnections(workspaceConnections);

        return workspaceDTO;
    }
}



