package org.example.logicgatesimulator.dto;

 /*
    Soll eine Verbindung zwischen Bauteilen darstellen.
    Enthält die Daten, die für den Export notwendig sind.
     */


public class ConnectionDTO {

    private String fromId;
    private String toId;
    private int inputIndex;
    private double startX;
    private double startY;
    private double endX;
    private double endY;

    public ConnectionDTO() {

    }


    //Getter

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public int getInputIndex() {
        return inputIndex;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndX() {
        return endX;
    }

    public double getEndY() {
        return endY;
    }


    //Setter

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public void setInputIndex(int inputIndex) {
        this.inputIndex = inputIndex;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }
}




