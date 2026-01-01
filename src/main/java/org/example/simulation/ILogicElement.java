package org.example.simulation;

public interface ILogicElement {
    void onInputChanged();
    void execute();
    boolean getOutput();
    void setInput(int index, ILogicElement element);
    void addOutputConnection(ILogicElement consumer);
}