package com.example.javafx_btl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class FXMLFailed {
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
        try {
            HelloApplication main = new HelloApplication();
            main.changeScene("FXML_MainForm.fxml", 629, 395);
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
