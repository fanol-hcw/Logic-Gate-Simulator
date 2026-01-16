package org.example.simulation.inputs;

import org.example.logicgatesimulator.components.ComponentBase;
import org.example.simulation.LogicElement;
import org.example.simulation.Runner;

import java.util.Arrays;

public class Clock extends LogicElement {

    private Thread clock;

    private int sleepTime = 2000;

    public Clock() {
        super(0);
    }

    public void setAndPush(boolean value) {
        if(value != this.output){
            this.output = value;
            Runner.getInstance().scheduleUpdates(outputConnections);
            Runner.getInstance().step();
        }
    }

    public void start(ComponentBase test){
        if(clock != null && clock.isAlive()){
            clock.interrupt();
            clock = null;
        }
        clock = new Thread(() -> {
            try {
                while(true){
                    if(test == null){
                        throw new InterruptedException();
                    }
                    setAndPush(!output);
                    onUpdate.accept(output);
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                setAndPush(false);
            }
        });
        clock.start();
    }

    public void stop(){
        clock.interrupt();
        clock = null;
    }

    public void setSleepTime(int sleepTime){
        if(sleepTime < 100){
            this.sleepTime = 100;
        }else {
            this.sleepTime = sleepTime;
        }
    }
    public int getSleepTime(){
        return sleepTime;
    }

    @Override
    public boolean calculateNewOutput() {
        return this.output;
    }
}
