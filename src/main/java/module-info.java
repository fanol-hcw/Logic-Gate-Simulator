module org.example.logicgatesimulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;


    opens org.example.logicgatesimulator to javafx.fxml;
    exports org.example.logicgatesimulator;
}