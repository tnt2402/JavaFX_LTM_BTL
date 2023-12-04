module com.example.javafx_btl {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires org.json;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;
    requires AnimateFX;

    opens com.example.javafx_btl to javafx.fxml;
    exports com.example.javafx_btl;
}