Project Documentation: Logic Gate Simulator
1. Overview
   The Logic Gate Simulator is an interactive JavaFX application that allows users to design and simulate digital logic circuits. 
    The program provides a Graphical User Interface (GUI) to place logic gates (such as AND, OR, NOT) via drag-and-drop, wire them together, and observe the circuit behavior in real-time.

The architecture strictly separates the Simulation (Logic) from the Visualization (UI).

2. Program Flow & Startup
   2.1 Startup Sequence
   The entry point of the application is the LogicGataSimulatorApplication class.

Splash Screen: Upon launch, a splash screen with a logo and progress bar is displayed to provide feedback while components initialize.

Launcher: The Launcher class serves as a wrapper to safely start the JavaFX application.

2.2 Start Menu
After the splash screen, the start menu (StartMenuController) appears.

Functions: The user can create a blank project or open an existing .json file.

Recent Files: A list of recently opened files is displayed and persisted in the recentGatesFiles.conf file.

3. User Interface (UI)
   The main interface is managed by the SimulatorUI class and consists of two main areas:

3.1 The Ribbon (Toolbar)
Located at the top is the Ribbon, which contains all available components.

Categories: Components are organized into groups: Inputs, Outputs, Gates, and Tools.

Drag & Drop: Each icon in the ribbon is a Button. Dragging one initiates a Dragboard event, transferring the component's class name.

3.2 The Workspace
The Workspace is the canvas where the circuit is built.

Grid: A grid helps with component alignment. When a component is released, it automatically snaps to the grid.

Wiring: Users can draw wires using the right mouse button. The ComponentBase class detects when a line ends on another component and establishes the logical connection.

4. Components
   Every visible component on the screen (e.g., an AND gate) is a combination of visual representation and logical function.

4.1 Base Classes
DraggableGate: This class enables object movement via mouse.and provides a context menu for deletion (right-click). It also updates the component's image based on its state (e.g., light on/off).

ComponentBase: Manages mouse events for wiring (right-click) and stores the incoming and outgoing connection lines.

4.2 Specific Components
Inputs:

Button: Sends a True signal while pressed. Sends False when released.

Clock: Toggles automatically between On and Off. A click starts/stops the clock; a middle-click opens a dialog to set the speed (in ms).

Constants: OneConstantComponent permanently sends a True signal.

Outputs:

LED: Visualizes the current state (On/Off) by swapping the image resource.

Gates: Components like AndGateComponent or OrGateComponent represent the logical operations.

4.3 Configuration
The ComponentRegistry serves as a central directory. It stores display names, tooltips, image paths, and icon sizes for all components, making it easy to register new ones.

5. Simulation Engine
   The core logic runs in the background (backend), separated from the graphics.

5.1 Logic Elements
The abstract class LogicElement defines the behavior of all logic blocks.

Calculation: Each element has a calculateNewOutput() method. This is executed whenever an input changes.

Signals: If the output value changes, the element notifies all connected downstream consumers.

5.2 The Runner
The Runner is the heart of the simulation.

Mechanism: It processes a Queue of updates. When a switch is toggled, the update is added to the Runner.

Protection: To prevent infinite loops (e.g., in short circuits), the Runner stops calculation after a safety limit of 1000 steps.

6. Persistence (Saving & Loading)
   The project uses JSON format to store circuits.

Export: The WorkspaceExporter converts the current objects in the Workspace into a WorkspaceDTO (Data Transfer Object), saving positions, types, and connections.

Import: The WorkspaceImporter reads the JSON file, clears the current workspace, and reconstructs the components at the saved positions.

7. Architecture Summary
   User Action: The user presses a button in the UI (ButtonComponent).

Logic Update: The button sets its logical value in the backend (ButtonElement) to true and informs the Runner.

Simulation: The Runner instructs all connected gates to re-evaluate their inputs (calculateNewOutput).

Propagation: The signal travels through the connections. If a state changes (e.g., LED turns on), a callback is sent to the UI.

Visualization: The UI (LedComponent) swaps its image (LedOn.png) so the user sees the result.