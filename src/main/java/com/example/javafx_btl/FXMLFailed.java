package com.example.javafx_btl;

import javafx.fxml.FXML;
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
    private void closePopup() {
        stage.close();
    }
}
