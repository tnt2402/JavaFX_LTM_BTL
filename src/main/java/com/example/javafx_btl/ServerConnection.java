package com.example.javafx_btl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection {
    private static String SERVER_ADDRESS;
    private static int SERVER_PORT;

    public void setServerAddress(String TMP_SERVER_ADDRESS, int TMP_SERVER_PORT) {
        SERVER_ADDRESS = TMP_SERVER_ADDRESS;
        SERVER_PORT = TMP_SERVER_PORT;
    }

    public boolean loginUser(String username, String password) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

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
            e.printStackTrace();
            return false; // An error occurred during the connection
        }
    }
}