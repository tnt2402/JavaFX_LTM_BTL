package com.example.javafx_btl;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.logging.Level;

import javafx.fxml.Initializable;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class ServerConnection implements Initializable {
    Logger logger = Logger.getLogger(ServerConnection.class.getName());
    private static String SERVER_ADDRESS;
    private static int SERVER_PORT;

    public Socket socket;

    public userData currentUser = new userData();

    public ServerConnection(String address, int port) {
        SERVER_ADDRESS = address;
        SERVER_PORT = port;
    }



    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setCurrentUser(Integer id, String username) {
        currentUser.id = id;
        currentUser.username = username;
    }

    public void updatePlayHistory(playData new_playdata) {
        currentUser.play_history.add(new_playdata);
//        System.out.println(currentUser.play_history);
        for (playData i: currentUser.play_history) {
            System.out.println(i.listQuestions);
        }
    }

    public boolean loginUser(String username, String password) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Send the username and password to the server
            out.println(username);
            out.println(password);

            // Wait for the server's response
            String response = in.readLine();

            // Check the server's response
            if (response != null && response.equals("LOGIN_SUCCESS")) {
                return true; // Login successful
            } else {
                return false; // Login failed
            }
        } catch (IOException e) {
            logger.log(Level.WARNING,"An error occurred during the connection", e);
            return false; // An error occurred during the connection
        }
    }

    public List<Object> getData() {
            currentUser.play_history = new ArrayList<>();

        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String requestData = "GET /data";
            writer.write(requestData);
            writer.newLine();
            writer.flush();

            StringBuilder jsonData = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }

            // Process the received JSON data
            String receivedData = jsonData.toString();
             System.out.println("Received JSON data:\n" + receivedData);

//            JSONObject json = new JSONObject(receivedData);
            ObjectMapper objectMapper = new ObjectMapper();
            List<Object> jsonObjects = objectMapper.readValue(receivedData, List.class);

            return jsonObjects;
        } catch (Exception e) {
            logger.log(Level.WARNING, "An error occurred during data retrieval");
            return null; // An error occurred during data retrieval
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
