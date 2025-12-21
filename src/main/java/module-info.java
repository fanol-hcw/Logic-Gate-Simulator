module org.example.logicgatesimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.logicgatesimulator to javafx.fxml;
    exports org.example.logicgatesimulator;
}