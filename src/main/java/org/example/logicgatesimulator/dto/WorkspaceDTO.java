package org.example.logicgatesimulator.dto;

/*
Repräsentiert den gesamten Workspace für den Export.
*/

import java.util.List;

public class WorkspaceDTO {

    private List<ComponentDTO> components;
    private List<ConnectionDTO> connections;

    public WorkspaceDTO() {

    }

    public List<ComponentDTO> getComponents() {
        return components;
    }

    public List<ConnectionDTO> getConnections() {
        return connections;
    }

    public void setComponents(List<ComponentDTO> components) {
        this.components = components;
    }

    public void setConnections(List<ConnectionDTO> connections) {
        this.connections = connections;
    }
}
