/*
Author: Stephen Li
Project Name: Culminating Graphics
Date Created: May 29, 2026
Date Last Modified: June 10, 2026
*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button; //added
import javafx.scene.layout.Pane; //added
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.shape.Circle; //added
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos; //added (tells positions on elements)
import javafx.geometry.Insets; //added
import javafx.scene.text.Font; //added
import javafx.scene.paint.Color; //added
import javafx.scene.input.KeyCode; //added
import javafx.animation.AnimationTimer; //added
import javafx.scene.layout.Background; //added
import javafx.scene.layout.BackgroundFill; //added

public class HelloFX extends Application {
    //Track if keys for paddle are pressed
    boolean leftPressed = false;
    boolean rightPressed = false;

    @Override
    public void start(Stage stage) {
        //Menu Setup
        Label l = new Label("Brick Breaker Game");
        l.setFont(new Font("Arial", 30));
        l.setTextFill(Color.WHITE); //make background black and text white
        Button startButton = new Button("Start Game");

        StackPane menuLayer = new StackPane();
        menuLayer.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
        StackPane.setAlignment(l, Pos.TOP_CENTER); //Positions title
        StackPane.setMargin(l, new Insets(50, 8, 8, 8));
        StackPane.setAlignment(startButton, Pos.BOTTOM_CENTER); //Positions button
        StackPane.setMargin(startButton, new Insets(8, 8, 50, 8));

        menuLayer.getChildren().add(l); //Adds all elements into the menu
        menuLayer.getChildren().add(startButton);
        Scene menuScene = new Scene(menuLayer, 640, 480);

        //Create game layer
        Pane gameLayer = new Pane();
        gameLayer.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));

        //Create shapes
        Rectangle paddle = new Rectangle(270, 430, 100, 15);
        Circle ball = new Circle(320, 200, 10);

        //Add shapes into game layer
        gameLayer.getChildren().add(ball);
        gameLayer.getChildren().add(paddle);

        Scene gameScene = new Scene(gameLayer, 640, 480);

        //Checking if key pressed using booleans to have smooth paddle movement
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
                leftPressed = true;
            }
            if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
                rightPressed = true;
            }
        });
        
        gameScene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
                leftPressed = false;
            }
            if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
                rightPressed = false;
            }
        });

        //Paddle movement
        AnimationTimer gameLoop = new AnimationTimer() {
            public void handle(long time) { //Checks time in nanosecond
                double paddleX = paddle.getX();
                int paddleSpeed = 6;
                if (leftPressed == true && paddleX > 0) {
                    paddle.setX(paddleX - paddleSpeed);
                }
                else if (rightPressed == true && (paddleX < 640 - paddle.getWidth())) { //repeat for arrow keys
                    paddle.setX(paddleX + paddleSpeed);
                }
            }
        };
        
        //Countdown for ball to move
        Label countdownTimer = new Label("3");
        countdownTimer.setFont(new Font("Arial", 60));
        countdownTimer.setTextFill(Color.WHITE);
        countdownTimer.setLayoutX(300);
        countdownTimer.setLayoutY(200);
        long[] startTime = {0};
        AnimationTimer ballStart = new AnimationTimer() {
            public void handle(long time) {
                if (startTime[0] == 0) { //Sets temp variable at current time
                    startTime[0] = time;
                    gameLayer.getChildren().add(countdownTimer);
                }
                long elaspedTime = time - startTime[0]; //Check how long has passed since timer started
                if (elaspedTime >= 1_000_000_000L && elaspedTime <= 2_000_000_000L) {
                    countdownTimer.setText("2");
                }
                if (elaspedTime >= 2_000_000_000L && elaspedTime <= 3_000_000_000L) {
                    countdownTimer.setText("1");
                }
                if (elaspedTime >= 3_000_000_000L && elaspedTime <= 4_000_000_000L) {
                    countdownTimer.setText("GO");
                }
                else if (elaspedTime >= 4_000_000_000L) {
                    gameLayer.getChildren().remove(countdownTimer);
                    stop();
                }
            }
        };

        //Button action on click
        startButton.setOnAction(event -> {
            stage.setScene(gameScene);
            gameLoop.start();
            ballStart.start();
        });

        stage.setTitle("Brick Breaker Game");
        stage.setScene(menuScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}