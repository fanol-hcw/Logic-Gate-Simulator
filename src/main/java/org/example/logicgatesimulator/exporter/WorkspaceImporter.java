package org.example.logicgatesimulator.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.logicgatesimulator.dto.WorkspaceDTO;

import java.io.File;
import java.io.IOException;

public class WorkspaceImporter {

    private final ObjectMapper mapper;

    public WorkspaceImporter() {
        mapper = new ObjectMapper();
        //mapper.enable(Des.INDENT_OUTPUT);
    }

    public WorkspaceDTO importFromJson(File importFile) throws IOException {
        if(!importFile.exists()){
            return new WorkspaceDTO();
        }
        return mapper.readValue(importFile, WorkspaceDTO.class);
    }

}
