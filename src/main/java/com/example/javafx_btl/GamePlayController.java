package com.example.javafx_btl;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.json.JSONObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Iterator;
public class GamePlayController implements Initializable {

    @FXML
    private TextArea questionField;
    @FXML
    private Label ans_a, ans_b, ans_c, ans_d;

    private ArrayList<questionAnswerData> ListQnA = new ArrayList<questionAnswerData>();
    ."";

    private void GetQuestionFromServer() {
        // testing purpose
//        questionAnswerData test = new questionAnswerData();
//        test.question = "Giải Grand Slam đầu tiên trong năm là giải nào?";
//        test.ans_a = "Austrlia mở rộng";
//        test.ans_b = "Wimbledon";
//        test.ans_c = "Roland Garos";
//        test.ans_d = "Mỹ mở rộng";
//        test.true_ans = "A";
        ServerConnection serverConnection = new ServerConnection("localhost", 8080);

        List<Object> JsonObj = serverConnection.getData();
//        System.out.println(JsonObj);

        for (Object i: JsonObj) {
            questionAnswerData test = new questionAnswerData();
            test.question = i.noi_dung;
            test.ans_a = i.
            test.ans_b =
            test.ans_c =
            test.ans_d =
            test.true_ans =
        }
//        ListQnA.add(test);
        //
        serverConnection.closeConnection();
    }

    private void Play() {
        for (questionAnswerData i:ListQnA) {
            questionField.setText(i.question);
            ans_a.setText(i.ans_a);
            ans_b.setText(i.ans_b);
            ans_c.setText(i.ans_c);
            ans_d.setText(i.ans_d);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GetQuestionFromServer();
        Play();
    }

}
