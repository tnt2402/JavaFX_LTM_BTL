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

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;

import javax.xml.transform.sax.SAXSource;

public class ServerConnection implements Initializable {
    Logger logger = Logger.getLogger(ServerConnection.class.getName());
    private static String SERVER_ADDRESS;
    private static int SERVER_PORT;

    Socket socket;
    BufferedWriter out;
    BufferedReader in;

    public userData currentUser = new userData();

    public ServerConnection() {
        Socket socket = new Socket();
        BufferedWriter out = null;
        BufferedReader in = null;
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
        for (playData i : currentUser.play_history) {
            System.out.println(i.listQuestions);
        }
    }

    public void write(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
    }

    public boolean loginUser(String username, String password) {
        try {
            write("GET /login");

            while (!in.ready());
            in.readLine();

            // Send the username and password to the server
            write(username);
            write(password);

            System.out.println(username + "||" + password);

            // Wait for the server's response
            String response = new String();
            response = in.readLine();
            System.out.println(response);

            // Check the server's response
            if (response != null && response.equals("LOGIN_SUCCESS")) {
                // Receive the user ID from the server
                String userID = in.readLine();
                System.out.printf("User ID: %s\n", userID);

                setCurrentUser(Integer.parseInt(userID), username);
                

                return true; // Login successful
            } else {
                return false; // Login failed
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "An error occurred during the connection", e);
            return false; // An error occurred during the connection
        }
    }

    public List<Object> getData() {
        currentUser.play_history = new ArrayList<>();

        try {
            out.write("GET /data");
            out.newLine();
            out.flush();

            StringBuilder jsonData = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                jsonData.append(line);
            }

            // Process the received JSON data
            String receivedData = jsonData.toString();
            System.out.println("Received JSON data:\n" + receivedData);

            ObjectMapper objectMapper = new ObjectMapper();
            List<Object> jsonObjects = objectMapper.readValue(receivedData, List.class);

            return jsonObjects;
        } catch (Exception e) {
            logger.log(Level.WARNING, "An error occurred during data retrieval", e);
            return null; // An error occurred during data retrieval
        }
    }

    public ServerConnection connect(String address, int port) {
        ServerConnection serverConnection = new ServerConnection();

        if (serverConnection.socket == null) {
            try {
                serverConnection.socket = new Socket(address, port);
                serverConnection.out = new BufferedWriter(new OutputStreamWriter(serverConnection.socket.getOutputStream()));
                serverConnection.in = new BufferedReader(new InputStreamReader(serverConnection.socket.getInputStream()));
                System.out.println("Connected to server successfully!");
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cannot connect to the server!");
                alert.showAndWait();
            }
        }

        return serverConnection;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}