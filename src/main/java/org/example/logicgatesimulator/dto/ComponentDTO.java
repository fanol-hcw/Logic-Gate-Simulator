package org.example.logicgatesimulator.dto;

public class ComponentDTO {

    /*
    Soll ein einzelnes Bauteil im Workspace darstellen.
    Enthält die Daten, die für den Export notwendig sind.
     */

    private String id;
    private String ComponentType;
    private String name;
    private double x;
    private double y;
    private Boolean state;

    public ComponentDTO() {
    }

    //Getter

    public String getId() {
        return id;
    }

    public String getComponentType() {
        return ComponentType;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Boolean getState() {
        return state;
    }


    //Setter

    public void setId(String id) {
        this.id = id;
    }

    public void setComponentType(String componentType) {
        this.ComponentType = componentType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
