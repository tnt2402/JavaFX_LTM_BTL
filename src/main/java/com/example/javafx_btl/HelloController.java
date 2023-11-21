package com.example.javafx_btl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class HelloController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageField;

    private ServerConnection serverConnection;

    public HelloController() {
        serverConnection = new ServerConnection();
    }

    @FXML
    protected void loginButtonClick() throws IOException {
        HelloApplication m = new HelloApplication();
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (usernameField.getText().isBlank() || passwordField.getText().isBlank()) {
            messageField.setText("Username/password cannot be empty!");
        }
        else {
            messageField.setText("You try to login!");
            boolean loginSuccess = serverConnection.loginUser(username, password);
            loginSuccess = true;
            if (loginSuccess) {
                messageField.setText("Login succesfully!");
                m.changeScene("home.fxml", 900, 520);
            }
        }
    }
}