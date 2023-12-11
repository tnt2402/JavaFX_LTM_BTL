package com.example.javafx_btl;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;

public class FXMLCongratulationController {
    @FXML
    private Canvas fireworksCanvas;
    @FXML
    private Button congratulationsButton;

    private FireworksSample.SanFranciscoFireworks sanFranciscoFireworks;
    private Stage stage;

    @FXML
    public void initialize() {
        GraphicsContext gc = fireworksCanvas.getGraphicsContext2D();
        sanFranciscoFireworks = new FireworksSample.SanFranciscoFireworks(gc);

        congratulationsButton.setOnAction(event -> {
            if (sanFranciscoFireworks.isRunning()) {
                sanFranciscoFireworks.stop();
                congratulationsButton.setText("Congratulations!");
            } else {
                sanFranciscoFireworks.start();
                congratulationsButton.setText("Stop the Show");
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(event -> {
            sanFranciscoFireworks.stop();
        });
    }

    public static class FireworksSample {
        public static class SanFranciscoFireworks {
            private final AnimationTimer timer;
            private final GraphicsContext gc;
            private final Group root;
            private final ParticleSystem particleSystem;
            private boolean running = false;

            public SanFranciscoFireworks(GraphicsContext gc) {
                this.gc = gc;
                root = new Group();
                particleSystem = new ParticleSystem(gc);

                timer = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        clearCanvas();
                        particleSystem.update();
                        particleSystem.draw();
                        checkStopCondition();
                    }
                };
            }

            public void start() {
                if (!running) {
                    running = true;
                    root.getChildren().add(particleSystem.getCanvas());
                    startTimer();
                }
            }

            public void stop() {
                if (running) {
                    running = false;
                    stopTimer();
                    root.getChildren().remove(particleSystem.getCanvas());
                }
            }

            public boolean isRunning() {
                return running;
            }

            private void startTimer() {
                timer.start();
            }

            private void stopTimer() {
                timer.stop();
            }

            private void clearCanvas() {
                gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            }

            private void checkStopCondition() {
                if (!particleSystem.hasActiveParticles()) {
                    stop();
                }
            }
        }

        public static class ParticleSystem {
            private final GraphicsContext gc;
            private final Particle[] particles;
            private int activeParticles;

            public ParticleSystem(GraphicsContext gc) {
                this.gc = gc;
                particles = new Particle[200];
                activeParticles = 0;
            }

            public void update() {
                for (int i = 0; i < activeParticles; i++) {
                    if (particles[i].update()) {
                        swapParticles(i, activeParticles - 1);
                        i--;
                        activeParticles--;
                    }
                }
                if (Math.random() < 0.03) {
                    createNewParticle();
                }
            }

            public void draw() {
                for (int i = 0; i < activeParticles; i++) {
                    particles[i].draw(gc);
                }
            }

            public boolean hasActiveParticles() {
                return activeParticles > 0;
            }

            private void createNewParticle() {
                if (activeParticles < particles.length) {
                    particles[activeParticles] = new Particle();
                    activeParticles++;
                }
            }

            private void swapParticles(int index1, int index2) {
                Particle temp = particles[index1];
                particles[index1] = particles[index2];
                particles[index2] = temp;
            }
        }

        public static class Particle {
            private static final double GRAVITY = 0.06;
            private static final double FADE_SPEED = 0.01;
            private static final double MIN_VELOCITY = -4.0;
            private static final double MAX_VELOCITY = 4.0;
            private static final double SIZE = 3.0;
            private static final double START_X = 512.0;
            private static final double START_Y = 250.0;
            private static final double START_VELOCITY_X = -1.0;
            private static final double START_VELOCITY_Y = -5.0;
            private static final double TARGET_X = 512.0;
            private static final double TARGET_Y = 50.0;
            private static final int LIFETIME = 120;

            private double x;
            private double y;
            private double velocityX;
            private double velocityY;
            private int lifetime;

            public Particle() {
                x = START_X;
                y = START_Y;
                velocityX = START_VELOCITY_X + (Math.random() * (MAX_VELOCITY - MIN_VELOCITY));
                velocityY = START_VELOCITY_Y + (Math.random() * (MAX_VELOCITY - MIN_VELOCITY));
                lifetime = LIFETIME;
            }

            public boolean update() {
                if (lifetime <= 0) {
                    return true; // particle expired
                }

                velocityY += GRAVITY;
                x += velocityX;
                y += velocityY;
                lifetime--;
                return false;
            }

            public void draw(GraphicsContext gc) {
                double alpha = (double) lifetime / LIFETIME;
                gc.setFill(Color.rgb(255, 255, 255, alpha));
                gc.fillOval(x, y, SIZE, SIZE);
            }
        }
    }
}