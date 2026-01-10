module org.example.logicgatesimulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.logicgatesimulator to javafx.fxml;
    exports org.example.logicgatesimulator;
}