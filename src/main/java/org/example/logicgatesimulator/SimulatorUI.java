package org.example.logicgatesimulator;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.logicgatesimulator.components.*;
import org.example.logicgatesimulator.dto.WorkspaceDTO;
import org.example.logicgatesimulator.exporter.WorkspaceExporter;
import org.example.logicgatesimulator.exporter.WorkspaceImporter;
import org.example.logicgatesimulator.Ribbon;

import java.io.File;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.time.Instant;

/*
 * hier ist der Stand für unser Hauptfenster (UI).
 * Das Menü oben  hat den gewünschten Look mit den Trennlinien.
 * Das Grid wird gezeichnet.
 * -Man kann die Gatter bereits aus dem Menü auf das Arbeitsfeld ziehen (Drag & Drop funktioniert).
 * TODO
 * die Verkabelung implementieren.
 * Aktuell gibt es noch gar keine Logik, um Kabel zwischen den Anschlüssen zu ziehen.
 * Wir müssen uns überlegen, wie wir die Maus-Events (Klicken & Ziehen an den Ports) hier verarbeiten
 * und die Linien zeichnen.
 */
public class SimulatorUI {
    private BorderPane root;
    private Workspace workspace = new Workspace();
    public static final int GRID_SIZE = 20;

    public SimulatorUI() {
        root = new BorderPane();
        setupMenu();
        setupRibbon();
        setupWorkspace();
    }

    public BorderPane getRoot() { return root; }

    public BorderPane getRoot(String pathToProject){
        File file = new File(pathToProject);
        if (file != null) {
            String path = file.getAbsolutePath();
            System.out.println(path);
            try {
                WorkspaceImporter importer = new WorkspaceImporter();
                WorkspaceDTO workspaceDTO = importer.importFromJson(new File(path));
                workspace.fromWorkspaceDTO(workspaceDTO);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return root;
    }

    private void setupMenu(){
        Menu menu = new Menu("File");
        //menu.getItems().add(new MenuItem("Save"));
        MenuItem menuSave = new MenuItem("Export");
        menuSave.setOnAction(event -> {
            DirectoryChooser dir_chooser = new DirectoryChooser();
            File file = dir_chooser.showDialog(this.getRoot().getScene().getWindow());
            if (file != null) {
                String path = file.getAbsolutePath() + "/workspace_" + Instant.now().toEpochMilli() + ".json";
                System.out.println(path);
                try {
                    WorkspaceDTO newDTO = workspace.toWorkspaceDTO();
                    WorkspaceExporter exporter = new WorkspaceExporter();
                    exporter.exportToJson(newDTO, new File(path));
                    System.out.println("Export completed");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        menu.getItems().add(menuSave);
        MenuItem menuLoad = new MenuItem("Import");
        menuLoad.setOnAction(event -> {
            FileChooser dir_chooser = new FileChooser();
            File file = dir_chooser.showOpenDialog(this.getRoot().getScene().getWindow());
            if (file != null) {
                String path = file.getAbsolutePath();
                System.out.println(path);
                try {
                    WorkspaceImporter importer = new WorkspaceImporter();
                    WorkspaceDTO workspaceDTO = importer.importFromJson(new File(path));
                    workspace.fromWorkspaceDTO(workspaceDTO);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        menu.getItems().add(menuLoad);
        MenuItem menuCloseProject= new MenuItem("Close Project");
        menuCloseProject.setOnAction(event -> {
            // todo: frage benutzer ob er speichern will
            try {
                Stage stage = (Stage) this.getRoot().getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("start-menu.fxml"));
                stage.setScene(new Scene(root, 600, 400));
                stage.setWidth(600);
                stage.setHeight(400);
                stage.setResizable(false);
                stage.show();
                stage.centerOnScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        menu.getItems().add(menuCloseProject);
        MenuBar mb = new MenuBar();
        mb.getMenus().add(menu);
        root.setTop(mb);
    }

    private void setupRibbon() {
        Ribbon ribbon = new Ribbon(root);
//        HBox ribbon = new HBox(20);
        ribbon.setPadding(new Insets(10));
        ribbon.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-radius: 30;" +
                        "-fx-border-color: #D0D0D0;" +
                        "-fx-border-width: 3;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );
        ribbon.setPrefHeight(120);
        ribbon.setAlignment(Pos.CENTER_LEFT);

        ribbon.addGroup("Inputs");
        ribbon.addGroup("Outputs");
        ribbon.addGroup("Gates");
        ribbon.addGroup("Tools");

        // Inputs - 5 Felder
        for (ComponentConfig.ComponentItem item : ComponentConfig.getInputComponents(workspace)) {
            ribbon.addItem("Inputs", item.getDisplayName(), item.getTooltip(), item.getImageName(), item.componentInstance);
        }

        // Outputs - 2 Felder
        for (ComponentConfig.ComponentItem item : ComponentConfig.getOutputComponents(workspace)) {
            ribbon.addItem("Outputs", item.getDisplayName(), item.getTooltip(), item.getImageName(), item.componentInstance);
        }

        // Gates - 3 Felder
        for (ComponentConfig.ComponentItem item : ComponentConfig.getGateComponents(workspace)) {
            ribbon.addItem("Gates", item.getDisplayName(), item.getTooltip(), item.getImageName(), item.componentInstance);
        }

        Button clearAll = new Button ("Reset");
        clearAll.setPrefSize(60, 30);
        clearAll.setFont((Font.font("Aptos", FontWeight.NORMAL, 10)));
        clearAll.setAlignment(Pos.CENTER);
        ComponentRegistry.ComponentMetadata resetMeta = ComponentRegistry.getMetadata("ResetButton");
        if (resetMeta != null) {
            javafx.scene.image.Image resetIcon = ImageLoader.loadImageAsImage(resetMeta.imagePath);
            if (resetIcon != null) {
                ImageView iconView = new ImageView(resetIcon);
                iconView.setFitWidth(resetMeta.iconSize);
                iconView.setPreserveRatio(true);
                clearAll.setGraphic(iconView);
                clearAll.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
            }
        }
        clearAll.setOnAction(event -> {
            workspace.clearAll();
        });


        Button exportToJson = new Button ("Export");
        exportToJson.setPrefSize(60, 30);
        exportToJson.setFont((Font.font("Aptos", FontWeight.NORMAL, 10)));
        exportToJson.setAlignment(Pos.CENTER);
        ComponentRegistry.ComponentMetadata exportMeta = ComponentRegistry.getMetadata("ExportButton");
        if (exportMeta != null) {
            javafx.scene.image.Image exportIcon = ImageLoader.loadImageAsImage(exportMeta.imagePath);
            if (exportIcon != null) {
                ImageView iconView = new ImageView(exportIcon);
                iconView.setFitWidth(exportMeta.iconSize);
                iconView.setPreserveRatio(true);
                exportToJson.setGraphic(iconView);
                exportToJson.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
            }
        }
        exportToJson.setOnAction(event -> {
            DirectoryChooser dir_chooser = new DirectoryChooser();
            File file = dir_chooser.showDialog(this.getRoot().getScene().getWindow());
            if (file != null) {
                String path = file.getAbsolutePath() + "/workspace_" + Instant.now().toEpochMilli() + ".json";
                System.out.println(path);
                try {
                    WorkspaceDTO newDTO = workspace.toWorkspaceDTO();
                    WorkspaceExporter exporter = new WorkspaceExporter();
                    exporter.exportToJson(newDTO, new File(path));
                    System.out.println("Export completed");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Button importFromJson = new Button ("Import");
        importFromJson.setPrefSize(60, 30);
        importFromJson.setFont((Font.font("Aptos", FontWeight.NORMAL, 10)));
        importFromJson.setAlignment(Pos.CENTER);
        ComponentRegistry.ComponentMetadata importMeta = ComponentRegistry.getMetadata("ImportButton");
        if (importMeta != null) {
            javafx.scene.image.Image importIcon = ImageLoader.loadImageAsImage(importMeta.imagePath);
            if (importIcon != null) {
                ImageView iconView = new ImageView(importIcon);
                iconView.setFitWidth(importMeta.iconSize);
                iconView.setPreserveRatio(true);
                importFromJson.setGraphic(iconView);
                importFromJson.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
            }
        }
        importFromJson.setOnAction(event -> {
            FileChooser dir_chooser = new FileChooser();
            File file = dir_chooser.showOpenDialog(this.getRoot().getScene().getWindow());
            if (file != null) {
                String path = file.getAbsolutePath();
                System.out.println(path);
                try {
                    WorkspaceImporter importer = new WorkspaceImporter();
                    WorkspaceDTO workspaceDTO = importer.importFromJson(new File(path));
                    workspace.fromWorkspaceDTO(workspaceDTO);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        ribbon.getGroupIconBox("Tools").getChildren().add(clearAll);
        ribbon.getGroupIconBox("Tools").getChildren().add(exportToJson);
        ribbon.getGroupIconBox("Tools").getChildren().add(importFromJson);

        ribbon.getGroupIconBox("Tools").setPrefHeight(60);

        root.setTop(ribbon);
    }

    private void setupWorkspace() {
//        workspace = new Pane();
//        workspace.setStyle("-fx-background-color: white;");
//        workspace.getChildren().add(drawGrid(2000, 2000));
//        workspace.setOnDragOver(event -> {
//            if (event.getGestureSource() != workspace && event.getDragboard().hasString()) {
//                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
//            }
//            event.consume();
//        });
//
//        workspace.setOnDragDropped(event -> {
//            Dragboard db = event.getDragboard();
//            if (db.hasString()) {
//                double snapX = Math.round(event.getX() / GRID_SIZE) * GRID_SIZE;
//                double snapY = Math.round(event.getY() / GRID_SIZE) * GRID_SIZE;
//                DraggableGate gate = new DraggableGate(db.getString(), this);
//                gate.setLayoutX(snapX);
//                gate.setLayoutY(snapY);
//                workspace.getChildren().add(gate);
//                event.setDropCompleted(true);
//            }
//            event.consume();
//        });
//
//        workspace.getChildren().add(new ButtonComponent("BTN 1"));

        root.setCenter(workspace);
    }

    private Canvas drawGrid(double width, double height) {
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.web("#E0E0E0"));
        gc.setLineWidth(1);
        for (double x = 0; x < width; x += GRID_SIZE) gc.strokeLine(x, 0, x, height);
        for (double y = 0; y < height; y += GRID_SIZE) gc.strokeLine(0, y, width, y);
        return canvas;
    }

    private VBox createRibbonSection(String title, Node content) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.TOP_LEFT);
        Label lbl = new Label(title);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        lbl.setTextFill(Color.web("#666666"));
        Separator line = new Separator();
        line.setOpacity(0.6);
        box.getChildren().addAll(lbl, line, content);
        return box;
    }

    private Button createImageButton(String tooltipText, String type, String imageName) {
        Button btn = new Button();
        btn.setTooltip(new Tooltip(tooltipText));
        btn.setPrefSize(60, 60);
        btn.setStyle("-fx-background-color: transparent; -fx-padding: 5;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #F0F0F0; -fx-background-radius: 5; -fx-padding: 5;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-padding: 5;"));

        try {
            ComponentRegistry.ComponentMetadata metadata = ComponentRegistry.getMetadata(type);
            if (metadata != null) {
                ImageView iv = ImageLoader.loadImage(metadata.imagePath, metadata.iconSize * 0.6); // 60% der Komponenten-Größe
                btn.setGraphic(iv);
            } else {
                btn.setText(type.substring(0, 2));
            }
        } catch (Exception e) {
            btn.setText("?");
        }


        btn.setOnDragDetected(event -> {
            Dragboard db = btn.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(type);
            db.setContent(content);
            event.consume();
        });
        return btn;
    }
}