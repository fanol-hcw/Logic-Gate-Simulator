package org.example.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public abstract class LogicElement {
    protected LogicElement[] inputs;
    protected boolean output = false;
    public String name = "Unnamed";
    protected List<LogicElement> outputConnections = new ArrayList<>();

    private Consumer<Boolean> onUpdate;

    public LogicElement(int inputCount) {
        // Default to 1 input if 0 is provided for consistency
        inputs = new LogicElement[inputCount > 0 ? inputCount : 1];
    }

    // This is the core logic of the gate (AND, OR, NOT)
    public abstract boolean calculateNewOutput();

    // Triggered when an input changes
    public void onInputChanged() {
        boolean newResult = calculateNewOutput();

        if (newResult != this.output) {
            this.output = newResult;
            onUpdate.accept(newResult);
            System.out.println(Arrays.toString(outputConnections.toArray()));
            // Notify the simulation engine that things have changed
            Runner.getInstance().scheduleUpdates(outputConnections);
        }
    }

    public void setInput(int index, LogicElement element) {
        System.out.println("Adding " + element + "to index " + index + "to " + this);
        if (index >= 0 && index < inputs.length) {
            System.out.println("Added");
            if(inputs[index] != null){
                index += 1;
            }
            inputs[index] = element;
            element.addOutputConnection(this);
        }
    }

    public void addOutputConnection(LogicElement consumer) {
        outputConnections.add(consumer);
    }

    public boolean getOutput() {
        return output;
    }

    // Helper to safely get input values without NullPointerExceptions
    protected boolean getInputSafe(int index) {
        if (index < inputs.length && inputs[index] != null) {
            return inputs[index].getOutput();
        }
        return false; // Default value for unconnected pins
    }

    public void setOnUpdate(Consumer<Boolean> onUpdate) {
        this.onUpdate = onUpdate;
    }

    public int getInputCount(){
        return inputs.length;
    }

    //Getter für Export
    public int getInputIndex(LogicElement inputElement) {
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] == inputElement) {
                return i;
            }
        }
        return -1; // wenn Element nicht verbunden
    }

}