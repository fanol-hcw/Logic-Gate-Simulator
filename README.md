# Logic Gate Simulator
The application lets you place logic gates on a workspace, connect them with wires, and see how signals move through the circuit.

![workspace.png](docs/workspace.png)

## What the Program Does

You can add different logic gates like AND, OR, and NOT to the workspace. Inputs such as buttons or switches can be used to control the circuit, and outputs like LEDs show the result. When you change an input, the simulator updates the circuit so you can immediately see what happens.

The interface is built with JavaFX and uses simple visuals for gates and wires. Components can be dragged around and connected to each other to form a working circuit.

## Project Structure

The project is split into two main parts: the user interface and the simulation logic.

The `example.logicgatesimulator` package contains the JavaFX application, the main window, the workspace, and the visual versions of the gates and wires. It also includes the component registry that keeps track of available logic elements.

The `simulation` package contains the actual logic behind the simulator. This is where the gates, inputs, outputs, and signal evaluation are implemented.

## Notes

This project was created as part of a school assignment and focuses on learning Java, JavaFX, and basic digital logic concepts and is intended for educational use only. The code can be extended with more gates or features if needed.
## Documentation
You can find detailed technical documentation regarding the project, architecture, and components here:
[📄 Open Project Documentation](docs/Documentation.md)