package com.example.javafx_btl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import com.example.javafx_btl.FXMLLogin;
import static java.lang.Thread.sleep;

public class GamePlayController implements Initializable {
    private ServerConnection serverConnection;

    public Stage stg;

    @FXML
    private Label userField;
    // timer clock
    @FXML
    private Label timerLabel;

    @FXML
    private Button startButton;

    private List<Integer> secondsList;
    private int currentSeconds;
    private Timeline timeline;
    private Boolean testMode = false;

    // Play

    private playData currentPlay;

    @FXML
    private TextArea questionField_2;

    @FXML
    private Button ans_a, ans_b, ans_c, ans_d;

    private ArrayList<questionAnswerData> ListQnA = new ArrayList<questionAnswerData>();

    public GamePlayController() {
    }



    private void GetQuestionFromServer() {
        System.out.println("admin-toor_2");
        serverConnection.loginUser("admin", "toor_2");
        List<Object> jsonObjects = serverConnection.getData();

        while (jsonObjects == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Cannot retrieve data from the server!");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                jsonObjects = serverConnection.getData();
            } else if (option.get().equals(ButtonType.CANCEL)) {
                break;
            }
        }

//        System.out.println(JsonObj);
        if (jsonObjects != null) {
            JsonParser jsonParser = new JsonParser();

            Gson gson = new Gson();
            for (Object obj : jsonObjects) {
                JsonObject jsonObject = jsonParser.parse(gson.toJson(obj)).getAsJsonObject();

                questionAnswerData tmp = new questionAnswerData();
                tmp.id = jsonObject.get("id").getAsString();
                tmp.question = jsonObject.get("noi_dung").getAsString();
                tmp.ans_a = jsonObject.get("phuong_an_a").getAsString();
                tmp.ans_b = jsonObject.get("phuong_an_b").getAsString();
                tmp.ans_c = jsonObject.get("phuong_an_c").getAsString();
                tmp.ans_d = jsonObject.get("phuong_an_d").getAsString();
                tmp.true_ans = jsonObject.get("dap_an").getAsString();
                ListQnA.add(tmp);
            }
        } else {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("FXML_MainForm.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setTitle("Ai là triệu phú - version 0.1");

                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clockSetup() {
        if (!testMode) {
            secondsList = new ArrayList<>();
            for (int i=0; i < 5; i++) secondsList.add(15);
            for (int i=0; i < 5; i++) secondsList.add(30);
            for (int i=0; i < 5; i++) secondsList.add(45);
            // Add more seconds to the list if needed

//            currentSeconds = 0;
            timerLabel.setText("00:00");
        }
    }

    int[] seconds = {};
    int[] minutes = {};
    int[] remainingSeconds = {};
    @FXML
    private void startCountdown() {
        if (timeline != null) {
            timeline.stop();
        }
        currentSeconds = currentPlay.currentQuestionNumber;
        if (currentSeconds < secondsList.size()) {
            seconds = new int[]{secondsList.get(currentSeconds)};
            int saved_seconds = seconds[0];
            minutes = new int[]{seconds[0] / 60};
            remainingSeconds = new int[]{seconds[0] % 60};
            timerLabel.setText(String.format("%02d:%02d", minutes[0], remainingSeconds[0]));

            timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                seconds[0]--;
                if (seconds[0] >= 0) {
                    minutes[0] = seconds[0] / 60;
                    remainingSeconds[0] = seconds[0] % 60;
                    timerLabel.setText(String.format("%02d:%02d", minutes[0], remainingSeconds[0]));
                } else {
                    timesup(seconds[0]);
                }
            }));

            timeline.setCycleCount(saved_seconds + 1);
            timeline.play();

        }
    }

    private void timesup(int second) {
        currentPlay.end = new Date();
        failed();
        return;
    }


    private void Play() {
        // Set all button to lightblue
            ans_a.setStyle("-fx-background-color: transparent;");
            ans_b.setStyle("-fx-background-color: transparent;");
            ans_c.setStyle("-fx-background-color: transparent;");
            ans_d.setStyle("-fx-background-color: transparent;");
            if (ListQnA != null) {
                questionAnswerData i = ListQnA.get(currentPlay.currentQuestionNumber - 1);
                questionField_2.setText(i.question);

                ans_a.setDisable(false);
                ans_b.setDisable(false);
                ans_c.setDisable(false);
                ans_d.setDisable(false);

                ans_a.setText(i.ans_a);
                ans_b.setText(i.ans_b);
                ans_c.setText(i.ans_c);
                ans_d.setText(i.ans_d);

            }
            // Set timer
            startCountdown();

    }

    @FXML
    public void displayUsername() {

        String user = userData.username;
        userField.setText(user);

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serverConnection = FXMLLogin.conn;
        displayUsername();
        GetQuestionFromServer();
        currentPlay = new playData();
        currentPlay.currentQuestionNumber = 1;
        currentPlay.secondsUsage = 0;
        currentPlay.begin = new Date();
        currentPlay.listQuestions = new ArrayList<>();
        clockSetup();
        Play();
    }

    private void showAlert(String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.initOwner(ownerStage);
//        alert.setContentText(message);
//
//        // Creating a pause transition to automatically close the alert after 2 seconds
//        PauseTransition delay = new PauseTransition(Duration.seconds(2));
//        delay.setOnFinished(event -> alert.close());
//        delay.play();
//
//        alert.showAndWait();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> option = alert.showAndWait();
//        if (option.get().equals(ButtonType.OK)) {
//            return;
//        }
    }

    @FXML
    private void fiftyfifty_clicked() {
//        VBox root = new VBox();
//        root.setAlignment(Pos.CENTER);
//
//        Stage primaryStage = new Stage();
//
//        primaryStage.setTitle("JavaFX Alert Demo");
//        primaryStage.setScene(new Scene(root, 400, 300));
//        primaryStage.show();
        showAlert("Hệ thống đã bỏ 2 đáp án sai!");
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");

        String variable = ListQnA.get(currentPlay.currentQuestionNumber-1).true_ans;

        List<String> filteredList = new ArrayList<>();
        // Filter the list to exclude the variable
        for (String element : list) {
            if (!element.equals(variable)) {
                filteredList.add(element);
            }
        }

        if (filteredList.size() >= 2) {
            List<String> randomElements = new ArrayList<>();
            Random random = new Random();

            // Select two random elements
            while (randomElements.size() < 2) {
                int randomIndex = random.nextInt(filteredList.size());
                String randomElement = filteredList.get(randomIndex);

                if (!randomElements.contains(randomElement)) {
                    randomElements.add(randomElement);
                }
            }

            System.out.println(randomElements);

            Scene currentScene = ans_a.getScene();
            for (String i: randomElements) {
                System.out.println(currentPlay.currentQuestionNumber);
                Button iAns = (Button) currentScene.lookup(String.format("#ans_%s", i.toLowerCase()));
                iAns.setDisable(true);
            }


        }
    }

    @FXML
    private void audiencesSuggest() {
        // Create a CategoryAxis for the x-axis (answers) and a NumberAxis for the y-axis (percentage)
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        // Create a stacked bar chart with x-axis and y-axis
        StackedBarChart<String, Number> chart = new StackedBarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setAnimated(false);

        // Set the title for the chart
        chart.setTitle("Answer Percentage");

        // Create a series to hold the data
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Generate random percentages for answers a, b, c, d
        Random random = new Random();
        double trueAnswerPercentage = random.nextInt(61) + 40; // Generate a random percentage between 40 and 100
        double remainingPercentage = 100 - trueAnswerPercentage;

        List<Double> listRandomPerc = new ArrayList<>();
        while (true) {
            double randomPercentage1 = random.nextInt((int) remainingPercentage + 1);
            double randomPercentage2 = random.nextInt((int) (remainingPercentage - randomPercentage1) + 1);
            double randomPercentage3 = remainingPercentage - randomPercentage1 - randomPercentage2;
            if (trueAnswerPercentage > Math.max(randomPercentage1, Math.max(randomPercentage2, randomPercentage3))) {
                listRandomPerc.add(randomPercentage1);
                listRandomPerc.add(randomPercentage2);
                listRandomPerc.add(randomPercentage3);
                break;
            }
        }


        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        String variable = ListQnA.get(currentPlay.currentQuestionNumber-1).true_ans;
        List<Double> filteredList = new ArrayList<>();
        for (Integer i=0; i < list.size(); i++) {
            if (!list.get(i).equals(variable)) {
                filteredList.add(listRandomPerc.getLast());
                listRandomPerc.removeLast();
            } else {
                filteredList.add(trueAnswerPercentage);
            }
        }

        // Add data to the series
//        series.getData().add(new XYChart.Data<>("A", trueAnswerPercentage));
//        series.getData().add(new XYChart.Data<>("B", randomPercentage1));
//        series.getData().add(new XYChart.Data<>("C", randomPercentage2));
//        series.getData().add(new XYChart.Data<>("D", randomPercentage3));
        System.out.println(list);
        System.out.println(filteredList);
        for (Integer i=0; i < list.size(); i++) {
            series.getData().add(new XYChart.Data<>(list.get(i), filteredList.get(i)));
        }

        // Add the series to the chart
        ObservableList<XYChart.Series<String, Number>> chartData = FXCollections.observableArrayList();
        chartData.add(series);
        chart.setData(chartData);

        // Create a stack pane to hold the chart
        StackPane root = new StackPane();
        root.getChildren().add(chart);

        // Create a scene with the root node
        Scene scene = new Scene(root, 400, 300);
        Stage primaryStage = new Stage();

        // Set the stage title and scene, then show the stage
        primaryStage.setTitle("Percentage Column Chart");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    synchronized private void handleMoneyRankList() {
        Button currentMoney = null, currentQuestions = null;
        Scene currentScene = ans_a.getScene();
        System.out.println(currentPlay.currentQuestionNumber);
        currentMoney = (Button) currentScene.lookup(String.format("#money_%d", currentPlay.currentQuestionNumber-1));
        currentQuestions = (Button) currentScene.lookup(String.format("#question_%d", currentPlay.currentQuestionNumber-1));

        if (currentPlay.currentQuestionNumber % 5 == 1 && currentPlay.currentQuestionNumber > 1) {
            currentMoney.setStyle("-fx-background-color: yellow;-fx-text-fill: black;");
            currentQuestions.setStyle("-fx-background-color: yellow;-fx-text-fill: black;");

        } else {
            currentMoney.setStyle("-fx-background-color: lightblue;");
            currentQuestions.setStyle("-fx-background-color: lightblue;");

        }
    }

    void checkAnswer(String tmp_answer) {
        if (tmp_answer.equals(ListQnA.get(currentPlay.currentQuestionNumber - 1).true_ans)) {
            System.out.printf("Question #%d done!\n", currentPlay.currentQuestionNumber);
            if (currentPlay.currentQuestionNumber == 15) {
                handleMoneyRankList();
                congratulations();
                return;
            }
            timeline.stop();
            System.out.printf("Question #%d - Time usage: %d seconds\n", currentPlay.currentQuestionNumber, seconds[0]);
            currentPlay.secondsUsage += seconds[0];
            currentPlay.currentQuestionNumber += 1;
            showEffect(true, tmp_answer);
            // logging
            currentPlay.listQuestions.add(ListQnA.get(currentPlay.currentQuestionNumber - 2).id);

        } else {
            showEffect(false, tmp_answer);
            // logging the user play data to user play history
            serverConnection.updatePlayHistory(currentPlay);
        }
    }
    synchronized void showEffect(Boolean tmp, String tmp_answer) {
        if (tmp) {
            handleMoneyRankList();
            handleAnswerEffect(tmp_answer, ListQnA.get(currentPlay.currentQuestionNumber - 1).true_ans, true);
        } else {
            handleAnswerEffect(tmp_answer, ListQnA.get(currentPlay.currentQuestionNumber - 1).true_ans, false);
        }
    }

    synchronized private void handleAnswerEffect(String ans, String true_ans, Boolean res) {
        Button currentAns = null, trueAns = null;
        Scene currentScene = ans_a.getScene();
        currentAns = (Button) currentScene.lookup(String.format("#ans_%s", ans.toLowerCase()));
        trueAns = (Button) currentScene.lookup(String.format("#ans_%s", true_ans.toLowerCase()));

        if (res) {
            Button finalCurrentAns3 = currentAns;
            Button finalCurrentAns4 = currentAns;
            Timeline blinkTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.1), event -> {
                        Platform.runLater(() -> finalCurrentAns3.setStyle("-fx-background-color: yellow; -fx-text-fill: white;"));
                    }),
                    new KeyFrame(Duration.seconds(0.2), event -> {
                        Platform.runLater(() -> finalCurrentAns4.setStyle("-fx-background-color: red; -fx-text-fill: white;"));
                    })
            );
            blinkTimeline.setCycleCount(5); // Blink for 5 cycles
            Button finalCurrentAns5 = currentAns;
            blinkTimeline.setOnFinished(event -> {
                Platform.runLater(() -> finalCurrentAns5.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
                Play();
            });
            blinkTimeline.play();
        } else {
            Button finalTrueAns1 = trueAns;
            Button finalCurrentAns1 = currentAns;
            Button finalTrueAns2 = trueAns;
            Button finalCurrentAns2 = currentAns;
            Timeline blinkTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.1), event -> {
                        Platform.runLater(() -> {
                            finalTrueAns1.setStyle("-fx-background-color: lightblue;");
                            finalCurrentAns1.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                        });
                    }),
                    new KeyFrame(Duration.seconds(0.2), event -> {
                        Platform.runLater(() -> {
                            finalTrueAns2.setStyle("-fx-background-color: white;"); // Set back to default color
                            finalCurrentAns2.setStyle("-fx-background-color: yellow; -fx-text-fill: white;");
                        });
                    })
            );
            blinkTimeline.setCycleCount(5); // Blink for 5 cycles
            Button finalTrueAns = trueAns;
            Button finalCurrentAns = currentAns;
            blinkTimeline.setOnFinished(event -> {
                Platform.runLater(() -> {
                    finalTrueAns.setStyle("-fx-background-color: transparent;"); // Set back to default color
                    finalCurrentAns.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
                });
                failed();
            });
            blinkTimeline.play();
        }

    }

    private void failed() {
        System.out.println("Well! You failed");
        //
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML_Failed.fxml"));
            Parent root = loader.load();

            FXMLFailed controller = loader.getController();
            Stage stage = new Stage();

            controller.setStage(stage);

            Scene scene = new Scene(root);
            stage.setTitle("Faillllllllll");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void congratulations() {
        System.out.println("Congratulation!!! You're billionaire");
        //
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML_Congratulation.fxml"));
            Parent root = loader.load();

            FXMLCongratulationController controller = loader.getController();
            Stage stage = new Stage();

            controller.setStage(stage);
//            controller.setCongratulationGif("./congrats.gif"); // Replace with the actual path

            Scene scene = new Scene(root);
            stage.setTitle("Congratulations! \uD83C\uDF89 Billionaireeeeeeee");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ans_a_clicked(ActionEvent actionEvent) {
        checkAnswer("A");
    }

    public void ans_b_clicked(ActionEvent actionEvent) {
        checkAnswer("B");
    }

    public void ans_c_clicked(ActionEvent actionEvent) {
        checkAnswer("C");
    }

    public void ans_d_clicked(ActionEvent actionEvent) {
        checkAnswer("D");
    }
    //
    public Stage getStage() {
        Stage stage = (Stage) ans_a.getScene().getWindow();
        return stage;
    }
}


