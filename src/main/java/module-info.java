module com.example.javafx_btl {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires org.json;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;
    requires AnimateFX;
    requires java.desktop;
    requires jlayer;

    opens com.example.javafx_btl to javafx.fxml;
    exports com.example.javafx_btl;
}