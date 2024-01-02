package com.example.javafx_btl;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLLogin implements Initializable {
    
    @FXML
    private AnchorPane si_loginForm;
    
    @FXML
    private TextField si_username;
    
    @FXML
    private PasswordField si_password;
    
    @FXML
    private Button si_loginBtn;
    
    @FXML
    private Hyperlink si_forgotPass;
    
    @FXML
    private AnchorPane su_signupForm;
    
    @FXML
    private TextField su_username;
    
    @FXML
    private PasswordField su_password;
    
    @FXML
    private ComboBox<?> su_question;
    
    @FXML
    private TextField su_answer;
    
    @FXML
    private Button su_signupBtn;
    
    @FXML
    private TextField fp_username;
    
    @FXML
    private AnchorPane fp_questionForm;
    
    @FXML
    private Button fp_proceedBtn;
    
    @FXML
    private ComboBox<?> fp_question;
    
    @FXML
    private TextField fp_answer;
    
    @FXML
    private Button fp_back;
    
    @FXML
    private AnchorPane np_newPassForm;
    
    @FXML
    private PasswordField np_newPassword;
    
    @FXML
    private PasswordField np_confirmPassword;
    
    @FXML
    private Button np_changePassBtn;
    
    @FXML
    private Button np_back;
    
    @FXML
    private AnchorPane side_form;
    
    @FXML
    private Button side_CreateBtn;
    
    @FXML
    private Button side_alreadyHave;
    
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    private Alert alert;

    public static ServerConnection conn = null;
    
    public void loginBtn() {
        
        if (si_username.getText().isEmpty() || si_password.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Empty Username/Password");
            alert.showAndWait();
        } else {
            conn = new ServerConnection();
            conn.Connect("localhost", 2402);

                try {
                    String tmp_username = si_username.getText();
                    String tmp_password = si_password.getText();
                    // IF SUCCESSFULLY LOGIN, THEN PROCEED TO ANOTHER FORM WHICH IS OUR MAIN FORM
                    if (conn.loginUser(tmp_username, tmp_password)) {
                        // TO GET THE USERNAME THAT USER USED
                        userData.username = si_username.getText();

                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Login!");
                        alert.showAndWait();

                        // LINK YOUR MAIN FORM
//                    Parent root = FXMLLoader.load(getClass().getResource("FXML_Home.fxml"));
                        Parent root = FXMLLoader.load(HelloApplication.class.getResource("FXML_MainForm.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(root);

                        stage.setTitle("Home");
                        stage.setMinWidth(1100);
                        stage.setMinHeight(600);

                        stage.setScene(scene);
                        stage.show();

                        si_loginBtn.getScene().getWindow().hide();

                    } else { // IF NOT, THEN THE ERROR MESSAGE WILL APPEAR
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Incorrect Username/Password");
                        alert.showAndWait();
                        conn.closeConnection();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
        
    }
    
    public void regBtn() {
        
        if (su_username.getText().isEmpty() || su_password.getText().isEmpty()
                || su_question.getSelectionModel().getSelectedItem() == null
                || su_answer.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {

            

                if (1==1) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(su_username.getText() + " is already taken");
                    alert.showAndWait();
                } else if (su_password.getText().length() < 8) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Password, atleast 8 characters are needed");
                    alert.showAndWait();
                } else {

                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully registered Account!");
                    alert.showAndWait();
                    
                    su_username.setText("");
                    su_password.setText("");
                    su_question.getSelectionModel().clearSelection();
                    su_answer.setText("");
                    
                    TranslateTransition slider = new TranslateTransition();
                    
                    slider.setNode(side_form);
                    slider.setToX(0);
                    slider.setDuration(Duration.seconds(.5));
                    
                    slider.setOnFinished((ActionEvent e) -> {
                        side_alreadyHave.setVisible(false);
                        side_CreateBtn.setVisible(true);
                    });
                    
                    slider.play();
                }
                

        }
        
    }
    
    private String[] questionList = {"What is your favorite Color?", "What is your favorite food?", "what is your birth date?"};
    
    public void regLquestionList() {
        List<String> listQ = new ArrayList<>();
        
        for (String data : questionList) {
            listQ.add(data);
        }
        
        ObservableList listData = FXCollections.observableArrayList(listQ);
        su_question.setItems(listData);
    }
    
    public void switchForgotPass() {
        fp_questionForm.setVisible(true);
        si_loginForm.setVisible(false);
        
        forgotPassQuestionList();
    }
    
    public void proceedBtn() {
        
        if (fp_username.getText().isEmpty() || fp_question.getSelectionModel().getSelectedItem() == null
                || fp_answer.getText().isEmpty()) {
            
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
            
        } else {
                if (1==1) {
                    np_newPassForm.setVisible(true);
                    fp_questionForm.setVisible(false);
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Information");
                    alert.showAndWait();
                }

            
        }
        
    }
    
    public void changePassBtn() {
        try {
            if (np_newPassword.getText().isEmpty() || np_confirmPassword.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {

                if (np_newPassword.getText().equals(np_confirmPassword.getText())) {

                    String date = "";
                    if (1 == 1) {
                        date = result.getString("date");
                    }


                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully changed Password!");
                    alert.showAndWait();

                    si_loginForm.setVisible(true);
                    np_newPassForm.setVisible(false);

                    // TO CLEAR FIELDS
                    np_confirmPassword.setText("");
                    np_newPassword.setText("");
                    fp_question.getSelectionModel().clearSelection();
                    fp_answer.setText("");
                    fp_username.setText("");


                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Not match");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void forgotPassQuestionList() {
        
        List<String> listQ = new ArrayList<>();
        
        for (String data : questionList) {
            listQ.add(data);
        }
        
        ObservableList listData = FXCollections.observableArrayList(listQ);
        fp_question.setItems(listData);
        
    }
    
    public void backToLoginForm(){
        si_loginForm.setVisible(true);
        fp_questionForm.setVisible(false);
    }
    
    public void backToQuestionForm(){
        fp_questionForm.setVisible(true);
        np_newPassForm.setVisible(false);
    }



    public void switchForm(ActionEvent event) {

        TranslateTransition slider = new TranslateTransition();

        if (event.getSource() == side_CreateBtn) {
            slider.setNode(side_form);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(true);
                side_CreateBtn.setVisible(false);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);

                regLquestionList();
            });

            slider.play();
        } else if (event.getSource() == side_alreadyHave) {
            slider.setNode(side_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(false);
                side_CreateBtn.setVisible(true);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);
            });

            slider.play();
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO



    }
    
}
