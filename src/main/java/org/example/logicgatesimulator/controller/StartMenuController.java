package org.example.logicgatesimulator.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.logicgatesimulator.SimulatorUI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.example.logicgatesimulator.SimulatorUI.WINDOW_HEIGHT;
import static org.example.logicgatesimulator.SimulatorUI.WINDOW_WIDTH;

public class StartMenuController {

    public Button btnOpenProject;
    public Button btnEmptyScene;
    @FXML
    public ListView<String> recentList;

    @FXML
    private Button btnOpenRecent;
    @FXML
    private Button btnDeleteRecent;


    @FXML
    public void initialize() {
        ObservableList<String> recentFiles = FXCollections.observableArrayList();
        recentFiles.addAll(loadRecentFiles());
        recentList.setItems(recentFiles);
        recentList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String fullPath, boolean empty) {
                super.updateItem(fullPath, empty);

                if (empty || fullPath == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    File file = new File(fullPath);

                    // Main File Name (Bold)
                    Label nameLabel = new Label(file.getName());
                    nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #2c3e50;");

                    // Full Path (Grayed out and smaller)
                    Label pathLabel = new Label(fullPath);
                    pathLabel.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 10px;");

                    VBox container = new VBox(2, nameLabel, pathLabel);
                    setGraphic(container);
                }
            }
        });
        recentList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedItem = recentList.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    openProject(selectedItem, (Stage) ((Node) event.getSource()).getScene().getWindow());
                }
            }
        });
        btnOpenRecent.disableProperty().bind(
                recentList.getSelectionModel().selectedItemProperty().isNull()
        );

        btnDeleteRecent.disableProperty().bind(
                recentList.getSelectionModel().selectedItemProperty().isNull()
        );
    }

    @FXML
    private void onBtnEmptySceneClicked(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SimulatorUI ui = new SimulatorUI();
        stage.getScene().setRoot(ui.getRoot());
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setTitle("Logic Gate Simulator");
        stage.setResizable(true);
        stage.centerOnScreen();
    }

    @FXML
    private void onBtnOpenProjectClicked(ActionEvent event) throws IOException {
        String filePath = "";
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser dir_chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        dir_chooser.getExtensionFilters().add(extFilter);
        File file = dir_chooser.showOpenDialog(stage);
        if (file == null || !file.isFile()) {
            return;
        }

        filePath = file.getAbsolutePath();

        openProject(filePath, (Stage) ((Node) event.getSource()).getScene().getWindow());
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

    private void openProject(String path, Stage stage) {
        SimulatorUI ui = new SimulatorUI();
        stage.getScene().setRoot(ui.getRoot(path));
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setTitle("Logic Gate Simulator");
        stage.setResizable(true);
        stage.centerOnScreen();
    }

    @FXML
    private void onBtnOpenRecentClicked(ActionEvent event) {
        String selectedPath = recentList.getSelectionModel().getSelectedItem();
        if (selectedPath != null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            openProject(selectedPath, stage);
        } else {
            System.out.println("No project selected to open.");
        }
    }

    @FXML
    private void onBtnDeleteRecentClicked(ActionEvent event) {
        int selectedIndex = recentList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // Remove from the visual list
            recentList.getItems().remove(selectedIndex);

            // Save the updated list to your config file
            saveRecentFiles(new ArrayList<>(recentList.getItems()));
        }
    }

    // Helper to save changes back to recentGatesFiles.conf
    private void saveRecentFiles(ArrayList<String> files) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new File("recentGatesFiles.conf"))) {
            for (String path : files) {
                writer.println(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
