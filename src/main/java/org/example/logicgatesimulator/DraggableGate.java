package org.example.logicgatesimulator;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DraggableGate extends StackPane {
    private double mouseAnchorX, mouseAnchorY;

    // --- CONNECTED WIRES (The Inputs and Outputs) ---
    private List<LogicWire> inputWires = new ArrayList<>();
    private List<LogicWire> outputWires = new ArrayList<>();

    // LOGIC STATE
    private boolean outputState = false;
    private String componentType;
    private ImageView imageView;
    public double width = 80, height = 60;
    private SimulatorUI uiContext;
    private boolean isDragging = false;

    public DraggableGate(String type, SimulatorUI context) {
        this.componentType = type;
        this.uiContext = context;

        // 1. Setup Image
        updateImage();
        if (imageView == null) {
            Rectangle fallback = new Rectangle(80, 50, Color.WHITE);
            fallback.setStroke(Color.BLACK);
            Text t = new Text(type);
            this.getChildren().addAll(fallback, t);
        }

        // 2. Setup Ports
        configurePorts(type);

        // 3. Initial Logic State (Constants are always active)
        if (type.equals("CONST1")) outputState = true;
        if (type.equals("CONST0")) outputState = false;

        // 4. Interaction
        setupHandlers();
    }

    // --- THE BRAIN: CALCULATE LOGIC ---
    public void calculateLogic() {
        boolean result = false;

        // Read signals from input wires
        boolean in1 = inputWires.size() > 0 && inputWires.get(0).getState();
        boolean in2 = inputWires.size() > 1 && inputWires.get(1).getState();

        switch (componentType) {
            case "AND": result = in1 && in2; break;
            case "OR":  result = in1 || in2; break;
            case "NOT": result = !in1; break;
            case "BUFFER": result = in1; break;
            case "CONST1": result = true; break;
            case "CONST0": result = false; break;
            case "SWITCH": result = outputState; break; // Switch remembers its state
            case "CLOCK":  result = outputState; break;
            case "LIGHT":
                // Lights don't output, they just glow
                if (in1) this.setEffect(new DropShadow(30, Color.RED));
                else this.setEffect(null);
                return; // Stop here for lights
            case "DIGIT":
                // (Optional) Code for digit display could go here
                return;
        }

        // Send result to all output wires
        this.outputState = result;
        for (LogicWire wire : outputWires) {
            wire.setSignal(outputState);
        }
    }

    private void toggleSwitch() {
        outputState = !outputState;
        updateImage();
        calculateLogic(); // Trigger the wire update!
    }

    private void updateImage() {
        String imageName = componentType.toLowerCase();
        if (componentType.equals("SWITCH")) {
            imageName = outputState ? "switch_on.png" : "switch_off.png";
        } else {
            imageName += ".png";
        }
        try {
            InputStream is = getClass().getResourceAsStream("/images/" + imageName);
            if (is == null && componentType.equals("SWITCH")) is = getClass().getResourceAsStream("/images/switch.png");
            if (is != null) {
                if (imageView != null) this.getChildren().remove(imageView);
                imageView = new ImageView(new Image(is));
                imageView.setFitWidth(80); imageView.setPreserveRatio(true);
                if (this.getChildren().size() > 0) this.getChildren().add(0, imageView);
                else this.getChildren().add(imageView);
            }
        } catch (Exception e) {}
    }

    // --- Wiring Helpers ---
    public void addInputWire(LogicWire wire) { inputWires.add(wire); calculateLogic(); }
    public void addOutputWire(LogicWire wire) { outputWires.add(wire); }

    public void removeConnections() {
        for (LogicWire w : inputWires) uiContext.removeWire(w);
        for (LogicWire w : outputWires) uiContext.removeWire(w);
    }

    private void setupHandlers() {
        this.setOnMousePressed(event -> {
            mouseAnchorX = event.getX(); mouseAnchorY = event.getY();
            isDragging = false;
            this.toFront();
            if (!(event.getTarget() instanceof Circle)) uiContext.resetSelection();
        });
        this.setOnMouseDragged(event -> {
            isDragging = true;
            double dragX = event.getSceneX() - uiContext.getWorkspace().getLayoutX() - mouseAnchorX;
            double dragY = event.getSceneY() - uiContext.getWorkspace().getLayoutY() - mouseAnchorY;
            this.setLayoutX(Math.round(dragX / 20) * 20);
            this.setLayoutY(Math.round(dragY / 20) * 20);
        });
        this.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                removeConnections();
                uiContext.removeGate(this);
            } else if (e.getButton() == MouseButton.PRIMARY && !isDragging) {
                if (componentType.equals("SWITCH") && e.getClickCount() == 1) toggleSwitch();
            }
        });
    }

    private void configurePorts(String type) {
        if (type.equals("AND") || type.equals("OR")) {
            addPort(false, -40, -15); addPort(false, -40, 15); addPort(true, 40, 0);
        } else if (type.equals("BUFFER") || type.equals("NOT")) {
            addPort(false, -40, 0); addPort(true, 40, 0);
        } else if (type.equals("SWITCH") || type.equals("CLOCK") || type.equals("CONST0") || type.equals("CONST1")) {
            addPort(true, 40, 0);
        } else if (type.equals("LIGHT") || type.equals("DIGIT")) {
            addPort(false, -40, 0);
        }
    }

    // ... inside DraggableGate class ...

    private void addPort(boolean isOutput, double xOffset, double yOffset) {
        Circle port = new Circle(6, Color.BLACK); // Dots
        port.setTranslateX(xOffset);
        port.setTranslateY(yOffset);

        // Bigger invisible stroke to make clicking easier
        port.setStroke(Color.TRANSPARENT);
        port.setStrokeWidth(8);

        // --- 1. START DRAGGING WIRE ---
        port.setOnDragDetected(e -> {
            e.consume();
            // Tell JavaFX to start a "Full Drag" (Node-to-Node)
            port.startFullDrag();
            // Tell our UI to draw the temp line
            uiContext.startWireDrag(this, port);
        });

        // --- 2. UPDATE DRAGGING VISUALS ---
        port.setOnMouseDragged(e -> {
            e.consume();
            // Calculate absolute position for the line end
            double dragX = this.getLayoutX() + this.width/2 + port.getTranslateX() + e.getX();
            double dragY = this.getLayoutY() + this.height/2 + port.getTranslateY() + e.getY();
            uiContext.updateWireDrag(dragX, dragY);
        });

        // --- 3. FINISH DRAGGING (Target Port) ---
        // This fires on the port you DROP onto
        port.setOnMouseDragReleased(e -> {
            e.consume();
            uiContext.completeWireDrag(this, port, isOutput);
        });

        this.getChildren().add(port);
    }
}
