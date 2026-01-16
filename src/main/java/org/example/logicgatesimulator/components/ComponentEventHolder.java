package org.example.logicgatesimulator.components;

import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ComponentEventHolder extends StackPane {
    private final ArrayList<Consumer<MouseEvent>> mousePressedHandlers = new ArrayList<>();
    private final ArrayList<Consumer<MouseEvent>> mouseDraggedHandlers = new ArrayList<>();
    private final ArrayList<Consumer<MouseEvent>> mouseReleasedHandlers = new ArrayList<>();
    private final ArrayList<Consumer<DragEvent>> dragDroppedHandlers = new ArrayList<>();

    public ComponentEventHolder(){
        super();
        init();
    }

    private void init(){
        setOnMousePressed(event -> {
            for (Consumer<MouseEvent> eventHandler : mousePressedHandlers){
                eventHandler.accept(event);
            }
            event.consume();
        });
        setOnMouseDragged(event -> {
            for (Consumer<MouseEvent> eventHandler : mouseDraggedHandlers){
                eventHandler.accept(event);
            }
            event.consume();
        });
        setOnMouseReleased(event -> {
            for (Consumer<MouseEvent> eventHandler : mouseReleasedHandlers){
                eventHandler.accept(event);
            }
            event.consume();
        });
    }

    public void addOnMousePressedEvent(Consumer<MouseEvent> event){
        mousePressedHandlers.add(event);
    }

    public void addOnMouseDraggedEvent(Consumer<MouseEvent> event){
        mouseDraggedHandlers.add(event);
    }

    public void addOnMouseReleasedEvent(Consumer<MouseEvent> event){
        mouseReleasedHandlers.add(event);
    }

    public void addOnDragDroppedEvent(Consumer<DragEvent> event){
        dragDroppedHandlers.add(event);
    }



}
