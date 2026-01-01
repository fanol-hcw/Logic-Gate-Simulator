package org.example.simulation;

import java.util.ArrayList;
import java.util.List;

public abstract class LogicElement implements ILogicElement {
    protected ILogicElement[] inputs;
    protected boolean output = false;
    protected boolean newOutput = false;
    public String name;

    private boolean isUpdating = false;
    protected List<ILogicElement> outputConnections = new ArrayList<>();

    public LogicElement(int inputCount) {
        inputs = new ILogicElement[inputCount > 0 ? inputCount : 1];
    }

    @Override
    public void onInputChanged() {
        if (isUpdating) {
            // Loop detected! Schedule this for the next Runner tick to break recursion.
            Runner.getInstance().scheduleUpdate(this);
            return;
        }

        isUpdating = true;
        boolean oldOutput = this.output;

        execute();
        this.output = this.newOutput;

        if (this.output != oldOutput) {
            for (ILogicElement child : outputConnections) {
                child.onInputChanged();
            }
        }
        isUpdating = false;
    }

    @Override
    public void addOutputConnection(ILogicElement consumer) {
        outputConnections.add(consumer);
    }

    @Override
    public boolean getOutput() { return output; }

    @Override
    public void setInput(int index, ILogicElement element) {
        if (index >= 0 && index < inputs.length) {
            inputs[index] = element;
            element.addOutputConnection(this);
        }
    }
}
