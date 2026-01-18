# Table of Contents

* [Part 1: Project Overview, Core Concepts, and Component Configuration](#part-1-project-overview-core-concepts-and-component-configuration)
    * [1. Project Overview](#1-project-overview)
    * [2. Core Concepts](#2-core-concepts)
        * [2.1 ComponentBase and LogicElement](#21-componentbase-and-logicelement)
        * [2.2 ComponentConfig](#22-componentconfig)
        * [2.3 ComponentEventHolder](#23-componenteventholder)
        * [2.4 StartMenuController](#24-startmenucontroller)
        * [2.5 Ribbon](#25-ribbon)
        * [2.6 SimulatorUI](#26-simulatorui)
        * [2.7 Workspace](#27-workspace)
* [Part 2: Simulation Engine, Logic Elements, and DTOs](#part-2-simulation-engine-logic-elements-and-dtos)
    * [3. Logic Simulation Layer](#3-logic-simulation-layer)
        * [3.1 LogicElement (Abstract Class)](#31-logicelement-abstract-class)
        * [3.2 Runner (Simulation Engine)](#32-runner-simulation-engine)
    * [4. Data Transfer Objects (DTOs)](#4-data-transfer-objects-dtos)
        * [4.1 ComponentDTO](#41-componentdto)
        * [4.2 ConnectionDTO](#42-connectiondto)
        * [4.3 WorkspaceDTO](#43-workspacedto)
    * [5. Component Connections](#5-component-connections)
    * [6. Summary of Simulation Flow](#6-summary-of-simulation-flow)
* [Part 3: User Interface, Ribbon, Workspace, and Interaction](#part-3-user-interface-ribbon-workspace-and-interaction)
    * [UI Layout Structure](#ui-layout-structure)
    * [8. Ribbon (Component Toolbar)](#8-ribbon-component-toolbar)
        * [8.1 Structure](#81-structure)
        * [8.2 Key Methods](#82-key-methods)
        * [8.3 Ribbon Buttons and Drag-and-Drop](#83-ribbon-buttons-and-drag-and-drop)
    * [9. SimulatorUI](#9-simulatorui)
        * [9.1 Key Fields](#91-key-fields)
        * [9.2 Key Methods](#92-key-methods)
        * [9.3 Tools in Ribbon](#93-tools-in-ribbon)
    * [10. Workspace](#10-workspace)
        * [10.1 Core Responsibilities](#101-core-responsibilities)
        * [10.2 Key Features](#102-key-features)
        * [10.3 Drag-and-Drop Behavior](#103-drag-and-drop-behavior)
* [Part 4: Logic Simulation, Components, and Runner](#part-4-logic-simulation-components-and-runner)
    * [11. LogicElement (Core Logic Unit)](#11-logicelement-core-logic-unit)
    * [12. Runner (Simulation Engine)](#12-runner-simulation-engine)
    * [13. ComponentBase (Bridge Between UI and Logic)](#13-componentbase-bridge-between-ui-and-logic)
    * [14. Component Connections](#14-component-connections)
* [Part 5: Serialization, DTOs, and JSON Import/Export](#part-5-serialization-dtos-and-json-importexport)
    * [15. Overview of Serialization](#15-overview-of-serialization)
    * [16. ComponentDTO](#16-componentdto)
    * [17. ConnectionDTO](#17-connectiondto)
    * [18. WorkspaceDTO](#18-workspacedto)
    * [19. Workspace Export](#19-workspace-export)
    * [20. Workspace Import](#20-workspace-import)
    * [21. Integration with UI](#21-integration-with-ui)
    * [22. Benefits of DTO Design](#22-benefits-of-dto-design)
* [Part 6: Component Library, Ribbon, and Tool Integration](#part-6-component-library-ribbon-and-tool-integration)
    * [23. Component Library Overview](#23-component-library-overview)
    * [24. ComponentRegistry](#24-componentregistry)
    * [25. ComponentConfig](#25-componentconfig)
    * [26. Ribbon Item Creation](#26-ribbon-item-creation)
    * [27. Ribbon Groups](#27-ribbon-groups)
    * [28. Tool Buttons](#28-tool-buttons)
    * [29. Workspace Integration](#29-workspace-integration)
    * [30. Summary of Component + Ribbon Flow](#30-summary-of-component--ribbon-flow)

# Project Documentation: Logic Gate Simulator

## 1. Project Overview
**Part 1: Project Overview, Core Concepts, and Component Configuration**

The **Logic Gate Simulator** is a JavaFX-based simulation tool that allows users to design and test digital logic circuits. Users can drag and drop components (inputs, outputs, gates) into a workspace, connect them visually, and observe how the logic propagates through the circuit.

### Key Features
* **Ribbon menu** for selecting logic components
* **Workspace** for placing, moving, and connecting components
* **Logic simulation** with propagation and updates
* **JSON import/export** for saving and loading circuit configurations
* **Start menu** with recent project tracking

The project is structured into multiple layers:
* **UI**
* **Workspace**
* **Components**
* **Simulation Engine**
* **DTOs (Data Transfer Objects)**

---

## 2. Core Concepts

### 2.1 ComponentBase and LogicElement

**ComponentBase** represents a visual gate or device in the workspace (for example: `ButtonComponent`, `AndGateComponent`).

**LogicElement** represents the underlying logical behavior of the component. It manages:
* **Inputs** (`LogicElement[]`)
* **Output** (`boolean`)
* **Connections** to other elements (`List<LogicElement> outputConnections`)

#### Responsibilities
* **Calculate output** based on inputs (`calculateNewOutput()`)
* **Notify downstream elements** when output changes (`onInputChanged()`)
* **Safely manage inputs** and avoid null exceptions (`getInputSafe()`)

---

### 2.2 ComponentConfig

**ComponentConfig** defines all available components and their metadata.

Each component is represented by a **ComponentItem**, which contains:
* **componentClassName** – Java class name of the component
* **componentInstance** – actual instance used in the workspace

#### Provided Functionality
* Helper methods for:
    * **Display name**
    * **Tooltip**
    * **Icon path**

* Grouped component access:
    * **getInputComponents()** – buttons, switches, clocks, constants
    * **getOutputComponents()** – LEDs
    * **getGateComponents()** – AND, OR, NOT, XOR gates

#### Example
* `new ComponentItem("ButtonComponent", new ButtonComponent("Button", workspace))`

---

### 2.3 ComponentEventHolder

**ComponentEventHolder** is a custom JavaFX `Pane` used to manage mouse events for a component.

#### Responsibilities
* Stores handlers for:
    * **mousePressed**
    * **mouseDragged**
    * **mouseReleased**
    * **mouseClicked**
    * **dragDropped**
* Executes **all attached handlers** when an event occurs
* Supports **multiple listeners per event**

---

### 2.4 StartMenuController

**StartMenuController** manages the start menu UI.

#### UI Elements
* **Buttons**
    * Open Project
    * Empty Scene
* **ListView**
    * `recentList` showing recent projects

#### Main Functions
* **initialize()** – loads recent projects and enables double-click opening
* **onBtnEmptySceneClicked()** – opens a fresh workspace
* **onBtnOpenProjectClicked()** – opens a JSON file
* **onBtnOpenRecentClicked()** – opens a selected recent project
* **onBtnDeleteRecentClicked()** – removes a recent project entry

#### Recent File Handling
* Stored in **recentGatesFiles.conf**
* Managed via:
    * **loadRecentFiles()**
    * **saveRecentFiles()**

---

### 2.5 Ribbon

**Ribbon** is a custom horizontal toolbar that organizes components into groups.

#### Responsibilities
* Holds component groups:
    * **Inputs**
    * **Outputs**
    * **Gates**
    * **Tools**
* Dynamically creates buttons with:
    * **Tooltips**
    * **Icons**
    * **Drag-and-drop support**
* Provides tools such as:
    * **Reset**
    * **Export**
    * **Import**

---

### 2.6 SimulatorUI

**SimulatorUI** is the main application UI controller.

#### Responsibilities
* Combines **MenuBar**, **Ribbon**, and **Workspace** in a `BorderPane`
* Sets up MenuBar actions:
    * **Export**
    * **Import**
    * **Close Project**
* Initializes the Ribbon using **ComponentConfig**
* Initializes the Workspace as the center pane

---

### 2.7 Workspace

**Workspace** extends `Pane` and represents the simulation canvas.

#### Responsibilities
* Stores placed components (`ArrayList<ComponentBase>`)
* Stores connections (`ArrayList<Connection>`)
* Handles drag-and-drop from the Ribbon
* Draws a background grid

#### Data Handling
* **toWorkspaceDTO()**
* **fromWorkspaceDTO()**
* **clearAll()**

## Part 2: Simulation Engine, Logic Elements, and DTOs

## 3. Logic Simulation Layer

### 3.1 LogicElement (Abstract Class)

**LogicElement** is the core abstraction for all logical components (gates, switches, outputs).

#### Responsibilities
* Manage inputs and outputs for a logic component
* Notify connected components when its output changes
* Provide a consistent interface for calculating output and updating connections

#### Key Properties
* **LogicElement[] inputs** – array of input connections
* **boolean output** – current output of the logic element
* **String name** – optional name for the element
* **List<LogicElement> outputConnections** – elements that depend on this output
* **Consumer<Boolean> onUpdate** – callback executed whenever output changes

#### Key Methods
* **calculateNewOutput()**
    * Abstract method implemented by subclasses (AND, OR, NOT, XOR)

* **onInputChanged()**
    * Calculates new output
    * Updates output if it changed
    * Notifies the `onUpdate` callback
    * Schedules updates for all connected elements using **Runner**

* **setInput(int index, LogicElement element)**
    * Safely sets an input at a specific index
    * Prevents overwriting inputs without increment

* **getOutput()**
    * Returns the current output

* **getInputSafe(int index)**
    * Safely retrieves the value of an input
    * Returns `false` if the input is not connected

* **getInputIndex(LogicElement inputElement)**
    * Helper method to identify which input index a connected element uses

#### Usage
* Every gate component (AND, OR, XOR, NOT) extends **LogicElement**
* Each gate implements **calculateNewOutput()**
* Output changes automatically propagate through connected elements

---

### 3.2 Runner (Simulation Engine)

**Runner** manages the logic update queue and ensures correct propagation of circuit state.

#### Design
* Uses the **Singleton pattern** (`getInstance()`)
* Only one simulation runner exists
* Maintains a `Queue<LogicElement>` called **updateQueue**

#### Responsibilities
* **scheduleUpdate(LogicElement element)** – adds a single element to the queue
* **scheduleUpdates(List<LogicElement> elements)** – schedules multiple updates
* **step()**
    * Iteratively dequeues elements
    * Calls `onInputChanged()` on each
    * Limits processing to **1000 iterations** to prevent infinite loops (oscillations)

#### Purpose
* Automatically propagates logic changes
* Ensures consistent and stable simulation behavior

---

## 4. Data Transfer Objects (DTOs)

DTOs are used to serialize and deserialize the workspace during import and export.

### 4.1 ComponentDTO

Represents a single component in the workspace.

#### Properties
* **String id** – unique identifier
* **String componentType** – class or type name
* **String name** – display name
* **double x, y** – position in the workspace
* **Boolean state** – logical state for inputs and outputs

#### Methods
* Standard getters and setters
* Used by `Workspace.toWorkspaceDTO()` for JSON export

---

### 4.2 ConnectionDTO

Represents a connection between two components.

#### Properties
* **String fromId** – ID of the source component
* **String toId** – ID of the target component
* **int inputIndex** – input index on the target component
* **double startX, startY, endX, endY** – line rendering positions

#### Methods
* Standard getters and setters
* Used by:
    * `Workspace.toWorkspaceDTO()`
    * `Workspace.fromWorkspaceDTO()`

---

### 4.3 WorkspaceDTO

Represents the entire workspace state.

#### Properties
* **List<ComponentDTO> components** – all workspace components
* **List<ConnectionDTO> connections** – all component connections

#### Methods
* Standard getters and setters
* Acts as the root object for JSON import and export

---

## 5. Component Connections

Connections exist on two levels:

* **Visual**
    * Represented by `Line` objects in the Workspace

* **Logical**
    * Represented by `LogicElement` connections via `outputConnections`

#### Workflow
* User drags a component into the workspace
* Component is added to `Workspace.components`
* User creates a connection
    * `Workspace.addConnection(from, to)` creates a `Line`
    * Connection is stored in `connections`
* `LogicElement.setInput(index, element)` updates logical links
* Logic changes propagate via:
    * `Runner.scheduleUpdate()`
    * `LogicElement.onInputChanged()`

#### Deleting Components
* Component removed from `Workspace.components`
* Associated visual lines removed
* Logical connections cleaned up to prevent stale references

---

## 6. Summary of Simulation Flow

* Components are placed in the **Workspace**
* Ribbon buttons drag-and-drop new components
* Each component contains a **LogicElement**
* Connections update both:
    * Visual lines
    * Logical outputs
* When an input changes:
    * `LogicElement.onInputChanged()` recalculates output
    * **Runner** schedules downstream updates
* Logic propagates until the circuit stabilizes
* Workspace can be exported to JSON using **WorkspaceDTO**
* Projects can be reloaded with identical layout and connections

## Part 3: User Interface, Ribbon, Workspace, and Interaction

The **Logic Gate Simulator** user interface is built using **JavaFX** and structured around a `BorderPane`.

### UI Layout Structure

BorderPane  
├─ **Top:** MenuBar / Ribbon  
├─ **Center:** Workspace (Canvas + Components)  
└─ **Optional:** Status Bar (not implemented yet)

* **MenuBar** – standard file operations (Import, Export, Close Project)
* **Ribbon** – categorized toolbar for components and tools
* **Workspace** – main canvas where users place components and create connections

---

## 8. Ribbon (Component Toolbar)

The **Ribbon** is a custom `HBox` containing groups of components (**Inputs, Outputs, Gates, Tools**).

### 8.1 Structure

* **Groups**
    * Stored in `Hashtable<String, ArrayList<Node>> groups`
    * Logical representation of each ribbon group

* **Group Components**
    * Stored in `Hashtable<String, HBox> groupComponents`
    * Each group has its own `HBox` holding buttons or icons

* **Drag-and-Drop**
    * Each component button supports drag-and-drop into the workspace

* **Styling**
    * Rounded borders
    * Shadows
    * Padding
    * Hover effects for better usability

---

### 8.2 Key Methods

#### addGroup(String name)

* Adds a new group to the ribbon
* Creates:
    * An `HBox` for icons
    * A `VBox` container with the group label

#### addItem(String groupName, String name, String tooltipText, String imageName, ComponentBase referencedComponent)

* Adds a new component button to a ribbon group
* Loads icon using **ImageLoader** if available
* Configures drag-and-drop behavior
    * Transfers the **class name** of the component to the workspace

#### getGroupIconBox(String groupName)

* Returns the `HBox` containing icons for a specific group
* Used to dynamically add tool buttons (Reset, Export, Import)

---

### 8.3 Ribbon Buttons and Drag-and-Drop

* Ribbon items are buttons with:
    * Icons
    * Tooltips

* **Drag detected event**
    * Creates a `Dragboard`
    * Stores the component class name

* **Workspace as drop target**
    * Accepts the class name
    * Instantiates the component dynamically
    * Snaps the component to the grid

---

## 9. SimulatorUI

**SimulatorUI** orchestrates the entire application interface.

### 9.1 Key Fields

* **BorderPane root** – main UI container
* **Workspace workspace** – central canvas
* **GRID_SIZE** – spacing for grid snapping
* **WINDOW_WIDTH**, **WINDOW_HEIGHT** – initial window size

---

### 9.2 Key Methods

#### getRoot()

* Returns the main `BorderPane`

#### getRoot(String pathToProject)

* Loads a workspace from a JSON file
* Returns the initialized root layout

#### setupMenu()

* Initializes the file menu
    * **Export** – saves workspace as JSON
    * **Import** – loads workspace from JSON
    * **Close Project** – returns to start menu

#### setupRibbon()

* Initializes ribbon groups
* Populates groups with components and tools

#### setupWorkspace()

* Places the workspace in the center of the `BorderPane`
* Enables drag-and-drop interactions

---

### 9.3 Tools in Ribbon

* **Reset Button**
    * Clears all components and connections

* **Export Button**
    * Opens a `DirectoryChooser`
    * Exports workspace to JSON

* **Import Button**
    * Opens a `FileChooser`
    * Imports workspace from JSON

#### Visual Layout

* Buttons use icons loaded from **ComponentRegistry**
* Consistent alignment, padding, and font sizes

---

## 10. Workspace

**Workspace** extends `Pane` and acts as the main simulation canvas.

### 10.1 Core Responsibilities

* Render a background grid
* Maintain component and connection lists
* Handle drag-and-drop from the ribbon
* Support component deletion via context menu
* Manage visual and logical connections

---

### 10.2 Key Features

#### Grid Drawing

* Drawn using a `Canvas`
* Vertical and horizontal lines every **GRID_SIZE** pixels
* Ensures clean and aligned circuit layouts

#### Adding Components

* Dragging from ribbon calls:
    * `addComponent(String type, double x, double y)`
* Instantiates components using **reflection**
* Adds a context menu for deletion
* Stores component in the `components` list

#### Deleting Components

* Removes component from `components`
* Deletes all associated connections
* Cleans up visual lines

#### Adding Connections

* `addConnection(ComponentBase from, ComponentBase to)`
* Creates a visual line between ports
* Stores a `Connection` object
* Updates both source and target `LogicElement` connections

#### Import / Export Support

* Converts workspace to and from **WorkspaceDTO**
* Saves and restores:
    * Component positions
    * Connections
    * Component types
    * Logical states

#### Workspace Clearing

* `clearAll()` removes:
    * All components
    * All connections
    * Both visually and logically

---

### 10.3 Drag-and-Drop Behavior

* Workspace implements:
    * `setOnDragOver`
    * `setOnDragDropped`
* Accepts `TransferMode.COPY_OR_MOVE`
* Snaps dropped components to nearest grid point
* Supports adding multiple components sequentially without overlap

# Part 4: Logic Simulation, Components, and Runner

## 11. LogicElement (Core Logic Unit)

`LogicElement` is an abstract class representing a single logical unit (e.g., AND gate, OR gate, NOT gate).

### 11.1 Fields

| Field | Type | Purpose |
| :--- | :--- | :--- |
| `inputs` | `LogicElement[]` | Array of input elements connected to this logic element |
| `output` | `boolean` | Current output state of this element |
| `name` | `String` | Optional name, default "Unnamed" |
| `outputConnections` | `List<LogicElement>` | Logic elements that consume this element's output |
| `onUpdate` | `Consumer<Boolean>` | Callback triggered when the output changes |

### 11.2 Constructor

```java public LogicElement(int inputCount)```
* Creates the `inputs` array.
* If `inputCount` is 0, defaults to 1 for consistency.

### 11.3 Key Methods

* **`calculateNewOutput()`** – abstract: must be implemented by each specific logic gate.
    * Example: AND gate returns `inputs[0].getOutput() && inputs[1].getOutput()`.
* **`onInputChanged()`** – triggers whenever an input changes:
    * Calls `calculateNewOutput()`.
    * Compares with current output.
    * Updates output if changed and triggers `onUpdate` callback.
    * Notifies `Runner` to schedule updates for connected output elements.
* **`setInput(int index, LogicElement element)`** – connects an input element to this gate.
    * Automatically adds this element as an output connection of the input element.
    * Ensures no null pointer exceptions by checking input index.
* **`addOutputConnection(LogicElement consumer)`** – adds a dependent element that will be notified on output change.
* **`getInputSafe(int index)`** – helper for safely retrieving input values without null pointer exceptions.
* **`getInputIndex(LogicElement inputElement)`** – returns the index of a connected input, useful for serialization/export.

### 11.4 Role in Simulation

* Each `LogicElement` is a node in the logic network.
* Changes propagate through `onInputChanged()` via the `Runner` update queue.
* Decouples UI representation from simulation logic.

## 12. Runner (Simulation Engine)

`Runner` is a singleton class that manages the propagation of logic changes across all elements.

### 12.1 Fields

| Field | Type | Purpose |
| :--- | :--- | :--- |
| `instance` | `Runner` | Singleton instance |
| `updateQueue` | `Queue<LogicElement>` | Queue of logic elements pending an update |

### 12.2 Key Methods

* **`getInstance()`** – returns singleton instance of `Runner`.
* **`scheduleUpdates(List<LogicElement> elements)`** – adds multiple elements to the queue.
* **`scheduleUpdate(LogicElement element)`** – adds a single element to the queue.
* **`step()`** – processes all queued updates until the circuit stabilizes:
    * Prevents infinite loops with a safety limit (1000 iterations).
    * Polls each element from `updateQueue` and calls `onInputChanged()`.
    * Ensures that all changes propagate in a deterministic order.

### 12.3 Role in Simulation

* Maintains a stable, sequential update flow for asynchronous logic circuits.
* Ensures that any input change propagates to all dependent elements in the correct order.
* Protects against oscillations or infinite loops in unstable circuit designs.

## 13. ComponentBase (Bridge Between UI and Logic)

`ComponentBase` represents a UI component tied to a `LogicElement`. Examples include gates, input switches, and output lamps.

### 13.1 Responsibilities

* Acts as the visual representation of a `LogicElement` on the workspace.
* Provides connectable ports (Circles in UI) for wiring components.
* Bridges between drag-and-drop placement and underlying logic simulation.

### 13.2 Key Features

* **DraggableGate (extends ComponentBase)**
    * Supports dragging components around the workspace.
    * Updates layout coordinates.
    * Maintains references to connected `LogicElement` objects.
* **Connection Management**
    * Adds or removes connections visually (`Line`) and logically (`LogicElement` output connections).
    * Snap-to-grid ensures components align neatly.
* **Context Menu**
    * Right-click menu for deleting components.
    * Safely removes all associated connections and updates simulation.

## 14. Component Connections

### 14.1 Visual Connections

* Represented by JavaFX `Line` between output port of a source component and input port of a target component.
* Stored in `Workspace.Connection` objects, linking from and to components.

### 14.2 Logical Connections

* Each `ComponentBase` has a reference to a `LogicElement`.
* `LogicElement.outputConnections` lists dependent elements.
* When a connection is created in UI:
    * `Line` is drawn in workspace.
    * Target `LogicElement` adds the source element to its inputs.
    * `Runner` schedules updates for dependent elements.

### 14.3 Serialization

* Workspace → `WorkspaceDTO` → `ComponentDTO` + `ConnectionDTO`:
    * Saves component type, coordinates, state.
    * Saves connections (fromId, toId, inputIndex).
* Loading restores both visual layout and logical connections.

# Part 5: Serialization, DTOs, and JSON Import/Export

## 15. Overview of Serialization

Serialization in the Logic Gate Simulator allows saving the state of the workspace, including:

* Placed components
* Their positions
* Component states (e.g., input/output values)
* Connections between components

The system uses Data Transfer Objects (DTOs) to convert Java objects into a format suitable for JSON export/import.

## 16. ComponentDTO

`ComponentDTO` represents one logic component in the workspace for export/import.

### 16.1 Fields

| Field | Type | Description |
| :--- | :--- | :--- |
| `id` | `String` | Unique identifier for the component in this workspace session |
| `ComponentType` | `String` | Class name of the component (e.g., "ANDGate") |
| `name` | `String` | Optional human-readable name of the component |
| `x` | `double` | X-coordinate in the workspace |
| `y` | `double` | Y-coordinate in the workspace |
| `state` | `Boolean` | Current output state of the component |

### 16.2 Purpose

* Captures both UI position and logical state.
* Used during workspace export and workspace reconstruction.

### 16.3 Methods

* Standard getters and setters for all fields.
* No logic beyond storing values.

## 17. ConnectionDTO

`ConnectionDTO` represents a wire/connection between two components.

### 17.1 Fields

| Field | Type | Description |
| :--- | :--- | :--- |
| `fromId` | `String` | ID of the source component |
| `toId` | `String` | ID of the target component |
| `inputIndex` | `int` | Index of the input port on the target component |
| `startX` | `double` | X-coordinate of the start of the line (optional for visual purposes) |
| `startY` | `double` | Y-coordinate of the start of the line |
| `endX` | `double` | X-coordinate of the end of the line |
| `endY` | `double` | Y-coordinate of the end of the line |

### 17.2 Purpose

* Captures both logical connection (from/to, input index) and visual connection (line coordinates).
* Used to rebuild workspace connections on import.

## 18. WorkspaceDTO

`WorkspaceDTO` represents the full workspace including all components and connections.

### 18.1 Fields

| Field | Type | Description |
| :--- | :--- | :--- |
| `components` | `List<ComponentDTO>` | All components currently in the workspace |
| `connections` | `List<ConnectionDTO>` | All connections between components |

### 18.2 Purpose

* Encapsulates the entire workspace state.
* Serves as the single object for JSON export/import.

## 19. Workspace Export

Exporting workspace saves the current simulation state and layout as JSON.

### 19.1 Flow

* Convert `Workspace` components to `ComponentDTO` objects.
* Assign unique IDs to each component.
* Record class type, coordinates, name, and output state.
* Convert `Workspace` connections to `ConnectionDTO` objects.
* Map source and target components using IDs.
* Save input index and line coordinates.
* Wrap all DTOs in a `WorkspaceDTO`.
* Use `WorkspaceExporter` to save the DTO as JSON to a user-specified file.

### 19.2 Sample Export JSON Structure

json
{
  "components": [
    {
      "id": "Comp1",
      "ComponentType": "ANDGate",
      "name": "AND1",
      "x": 100.0,
      "y": 200.0,
      "state": false
    }
  ],
  "connections": [
    {
      "fromId": "Comp1",
      "toId": "Comp2",
      "inputIndex": 0,
      "startX": 120.0,
      "startY": 220.0,
      "endX": 250.0,
      "endY": 220.0
    }
  ]
}
## 20. Workspace Import

Importing workspace reconstructs UI + logical state from JSON.

### 20.1 Flow

* Load JSON into a `WorkspaceDTO` using `WorkspaceImporter`.
* Clear current workspace (components and connections).
* Iterate over `ComponentDTO` list:
    * Add each component to `Workspace` at saved coordinates.
    * Map component IDs to created `ComponentBase` objects.
* Iterate over `ConnectionDTO` list:
    * Connect source and target components using stored IDs and `inputIndex`.
    * Draw connecting lines in workspace.
    * Rebuild `LogicElement` connections for simulation propagation.

## 21. Integration with UI

* Ribbon buttons trigger import/export operations.
* "Export" → opens `DirectoryChooser` → calls `workspace.toWorkspaceDTO()` → writes JSON.
* "Import" → opens `FileChooser` → reads JSON → calls `workspace.fromWorkspaceDTO()`.
* This ensures that user layout, component state, and circuit connections persist between sessions.

## 22. Benefits of DTO Design

* Decouples UI representation from simulation logic.
* Allows safe serialization of workspace state.
* Supports versioning, future updates, and undo/redo mechanisms.
* Simplifies unit testing and validation of saved circuits.

# Part 6: Component Library, Ribbon, and Tool Integration

## 23. Component Library Overview

The simulator includes a component library which defines all available inputs, outputs, and logic gates for the user to place in the workspace.

This is handled through:

* **`ComponentBase`** – abstract base class for all components placed in the workspace.
* **`ComponentConfig`** – configuration provider listing all components by category.
* **`ComponentRegistry`** – metadata repository storing image paths, icon sizes, and types for all components.

## 24. ComponentRegistry

`ComponentRegistry` stores metadata for all components, allowing the Ribbon to dynamically load and display buttons.

### 24.1 Stored Metadata (ComponentMetadata)

| Field | Description |
| :--- | :--- |
| `imagePath` | path to the component’s icon |
| `iconSize` | size of the icon in the Ribbon |
| `className` | the Java class name representing the component |

### 24.2 Purpose

* Ensures the Ribbon can automatically generate buttons for all components.
* Centralizes component data, avoiding hardcoding image paths or sizes.
* Supports dynamic creation of new components without modifying Ribbon code.

## 25. ComponentConfig

`ComponentConfig` organizes components into categories for the Ribbon:

* **Inputs** – user-driven inputs, e.g., switches or toggles.
* **Outputs** – monitors or output indicators, e.g., lamps or LEDs.
* **Gates** – logic gates like AND, OR, NOT, XOR.

### 25.1 Example Structure

java
public static List<ComponentItem> getInputComponents(Workspace workspace) {...}
public static List<ComponentItem> getOutputComponents(Workspace workspace) {...}
public static List<ComponentItem> getGateComponents(Workspace workspace) {...}

Returns a list of `ComponentItem` objects, which contain:

* Display name
* Tooltip text
* Image file name
* Instance of the component

This allows Ribbon creation to iterate over each category and add buttons dynamically.

## 26. Ribbon Item Creation

The `Ribbon` class handles adding buttons/icons for each component:

### 26.1 `addItem()` Method

java
ribbon.addItem("Gates", "AND", "AND Gate", "and_icon.png", new ANDGate());

**Steps performed:**

* Retrieve component metadata from `ComponentRegistry`.
* Create a button with tooltip and size (matching Workspace icon size).
* Add mouse events for drag-and-drop:
    * On drag detected → start a `Dragboard` transfer
    * Pass the component class name as drag content
    * Use the component icon as drag preview
* Add button to the corresponding Ribbon group (Inputs, Outputs, Gates, Tools).

### 26.2 Drag-and-Drop Workflow

* User drags button from Ribbon → Workspace.
* Workspace listens for `DragEvent`:
    * Reads class name from `Dragboard`
    * Instantiates a new component dynamically using reflection
    * Snaps component to grid
    * Adds component to workspace and logic simulation

## 27. Ribbon Groups

Ribbon items are grouped visually and functionally using `Ribbon.addGroup()`:

* Each group has a `VBox` containing:
    * Group label (bold)
    * `HBox` containing buttons/icons
* Example groups:
    * "Inputs"
    * "Outputs"
    * "Gates"
    * "Tools"

Groups ensure organized and intuitive layout for the user.

## 28. Tool Buttons

The "Tools" group contains workspace management buttons, including:

* **Reset**
    * Clears all components and connections from the workspace.
    * Calls `workspace.clearAll()`.
* **Export**
    * Saves current workspace state to JSON.
    * Invokes `WorkspaceExporter` using `workspace.toWorkspaceDTO()`.
* **Import**
    * Loads a workspace from JSON.
    * Invokes `WorkspaceImporter` and `workspace.fromWorkspaceDTO()`.

### 28.1 Button Behavior

* Each button uses icon + label in Ribbon.
* Icons are loaded via `ImageLoader` using `ComponentRegistry` metadata.
* Buttons handle `onAction` events for tool execution.

## 29. Workspace Integration

Ribbon buttons do not directly create components in Workspace.
They provide a drag-and-drop interface:

* User drags a button → Workspace receives `DragEvent`.
* Workspace instantiates the component and snaps it to the grid.
* Logic connections and UI rendering are handled automatically.

Tools directly affect Workspace content:

* **Reset** → clears all components
* **Export** → serializes current components + connections
* **Import** → rebuilds workspace from JSON

## 30. Summary of Component + Ribbon Flow

* Ribbon initialized → groups created (Inputs, Outputs, Gates, Tools).
* `ComponentConfig` provides lists of components by category.
* `Ribbon.addItem()` creates buttons with:
    * Tooltip
    * Icon (via `ComponentRegistry`)
    * Drag-and-drop event handlers
* User drags component button to workspace → Workspace instantiates component dynamically and snaps it to grid.
* Tools buttons execute workspace management operations directly (Reset, Export, Import).