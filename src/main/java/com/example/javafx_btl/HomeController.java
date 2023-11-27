package com.example.javafx_btl;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.IOException;

public class HomeController {

    @FXML
    private Label username;

    @FXML
    protected void playModeStart() throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("playMode.fxml", 900, 520);
    }
}
