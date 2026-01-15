package org.example.logicgatesimulator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.logicgatesimulator.SimulatorUI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class StartMenuController {

    public Button btnOpenProject;
    public Button btnEmptyScene;
    @FXML
    public ListView<String> recentList;

    @FXML
    public void initialize() {
        ObservableList<String> recentFiles = FXCollections.observableArrayList();
        recentFiles.addAll(loadRecentFiles());
        recentList.setItems(recentFiles);
        recentList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedItem = recentList.getSelectionModel().getSelectedItem();

                if (selectedItem != null) {
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    SimulatorUI ui = new SimulatorUI();
                    stage.getScene().setRoot(ui.getRoot(selectedItem));
                    stage.setWidth(1024);
                    stage.setHeight(800);
                    stage.setTitle("Logic Gate Simulator");
                    stage.setResizable(true);
                    stage.centerOnScreen();
                }
            }
        });
    }

    @FXML
    private void onBtnEmptySceneClicked(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SimulatorUI ui = new SimulatorUI();
        stage.getScene().setRoot(ui.getRoot());
        stage.setWidth(1024);
        stage.setHeight(800);
        stage.setTitle("Logic Gate Simulator");
        stage.setResizable(true);
        stage.centerOnScreen();
    }

    @FXML
    private void onBtnOpenProjectClicked(ActionEvent event) throws IOException {
        String filePath = "";
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser dir_chooser = new FileChooser();
        File file = dir_chooser.showOpenDialog(stage);
        if (file != null) {
            filePath = file.getAbsolutePath();
        }

        SimulatorUI ui = new SimulatorUI();
        stage.getScene().setRoot(ui.getRoot(filePath));
        stage.setWidth(1024);
        stage.setHeight(800);
        stage.setTitle("Logic Gate Simulator");
        stage.setResizable(true);
        stage.centerOnScreen();
    }

    private ArrayList<String> loadRecentFiles() {
        try {
            File recentFilesFile = new File("recentGatesFiles.conf");
            if(!recentFilesFile.exists()){
                if(!recentFilesFile.createNewFile()){
                    System.err.println("Failed to create config file.");
                    return new ArrayList<>();
                }
            }
            ArrayList<String> recentFiles = new ArrayList<>();
            Scanner reader = new Scanner(recentFilesFile);
            while(reader.hasNextLine()){
                String file = reader.nextLine();
                recentFiles.add(file);
            }
            return recentFiles;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
