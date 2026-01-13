package org.example.logicgatesimulator.exporter;

/*
WorkspaceExporter schreibt ein WorkspaceDTO als JSON-Datei.
Nutzt Jackson ObjectMapper und sorgt für das Speichern der Datei.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.logicgatesimulator.dto.WorkspaceDTO;

import java.io.File;
import java.io.IOException;

public class WorkspaceExporter {

    private final  ObjectMapper mapper;

    public WorkspaceExporter() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void exportToJson(WorkspaceDTO workspaceDTO, File newfile) throws IOException {
        mapper.writeValue(newfile, workspaceDTO);
    }

}
