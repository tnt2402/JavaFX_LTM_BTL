module com.example.javafx_btl {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.javafx_btl to javafx.fxml;
    exports com.example.javafx_btl;
}