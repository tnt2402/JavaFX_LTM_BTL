package com.example.javafx_btl;

import animatefx.animation.FadeIn;
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

    @FXML
    private TextArea questionField;
    @FXML
    private Label ans_a, ans_b, ans_c, ans_d;

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
                tmp.true_ans = jsonObject.get("dap_an").getAsString();;
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
            for (questionAnswerData i:ListQnA) {
                questionField.setText(i.question);
                ans_a.setText(i.ans_a);
                ans_b.setText(i.ans_b);
                ans_c.setText(i.ans_c);
                ans_d.setText(i.ans_d);
            }
        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GetQuestionFromServer();
        Play();
    }

}
