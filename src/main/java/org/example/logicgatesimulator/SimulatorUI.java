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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.InputStream;

public class SimulatorUI {
    private BorderPane root;
    private Pane workspace;
    private final int GRID_SIZE = 20;

    // --- WIRING STATE (New Dragging Logic) ---
    private Line tempLine = null; // The visual line you see while dragging
    private Circle startPort = null;
    private DraggableGate startGate = null;

    public SimulatorUI() {
        root = new BorderPane();
        setupRibbon();
        setupWorkspace();
    }

    public BorderPane getRoot() { return root; }
    public Pane getWorkspace() { return workspace; }

    // --- 1. DRAG WIRING LOGIC (Start, Move, Finish) ---

    // Called when you start dragging a port
    public void startWireDrag(DraggableGate gate, Circle port) {
        this.startGate = gate;
        this.startPort = port;

        // Create a temporary dashed line
        tempLine = new Line();
        tempLine.setStrokeWidth(2);
        tempLine.setStroke(Color.GRAY);
        tempLine.getStrokeDashArray().addAll(5d, 5d); // Dashed pattern

        // Start point is fixed to the port
        tempLine.setStartX(gate.getLayoutX() + gate.width/2 + port.getTranslateX());
        tempLine.setStartY(gate.getLayoutY() + gate.height/2 + port.getTranslateY());

        // End point follows mouse (initially same as start)
        tempLine.setEndX(tempLine.getStartX());
        tempLine.setEndY(tempLine.getStartY());

        workspace.getChildren().add(tempLine);
    }

    // Called while you are moving the mouse
    public void updateWireDrag(double mouseX, double mouseY) {
        if (tempLine != null) {
            // Need to convert local mouse coordinates to workspace coordinates
            // (Passed from DraggableGate, which does the calculation)
            tempLine.setEndX(mouseX);
            tempLine.setEndY(mouseY);
        }
    }

    // Called when you release the mouse over a target port
    public void completeWireDrag(DraggableGate endGate, Circle endPort, boolean isOutput) {
        if (tempLine == null) return; // Safety check

        // Clean up temp line
        workspace.getChildren().remove(tempLine);
        tempLine = null;

        // LOGIC CHECK:
        // 1. Don't connect to yourself
        // 2. Don't connect Output to Output (or Input to Input)
        // 3. One side must be Input, one side Output

        // Assume startPort is Output (Source). If not, we swap logic or block it.
        // For simplicity: We only allow dragging FROM Output TO Input in this logic
        // But let's handle both directions:

        boolean startIsOutput = (startPort.getTranslateX() > 0); // Hack: Right side is output

        if (startIsOutput && !isOutput) {
            // Valid: Output -> Input
            drawWire(startGate, startPort, endGate, endPort);
        } else if (!startIsOutput && isOutput) {
            // Valid: Input -> Output (User dragged backwards)
            drawWire(endGate, endPort, startGate, startPort);
        } else {
            System.out.println("Invalid Connection: Must connect Output to Input.");
        }

        // Reset
        startGate = null;
        startPort = null;
    }

    // Called if you drop the wire in empty space
    public void cancelWireDrag() {
        if (tempLine != null) {
            workspace.getChildren().remove(tempLine);
            tempLine = null;
        }
        startGate = null;
        startPort = null;
    }

    private void drawWire(DraggableGate startGate, Circle startPort, DraggableGate endGate, Circle endPort) {
        LogicWire wire = new LogicWire(startGate, startPort, endGate, endPort);

        if (workspace.getChildren().size() > 1) workspace.getChildren().add(1, wire);
        else workspace.getChildren().add(wire);

        startGate.addOutputWire(wire);
        endGate.addInputWire(wire);
        startGate.calculateLogic();

        wire.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                removeWire(wire);
            }
        });
    }

    public void removeWire(Line wire) { workspace.getChildren().remove(wire); }
    public void removeGate(DraggableGate gate) { workspace.getChildren().remove(gate); }

    // --- SETUP CODE ---
    // ---- The top ribbon where our symbols are
    private void setupRibbon() {
        HBox ribbon = new HBox(20);
        ribbon.setPadding(new Insets(10));
        ribbon.setStyle("-fx-background-color: white; -fx-border-color: #DDDDDD; -fx-border-width: 0 0 1 0;");
        ribbon.setPrefHeight(120);
        ribbon.setAlignment(Pos.CENTER_LEFT);

        HBox inputsGroup = new HBox(5);
        inputsGroup.getChildren().addAll(
                createImageButton("Toggle Button", "SWITCH", "switch.png"),
                createImageButton("Clock", "CLOCK", "clock.png"),
                createImageButton("Constant 0", "CONST0", "const0.png"),
                createImageButton("Constant 1", "CONST1", "const1.png")
        );

        HBox outputsGroup = new HBox(5);
        outputsGroup.getChildren().addAll(
                createImageButton("Light", "LIGHT", "light.png"),
                createImageButton("X-Bit Digit", "DIGIT", "digit.png")
        );

        HBox gatesGroup = new HBox(5);
        gatesGroup.getChildren().addAll(
                createImageButton("AND Gate", "AND", "and.png"),
                createImageButton("OR Gate", "OR", "or.png"),
                createImageButton("Yes/Buffer", "BUFFER", "buffer.png")
        );

        Button clearBtn = new Button("Clear");
        clearBtn.setOnAction(e -> {
            workspace.getChildren().clear();
            workspace.getChildren().add(drawGrid(2000, 2000));
        });

        ribbon.getChildren().addAll(
                createRibbonSection("INPUTS", inputsGroup),
                new Separator(javafx.geometry.Orientation.VERTICAL),
                createRibbonSection("OUTPUTS", outputsGroup),
                new Separator(javafx.geometry.Orientation.VERTICAL),
                createRibbonSection("GATES", gatesGroup),
                new Separator(javafx.geometry.Orientation.VERTICAL),
                createRibbonSection("TOOLS", new HBox(clearBtn))
        );
        root.setTop(ribbon);
    }

    private void setupWorkspace() {
        workspace = new Pane();
        workspace.setStyle("-fx-background-color: white;");
        workspace.getChildren().add(drawGrid(2000, 2000));

        workspace.setOnDragOver(event -> {
            if (event.getGestureSource() != workspace && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        workspace.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                double snapX = Math.round(event.getX() / GRID_SIZE) * GRID_SIZE;
                double snapY = Math.round(event.getY() / GRID_SIZE) * GRID_SIZE;
                DraggableGate gate = new DraggableGate(db.getString(), this);
                gate.setLayoutX(snapX);
                gate.setLayoutY(snapY);
                workspace.getChildren().add(gate);
                event.setDropCompleted(true);
            }
            event.consume();
        });

        // Cancel wire drag if you release mouse on empty space
        workspace.setOnMouseReleased(e -> cancelWireDrag());

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
        box.setAlignment(Pos.TOP_CENTER);
        Label lbl = new Label(title);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        lbl.setTextFill(Color.web("#666666"));
        box.getChildren().addAll(lbl, content);
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

    // --- SELECTION HELPER ---
    public void resetSelection() {
        // Just cancel any active drag operation
        cancelWireDrag();
    }
}