package com.example.javafx_btl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class FXMLFindingCompetitor extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("FXML_GamePlay_trainMode.fxml"));

        // Create the progress indicator
        ProgressIndicator progressIndicator = new ProgressIndicator();

        // Get the anchor pane defined in the FXML file
        AnchorPane anchorPane = (AnchorPane) root;

        // Add the progress indicator to the anchor pane
        anchorPane.getChildren().add(progressIndicator);

        // Create the scene
        Scene scene = new Scene(root, 400, 300);

        stage.setTitle("Tìm đối");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}