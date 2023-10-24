module com.example.latticeline {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires javafx.web;
    requires okhttp3;
    requires okio;
    requires com.google.gson;
    requires java.sql;
    opens com.example.latticeline to javafx.fxml;
    exports com.example.latticeline;
}