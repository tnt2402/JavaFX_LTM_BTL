package com.example.javafx_btl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class FXMLFailed {
    @FXML
    private Button backButton;
    private Button retryButton;
    @FXML
    private ImageView gifImageView;
    @FXML
    private Label titleLabel;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCongratulationGif(String gifPath) {
        Image gifImage = new Image(gifPath);
        gifImageView.setImage(gifImage);
    }

    @FXML
    private void backToHome() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        // do what you have to do
        stage.close();
        try {
            GamePlayController tmp = new GamePlayController();
            Stage stg = tmp.getStage();
            stg.setResizable(false);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("FXML_MainForm.fxml"));
            stg.setTitle("Home");
            stg.setMinWidth(1100);
            stg.setMinHeight(600);

            stg.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void replay() {}

    @FXML
    private void closePopup() {
        stage.close();
    }
}
