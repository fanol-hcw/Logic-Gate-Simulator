package org.example.logicgatesimulator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.logicgatesimulator.components.*;

import java.io.InputStream;

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
        setupRibbon();
        setupWorkspace();

    }

    public BorderPane getRoot() { return root; }

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
        ribbon.addItem("Inputs", "Button", "Button",  "switch.png", new ButtonComponent("Button", workspace));
        ribbon.addItem("Outputs", "Led", "Led", "light.png", new LedComponent("Led", workspace));
        ribbon.addItem("Gates", "And Gate", "And Gate", "and.png", new AndGateComponent("And Gate", workspace));
        ribbon.addItem("Gates", "Or Gate", "Or Gate", "or.png", new OrGateComponent("Or Gate", workspace));
        ribbon.addItem("Gates", "Not Gate", "Not Gate", "not.png", new NotGateComponent("Not Gate", workspace));

        ribbon.addGroup("Tools");
        Button clearAll = new Button ("Reset");
        clearAll.setPrefSize(60, 30);
        clearAll.setFont((Font.font("Aptos", FontWeight.NORMAL, 10)));
        clearAll.setAlignment(Pos.CENTER);
        clearAll.setOnAction(e -> {
            workspace.clearAll();
        });
        ribbon.getGroupIconBox("Tools").getChildren().add(clearAll);
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
            InputStream is = getClass().getResourceAsStream("/images/" + imageName);
            if (is != null) {
                ImageView iv = new ImageView(new Image(is));
                iv.setFitWidth(50); iv.setPreserveRatio(true);
                btn.setGraphic(iv);
            } else {
                btn.setText(type.substring(0, 2));
            }
        } catch (Exception e) { btn.setText("?"); }

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