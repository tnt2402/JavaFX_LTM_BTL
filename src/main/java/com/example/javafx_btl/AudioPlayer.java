package com.example.javafx_btl;

import javafx.application.Application;
import javafx.stage.Stage;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class AudioPlayer extends Application {
    private Queue<String> audioQueue = new LinkedList<>();
    private Player currentAudioPlayer;

    public boolean getStatus() {
        return !audioQueue.isEmpty() || (currentAudioPlayer != null && !currentAudioPlayer.isComplete());
    }

    public void Play(String audioFilePath) {
        audioQueue.offer(audioFilePath);
        if (currentAudioPlayer == null || currentAudioPlayer.isComplete()) {
            playNext();
        }
    }

    private void playNext() {
        String audioFilePath = audioQueue.poll();
        if (audioFilePath == null) return;

        File file = new File(audioFilePath);
        if (file.exists()) {
            try {
                InputStream inputStream = new BufferedInputStream(new FileInputStream(audioFilePath));
                currentAudioPlayer = new Player(inputStream);

                Thread playbackThread = new Thread(() -> {
                    try {
                        currentAudioPlayer.play();
                        currentAudioPlayer.close(); // Close the player after playback is finished
                        playNext(); // Play the next file in the queue
                    } catch (Exception ex) {
                        System.out.println("Error occurred during playback process: " + ex.getMessage());
                        playNext(); // Play the next file even if an error occurs
                    }
                });

                playbackThread.setDaemon(true); // Mark the thread as daemon so it doesn't prevent application shutdown
                playbackThread.start();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (JavaLayerException e) {
                throw new RuntimeException(e);
            }
        } else {
            playNext(); // File not found, move to the next file in the queue
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        // You can put your JavaFX UI code here if needed
    }

    public static void main(String[] args) {
        launch(args);
    }
}