package com.example.javafx_btl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public String username_login;
    private static Stage stg;

    public void setUsername_login(String tmp_username) {
        username_login = tmp_username;
        System.out.println(username_login);
    }

    public String getUsername_login() {
        return username_login;
    }
    @Override
    public void start(Stage stage) throws IOException {
        // setup server - client connection
        stg = stage;
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("FXML_Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 629, 395);
        stage.setTitle("Chương trình Ai là triệu phú - version 0.1");
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(String fxml, int height, int width) throws IOException {
        FXMLLoader pane = new FXMLLoader(HelloApplication.class.getResource("FXML_Home.fxml"));
        Scene scene = new Scene(pane.load(), height, width);
        stg.setScene(scene);
//        stg.getScene().setRoot(pane);
    }

    public static void main(String[] args) {
        launch();
    }
}