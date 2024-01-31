/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.javafx_btl;

import animatefx.animation.FadeIn;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.stage.Window;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private AnchorPane history;

    @FXML
    private Label dashboard_NC;
    
    @FXML
    private Label dashboard_TI;
    
    @FXML
    private Label dashboard_TotalI;
    
    @FXML
    private Label dashboard_NSP;
    
    @FXML
    private AreaChart<String, Number> dashboard_incomeChart;
    
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
        Stage stage = new Stage();
        stage.setTitle("Tìm đối");
        stage.setResizable(false);
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);
        ProgressIndicator progressIndicator = new ProgressIndicator();
        Text text = new Text("Đang tìm đối thủ");
        root.getChildren().addAll(progressIndicator, text);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();



        Thread findCompetitorThread = new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis();
                long timeoutMillis = 30000; // 30 seconds

                while (true) {
                    // Simulate server response for demonstration purpose
                    Thread.sleep(2000);
//                    String response = "Not found"; // Replace this with actual server response
                    String response = conn.read();
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    long remainingTime = timeoutMillis - elapsedTime;
                    if (remainingTime <= 0) {
                        Platform.runLater(() -> {
                            text.setText("Không tìm thấy đối thủ trong thời gian quy định!");

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Tìm đối thủ");
                            alert.setHeaderText(null);
                            alert.setContentText("Không tìm thấy đối thủ trong thời gian quy định!");
                            alert.showAndWait();
                            stage.close(); // Close the stage
                        });
                        break;
                    } else {
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingTime);
                        Platform.runLater(() -> {
                            text.setText("Đang tìm đối thủ (" + seconds + " giây)");
                        });
                    }

                    if (response.equals("Not found")) {
                        Platform.runLater(() -> {
                            text.setText("Đang tìm đối thủ");
                        });
                    } else {
                        Platform.runLater(() -> {
                            text.setText("Đã tìm thấy đối thủ!");

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Tìm đối thủ");
                            alert.setHeaderText(null);
                            alert.setContentText("Đã tìm thấy đối thủ!");
                            alert.showAndWait();

                            // Continue with competing logic using the response string
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML_GamePlay_competitiveMode.fxml"));
                            try {
                                Parent root_ = loader.load();
                                Scene scene_ = new Scene(root_);
                                Stage currentStage = (Stage) root.getScene().getWindow();

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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        findCompetitorThread.start();

        try {
            conn.write("GET /findingCompetitor");
            conn.write(String.valueOf(userData.id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.setOnCloseRequest(event -> {
            findCompetitorThread.interrupt(); // Stop the thread if the stage is closed
            try {
                conn.write("CLOSE /findingCompetitor");
                conn.write(String.valueOf(userData.id));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    private void showHistory() {
//        dashboard_incomeChart.getXAxis().setTickLabelRotation(90);
        List<JSONObject> userPlayHistory = conn.getUserPlayHistory();
        System.out.println(userPlayHistory);
        // Sample data
//        List<String> score = List.of("100", "80", "120", "150", "90"); // Replace with your actual list of scores
//        List<String> time = List.of("10:00", "11:00", "12:00", "13:00", "14:00"); // Replace with your actual list of times

        List<String> score = new ArrayList<>(), time = new ArrayList<>();
        for (JSONObject i: userPlayHistory) {
            score.add((i.get("Score")).toString());

            // parse time
            String dateString = (String) i.get("Begin");
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date date = null;
            try {
                date = inputFormat.parse(dateString);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            // Create a SimpleDateFormat object with the desired output pattern
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String formattedDate = outputFormat.format(date);
            time.add(formattedDate);
        }
//        System.out.println(score);
//        System.out.println(time);


        // Create the chart
//        dashboard_incomeChart.setTitle("Score History");

        // Prepare the data series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();

        // Populate the data series with score history
        for (int i = 0; i < time.size(); i++) {
            String timePoint = time.get(i);
            String scoreValue = score.get(i);
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(timePoint, Integer.parseInt(scoreValue));
            data.add(dataPoint);
        }

        // Set the data series to the chart
        series.setData(data);
        dashboard_incomeChart.getData().add(series);

        // Set the x-axis to the chart
        dashboard_incomeChart.getXAxis().setTickLabelRotation(90);
        dashboard_incomeChart.getXAxis().setTickLabelsVisible(false);
        dashboard_incomeChart.setHorizontalGridLinesVisible(false);
        dashboard_incomeChart.setVerticalGridLinesVisible(false);
        dashboard_incomeChart.getXAxis().setOpacity(0);
    }

    @FXML
    private void showAllHistory() {
        // Create a new Stage for the play history screen
        Stage historyStage = new Stage();
        historyStage.setTitle("Play History");

        // Create the root layout for the play history screen
        BorderPane root = new BorderPane();

        // Create a TableView to hold the play history data
        TableView<PlayHistoryEntry> historyTable = new TableView<>();
        historyTable.setPrefHeight(500);
        historyTable.setPrefWidth(600);

        // Define the columns for the table
        TableColumn<PlayHistoryEntry, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("formattedTime"));

        TableColumn<PlayHistoryEntry, String> timeUsageColumn = new TableColumn<>("Time Usage");
        timeUsageColumn.setCellValueFactory(new PropertyValueFactory<>("timeUsage"));

        TableColumn<PlayHistoryEntry, String> questionsColumn = new TableColumn<>("Questions");
        questionsColumn.setCellValueFactory(new PropertyValueFactory<>("lastQuestion"));

        TableColumn<PlayHistoryEntry, String> md5SumColumn = new TableColumn<>("MD5 Sum");
        md5SumColumn.setCellValueFactory(new PropertyValueFactory<>("competitiveMode"));

        // Add the columns to the table
        historyTable.getColumns().addAll(timeColumn, timeUsageColumn, questionsColumn, md5SumColumn);

        List<JSONObject> userPlayHistory = conn.getUserPlayHistory();

        // Iterate through the playHistory list and add each playData object to the table
        for (JSONObject i : userPlayHistory) {
            // parse time
            String dateString = (String) i.get("Begin");
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date date = null;
            try {
                date = inputFormat.parse(dateString);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            // Create a SimpleDateFormat object with the desired output pattern
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String formattedDate = outputFormat.format(date);

            String timeUsage = i.get("Time_Usage").toString();
            String lastQuestion = i.get("Last_Question").toString();
            String competitiveMode = i.get("competitiveMode").toString();

            PlayHistoryEntry entry = new PlayHistoryEntry(formattedDate, timeUsage, lastQuestion, competitiveMode);
            historyTable.getItems().add(entry);
        }

        // Set the TableView as the center content of the root layout
        root.setCenter(historyTable);

        // Create a Scene with the root layout
        Scene scene = new Scene(root, 600, 500);

        // Set the scene and show the history stage
        historyStage.setScene(scene);
        historyStage.show();
    }

    private StackPane dataPointPopup;
    private Label dataPointLabel;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();
        // Set the data point symbol to be a label
        dashboard_incomeChart.setCreateSymbols(true);
        dashboard_incomeChart.setLegendVisible(false);


        // Add mouse hover event listener for each data point
        for (XYChart.Series<String, Number> series : dashboard_incomeChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node node = data.getNode();

                node.setOnMouseEntered(event -> {
                    // Show the value of the data point as a label
                    double x = node.getBoundsInParent().getMinX() + node.getBoundsInParent().getWidth() / 2;
                    double y = node.getBoundsInParent().getMinY() - 10;

                    Text dataPointLabel = new Text(data.getYValue().toString());
                    dataPointLabel.getStyleClass().add("chart-area-symbol");
                    dataPointLabel.relocate(x, y);
                    ((Pane) node.getParent()).getChildren().add(dataPointLabel);
                });

                node.setOnMouseExited(event -> {
                    // Hide the data point label when the mouse leaves
                    ((Pane) node.getParent()).getChildren().removeIf(n -> n.getStyleClass().contains("chart-area-symbol"));
                });
            }
        }
        showHistory();
    }



}

