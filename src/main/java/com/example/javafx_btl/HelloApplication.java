package com.example.javafx_btl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage stg;
    @Override
    public void start(Stage stage) throws IOException {
        // setup server - client connection
        stg = stage;
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 629, 395);
        stage.setTitle("Chương trình Ai là triệu phú - version 0.1");
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(String fxml, int height, int width) throws IOException {
        FXMLLoader pane = new FXMLLoader(HelloApplication.class.getResource("home.fxml"));
        Scene scene = new Scene(pane.load(), height, width);
        stg.setScene(scene);
//        stg.getScene().setRoot(pane);
    }

    public static void main(String[] args) {
        launch();
    }
}