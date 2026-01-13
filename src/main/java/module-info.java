module org.example.logicgatesimulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;


    opens org.example.logicgatesimulator to javafx.fxml;
    opens org.example.logicgatesimulator.dto to com.fasterxml.jackson.databind;

    exports org.example.logicgatesimulator;
    exports org.example.logicgatesimulator.dto;
}