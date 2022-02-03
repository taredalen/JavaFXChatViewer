module main {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires json.simple;
    requires com.google.gson;
    requires junit;

    opens main to javafx.fxml;
    exports main;
    exports data;
    opens data to javafx.fxml;
}