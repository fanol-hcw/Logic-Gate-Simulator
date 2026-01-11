package org.example.logicgatesimulator;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.example.logicgatesimulator.components.ComponentBase;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;

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


    public Workspace(){
        init();
    }

    private void init(){
        components = new ArrayList<>();
        connections = new ArrayList<>();

        setStyle("-fx-background-color: white;");
        getChildren().add(drawGrid(2000, 2000));
        setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        setOnDragDropped(this::addComponent);

    }

    private void addComponent(DragEvent event){
        if (event.getGestureSource() != this && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        Dragboard db = event.getDragboard();
        if (db.hasString()) {
            double snapX = Math.round(event.getX() / GRID_SIZE) * GRID_SIZE;
            double snapY = Math.round(event.getY() / GRID_SIZE) * GRID_SIZE;
            Object gate;
            try {
                gate = Class.forName(db.getString()).getDeclaredConstructor(String.class, Workspace.class).newInstance(db.getString(), this);
            } catch (InstantiationException e) {
                return;
            } catch (IllegalAccessException e) {
                return;
            } catch (InvocationTargetException e) {
                return;
            } catch (NoSuchMethodException e) {
                return;
            } catch (ClassNotFoundException e) {
                return;
            }
            if(!(gate instanceof DraggableGate)){
                throw new RuntimeException("Node " +db.getString()+ " is not of type DragableGate.");
            }
            ((DraggableGate)gate).setLayoutX(snapX);
            ((DraggableGate)gate).setLayoutY(snapY);
            getChildren().add((DraggableGate)gate);
            components.add((DraggableGate) gate);
            event.setDropCompleted(true);
        }
        event.consume();
    }

    public Line addConnection(ComponentBase from, ComponentBase to){
        // check if exists
        if(from == to){
            System.out.println(from + " is equal to " + to);
            return null;
        }
        for(var connection : connections){
            if((connection.from == from && connection.to == to ) || ( connection.to == from && connection.from == to)){
                System.out.println("Returning null because:");
                System.out.println("Connection.from: " + connection.from);
                System.out.println("Connection.to: " + connection.to);
                System.out.println("params from: " + from);
                System.out.println("params to: " + to);
                return null;
            }
        }
        // add new connection
        Line line = new Line(from.getLayoutX(), from.getLayoutY(), to.getLayoutX(), to.getLayoutY());
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


}
