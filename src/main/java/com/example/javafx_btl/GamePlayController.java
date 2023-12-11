package com.example.javafx_btl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.stage.Stage;

public class GamePlayController implements Initializable {

    private playData currentPlay;

    @FXML
    private TextArea questionField_2;

    @FXML
    private Button ans_a, ans_b, ans_c, ans_d;

    private ArrayList<questionAnswerData> ListQnA = new ArrayList<questionAnswerData>();


    private void GetQuestionFromServer() {
        ServerConnection serverConnection = new ServerConnection("localhost", 8080);

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

    private void Play() {
        if (ListQnA != null) {
            questionAnswerData i = ListQnA.get(currentPlay.currentQuestionNumber - 1);
            questionField_2.setStyle("-fx-background-color: transparent;");

            questionField_2.setText(i.question);
            ans_a.setText(i.ans_a);
            ans_b.setText(i.ans_b);
            ans_c.setText(i.ans_c);
            ans_d.setText(i.ans_d);

        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GetQuestionFromServer();
        currentPlay = new playData();
        currentPlay.currentQuestionNumber = 1;
        Play();
    }

    @FXML
    private void handleMoneyRankList() {
        Button currentMoney = null;
        Scene currentScene = ans_a.getScene();
        currentMoney = (Button) currentScene.lookup(String.format("#money_%d", currentPlay.currentQuestionNumber));
        if (currentPlay.currentQuestionNumber % 5 == 0) {
            currentMoney.setStyle("-fx-background-color: yellow;-fx-text-fill: black;");
        } else {
            currentMoney.setStyle("-fx-background-color: lightblue;");
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
            handleMoneyRankList();
            currentPlay.currentQuestionNumber += 1;
            Play();
        } else {
            congratulations();
            return;
        }
    }

    private void congratulations() {
        System.out.println("Congratulation!!! You're billionaire");
        //
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXML_Congratulation.fxml"));

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Billionaireeeeeeee");
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
}
