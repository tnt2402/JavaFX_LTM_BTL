package com.example.javafx_btl;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
    protected void loginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (usernameField.getText().isBlank() || passwordField.getText().isBlank()) {
            messageField.setText("Username/password cannot be empty!");
        }
        else {
            messageField.setText("You try to login!");
            boolean loginSuccess = serverConnection.loginUser(username, password);
            if (loginSuccess) {
                messageField.setText("Login succesfully!");
            }
        }
    }
}