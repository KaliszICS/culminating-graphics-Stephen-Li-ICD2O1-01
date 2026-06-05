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
import java.util.Random; //added

public class HelloFX extends Application {
    //Track if keys for paddle are pressed
    boolean leftPressed = false;
    boolean rightPressed = false;
    double ballSpeedX = 4;
    double ballSpeedY = -4;
    double x = 320;
    double y = 200;
    Random random = new Random();

    @Override
    public void start(Stage stage) {
        //Menu Setup
        Label label1 = new Label("Brick Breaker Game");
        label1.setFont(new Font("Arial", 30));
        label1.setTextFill(Color.WHITE); //make background black and text white
        Button startButton = new Button("Start Game");
        Button instructions = new Button("How to play");
        Button goBack = new Button("Return");

        //Position items on menulayer
        StackPane menuLayer = new StackPane();
        menuLayer.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
        StackPane.setAlignment(label1, Pos.TOP_CENTER); //Positions title
        StackPane.setMargin(label1, new Insets(50, 8, 8, 8));
        StackPane.setAlignment(instructions, Pos.BOTTOM_CENTER); //Positions How to play button
        StackPane.setMargin(instructions, new Insets(8, 8, 150, 8));
        StackPane.setAlignment(startButton, Pos.BOTTOM_CENTER); //Positions button
        StackPane.setMargin(startButton, new Insets(8, 8, 100, 8));
        
        //Adds all elements into the menu
        menuLayer.getChildren().addAll(label1, startButton, instructions); 
        Scene menuScene = new Scene(menuLayer, 640, 480);

        //Create game layer
        Pane gameLayer = new Pane();
        gameLayer.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));

        //Create How to play layer
        StackPane manualLayer = new StackPane();
        manualLayer.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));

        //Position items on How to play layer
        StackPane.setAlignment(goBack, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(goBack, new Insets(8, 8, 8, 8));
        Label label2 = new Label("Controls"); //Create and position label2
        label2.setFont(new Font("Arial", 40));
        StackPane.setAlignment(label2, Pos.TOP_CENTER);
        StackPane.setMargin(label2, new Insets(10, 8, 8, 8));
        Label label3 = new Label(" o Left Arrow Key / A - Move Paddle Left\n o Right Arrow Key / D - Move Paddle Right"); //Create and position label3 
        label3.setFont(new Font("Arial", 20));
        StackPane.setAlignment(label3, Pos.TOP_CENTER);
        StackPane.setMargin(label3, new Insets(60, 8, 8, 8));
        Label label4 = new Label("Objective"); //Create and position label4
        label4.setFont(new Font("Arial", 40));
        StackPane.setAlignment(label4, Pos.TOP_CENTER);
        StackPane.setMargin(label4, new Insets(130, 8, 8, 8));
        Label label5 = new Label(" o Destroy all bricks on the screen using the bouncing ball\n o Keep the ball from passing below your paddle.\n      o You will lose the game if it does"); //Create and position label5
        label5.setFont(new Font("Arial", 20));
        StackPane.setAlignment(label5, Pos.TOP_CENTER);
        StackPane.setMargin(label5, new Insets(180, 8, 8, 8));

        //Create shapes
        Rectangle paddle = new Rectangle(270, 430, 100, 15);
        Circle ball = new Circle(320, 200, 10);

        //Add shapes into layers
        gameLayer.getChildren().addAll(ball, paddle);
        manualLayer.getChildren().addAll(label2, label3, label4, label5, goBack);

        //Create both scenes
        Scene gameScene = new Scene(gameLayer, 640, 480);
        Scene manualScene = new Scene(manualLayer, 640, 480);

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
        AnimationTimer paddleMove = new AnimationTimer() {
            @Override
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

        //Ball movement
        AnimationTimer ballMove = new AnimationTimer() {
            @Override
            public void handle(long time) {
                x += ballSpeedX;
                y += ballSpeedY;
                if (x - 10 < 0) { //If ball is touching left wall
                    ballSpeedX = Math.abs(ballSpeedX);
                    x = 10; //Force ball right and flips movement to not get stuck in wall
                }
                else if (x + 10 > 640) { //If ball is touching right wall
                    ballSpeedX = -Math.abs(ballSpeedX);
                    x = 630; //Force ball left and flips movement to not get stuck in wall
                }
                if (y - 10 < 0) { //If ball is touching top wall
                    ballSpeedY = Math.abs(ballSpeedY);
                    y = 10; //Force ball down and flips movement to not get stuck in wall
                }
                else if (y + 10 > 480) { //if ball is touching bottom wall
                    ballSpeedY = -Math.abs(ballSpeedY);
                    y = 470; //Force ball up and flips movement to not get stuck in wall
                }
                double paddleX = paddle.getX(); //Check if ball is touching paddle
                if (y + 10 >= 430 && y - 10 <= 445) { //If ball y is touching top of paddle
                    if (x + 10 >= paddleX && x - 10 <= paddleX + 100) { //If ball is between left edge to right edge of paddle
                        ballSpeedY = -Math.abs(ballSpeedY); //Force ball upwards and flip direction
                        y = 420;
                    }
                }
                //Move ball
                ball.setCenterX(x);
                ball.setCenterY(y);
            }
        };

        //Randomize ball speed
        double ballSpeedRandom = 3.0 + (random.nextDouble() * 1.5); //Ensures the x value is between 3.0 to 4.5
        if (random.nextBoolean()) { //If true ball starts going right
            ballSpeedX = ballSpeedRandom;
        }
        else { //If false ball starts going left
            ballSpeedX = -ballSpeedRandom;
        }
        ballSpeedY = -(3.0 + (random.nextDouble() * 1.5)); //Ensures the y value is between -3.0 and -4.5
        
        //Countdown for ball to move
        Label countdownTimer = new Label("3");
        countdownTimer.setFont(new Font("Arial", 60));
        countdownTimer.setTextFill(Color.WHITE);
        countdownTimer.setLayoutX(300);
        countdownTimer.setLayoutY(200);
        long[] startTime = {0};
        AnimationTimer ballCountdown = new AnimationTimer() {
            @Override
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
                    ballMove.start(); //Start ball movement after countdown ends
                }
                else if (elaspedTime >= 4_000_000_000L) {
                    gameLayer.getChildren().remove(countdownTimer);
                    stop(); //Stop countdown timer so it doesnt keep running in background.
                }
            }
        };

        //Button for starting the game
        startButton.setOnAction(event -> {
            stage.setScene(gameScene);
            paddleMove.start();
            ballCountdown.start();
        });

        //Button for how to play
        instructions.setOnAction(event -> {
            stage.setScene(manualScene);
        });

        //Button to return to main menu
        goBack.setOnAction(event -> {
            stage.setScene(menuScene);
        });

        stage.setTitle("Brick Breaker Game");
        stage.setScene(menuScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}