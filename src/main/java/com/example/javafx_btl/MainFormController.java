/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.javafx_btl;

import animatefx.animation.FadeIn;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.javafx_btl.ServerConnection.conn;

public class MainFormController implements Initializable {
    
    @FXML
    private AnchorPane main_form;
    
    @FXML
    private Label username;
    
    @FXML
    private Button dashboard_btn;
    
    @FXML
    private Button inventory_btn;
    
    @FXML
    private Button menu_btn;
    
    @FXML
    private Button customers_btn;
    
    @FXML
    private Button logout_btn;
    
    @FXML
    private AnchorPane inventory_form;

    @FXML
    private ImageView inventory_imageView;
    
    @FXML
    private Button inventory_importBtn;
    
    @FXML
    private Button inventory_addBtn;
    
    @FXML
    private Button inventory_updateBtn;
    
    @FXML
    private Button inventory_clearBtn;
    
    @FXML
    private Button inventory_deleteBtn;
    
    @FXML
    private TextField inventory_productID;
    
    @FXML
    private TextField inventory_productName;
    
    @FXML
    private TextField inventory_stock;
    
    @FXML
    private TextField inventory_price;
    
    @FXML
    private ComboBox<?> inventory_status;
    
    @FXML
    private ComboBox<?> inventory_type;
    
    @FXML
    private AnchorPane menu_form;
    
    @FXML
    private ScrollPane menu_scrollPane;
    
    @FXML
    private GridPane menu_gridPane;

    
    @FXML
    private Label menu_total;
    
    @FXML
    private TextField menu_amount;
    
    @FXML
    private Label menu_change;
    
    @FXML
    private Button menu_payBtn;
    
    @FXML
    private Button menu_removeBtn;
    
    @FXML
    private Button menu_receiptBtn;
    
    @FXML
    private AnchorPane dashboard_form;
    
    @FXML
    private AnchorPane customers_form;

    
    @FXML
    private Label dashboard_NC;
    
    @FXML
    private Label dashboard_TI;
    
    @FXML
    private Label dashboard_TotalI;
    
    @FXML
    private Label dashboard_NSP;
    
    @FXML
    private AreaChart<?, ?> dashboard_incomeChart;
    
    @FXML
    private BarChart<?, ?> dashboard_CustomerChart;
    
    private Alert alert;
    
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    
    private Image image;

    public Stage stg;

    public void logout() {
        
        try {
            
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();
            
            if (option.get().equals(ButtonType.OK)) {

                // TO HIDE MAIN FORM 
                logout_btn.getScene().getWindow().hide();

                // LINK YOUR LOGIN FORM AND SHOW IT 
                Parent root = FXMLLoader.load(getClass().getResource("FXML_Login.fxml"));
                
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                
                stage.setTitle("Ai là triệu phú - version 0.1");
                
                stage.setScene(scene);
                stage.show();
                new FadeIn(root).play();
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void displayUsername() {
        
        String user = userData.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        
        username.setText(user);
        
    }


    public void contestMode(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML_GamePlay.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(scene);
            currentStage.setTitle("Ai là triệu phú - version 0.1");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void trainMode() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        Text text = new Text("Đang tìm đối thủ");

        FlowPane root = new FlowPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.getChildren().addAll(progressIndicator, text);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Tìm đối");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        Thread findCompetitorThread = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            long timeoutMillis = 30000; // 30 seconds

            while (!Thread.currentThread().isInterrupted()) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long remainingTime = timeoutMillis - TimeUnit.MILLISECONDS.toSeconds(elapsedTime) * 1000;
                if (remainingTime <= 0) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Tìm đối thủ");
                        alert.setHeaderText(null);
                        alert.setContentText("Không tìm thấy đối thủ trong thời gian quy định!");
                        alert.showAndWait();
                        stage.close(); // Close the stage
                    });
                    break;
                }

                long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingTime);
                Platform.runLater(() -> {
                    text.setText("Đang tìm đối thủ (" + seconds + " giây)");
                });
                String response;
                try {
                    conn.write("GET /findingCompetitor");
                    response = conn.read();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (response.equals("Not found")) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Tìm đối thủ");
                        alert.setHeaderText(null);
                        alert.setContentText("Không tìm thấy đối thủ!");
                        alert.showAndWait();
                        stage.close(); // Close the stage
                    });
                    break;
                } else {
                    Platform.runLater(() -> {
                        try {
                            conn.write(String.valueOf(userData.id));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Tìm đối thủ");
                        alert.setHeaderText(null);
                        alert.setContentText("Đã tìm thấy đối thủ!");
                        alert.showAndWait();

                        // Continue with competing logic using the response string
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML_GamePlay.fxml"));
                        try {
//                            Parent root = null;

                            Parent root_ = loader.load();
                            Scene scene_ = new Scene(root_);
                            Stage currentStage = stage;

                            currentStage.setScene(scene_);
                            currentStage.setTitle("Ai là triệu phú - version 0.1");
                            currentStage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    });
                    break;
                }
            }
        });

        findCompetitorThread.start();

        stage.setOnCloseRequest(event -> {
            findCompetitorThread.interrupt(); // Stop the thread if the stage is closed
        });
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        displayUsername();

    }
    
}

