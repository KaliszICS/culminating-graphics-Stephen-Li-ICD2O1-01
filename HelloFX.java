/*
Author: Stephen Li
Project Name: Culminating Graphics
Date Created: May 29, 2026
Date Last Modified: June 10, 2026
*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TextField;

public class HelloFX extends Application {
    //Track if keys for paddle are pressed
    boolean leftPressed = false;
    boolean rightPressed = false;
    double ballSpeedX = 4;
    double ballSpeedY = -4;
    double x = 320;
    double y = 200;
    Random random = new Random();
    List<Rectangle> bricks = new ArrayList<>();
    boolean ballStarted = false;
    AnimationTimer ballMove;
    int lives = 3;
    int score = 0;
    AnimationTimer ballCountdown;
    long[] startTime = {0};

    @Override
    public void start(Stage stage) {
        //Menu Setup
        Label label1 = new Label("Brick Breaker Game");
        label1.setFont(new Font("Arial", 30));
        label1.setTextFill(Color.WHITE); //make background black and text white
        Button startButton = new Button("Start Game");
        Button instructions = new Button("How to play");
        Button goBack = new Button("Return");
        TextField livesInput = new TextField(); //Find amount of lives wanted
        livesInput.setMaxWidth(60);
        livesInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                return;
            }
            if (!newValue.equals("1") && !newValue.equals("2") && !newValue.equals("3")) {
                livesInput.setText(oldValue);
            }
        });
        Label livesInstructions1 = new Label("Write the amount of lives(1-3)");
        livesInstructions1.setFont(new Font("Arial", 15));
        livesInstructions1.setTextFill(Color.BLACK);
        Label livesInstructions2 = new Label("(If left empty will automatically be 3)");
        livesInstructions2.setFont(new Font("Arial", 15));
        livesInstructions2.setTextFill(Color.BLACK);

        //Position items on menulayer
        StackPane menuLayer = new StackPane();
        menuLayer.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
        StackPane.setAlignment(label1, Pos.TOP_CENTER); //Positions title
        StackPane.setMargin(label1, new Insets(50, 8, 8, 8));
        StackPane.setAlignment(instructions, Pos.BOTTOM_CENTER); //Positions How to play button
        StackPane.setMargin(instructions, new Insets(8, 8, 150, 8));
        StackPane.setAlignment(startButton, Pos.BOTTOM_CENTER); //Positions button
        StackPane.setMargin(startButton, new Insets(8, 8, 100, 8));
        StackPane.setAlignment(livesInput, Pos.BOTTOM_CENTER);
        StackPane.setMargin(livesInput, new Insets(8, 8, 70, 8));
        StackPane.setAlignment(livesInstructions1, Pos.BOTTOM_LEFT);
        StackPane.setMargin(livesInstructions1, new Insets(8, 8, 75, 60));
        StackPane.setAlignment(livesInstructions2, Pos.BOTTOM_LEFT);
        StackPane.setMargin(livesInstructions2, new Insets(8, 8, 50, 60));
        
        //Adds all elements into the menu
        menuLayer.getChildren().addAll(label1, startButton, instructions, livesInput, livesInstructions1, livesInstructions2); 
        Scene menuScene = new Scene(menuLayer, 640, 480);

        //GameLayer Labels
        Label winloseText = new Label();
        Label score = new Label();
        Label livesLabel = new Label();

        //Position and edit gameLayer labels
        winloseText.setLayoutX(90);
        winloseText.setLayoutY(270);
        winloseText.setFont(new Font("Arial", 100));
        winloseText.setTextFill(Color.BLACK);
        score.setLayoutX(40);
        score.setLayoutY(20);
        score.setFont(new Font("Arial", 20));
        score.setTextFill(Color.BLACK);
        livesLabel.setLayoutX(560);
        livesLabel.setLayoutY(15);
        livesLabel.setFont(new Font("Arial", 15));
        livesLabel.setTextFill(Color.BLACK);

        //Create game layer
        Pane gameLayer = new Pane();
        gameLayer.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));

        //Create How to play layer
        StackPane manualLayer = new StackPane();
        manualLayer.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));

        //Position items on How to play layer
        StackPane.setAlignment(goBack, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(goBack, new Insets(8, 20, 20, 8));
        Label label2 = new Label("Controls"); //Edit label for subtitle controls
        label2.setFont(new Font("Arial", 40));
        StackPane.setAlignment(label2, Pos.TOP_CENTER);
        StackPane.setMargin(label2, new Insets(10, 8, 8, 8));
        Label label3 = new Label(" o Left Arrow Key / A - Move Paddle Left\n o Right Arrow Key / D - Move Paddle Right"); //Edit label for a list of main controls
        label3.setFont(new Font("Arial", 20));
        StackPane.setAlignment(label3, Pos.TOP_LEFT);
        StackPane.setMargin(label3, new Insets(60, 8, 8, 8));
        Label label4 = new Label("Objective"); //Edit label for subtitile lose and win conditions
        label4.setFont(new Font("Arial", 40));
        StackPane.setAlignment(label4, Pos.TOP_CENTER);
        StackPane.setMargin(label4, new Insets(120, 8, 8, 8));
        Label label5 = new Label(" o Destroy all bricks on the screen using the bouncing ball\n o Keep the ball from passing below your paddle.\n        o You will lose a life each time it does\n        o After you lose all your lives you will lose the game"); //Edit label for how to win and lose
        label5.setFont(new Font("Arial", 20));
        StackPane.setAlignment(label5, Pos.TOP_LEFT);
        StackPane.setMargin(label5, new Insets(170, 8, 8, 8));
        Label label6 = new Label("Tips"); //Edit label for subtitle tips
        label6.setFont(new Font("Arial", 40));
        StackPane.setAlignment(label6, Pos.TOP_CENTER);
        StackPane.setMargin(label6, new Insets(280, 8, 8, 8));
        Label label7 = new Label(" o You can slightly control the angles of the ball with the \n    paddle\n        o Hitting the ball with the left or right edge of the\n           paddle will send it sharply in that direction"); //Edit label for aiming the ball with the paddle position
        label7.setFont(new Font("Arial", 20));
        StackPane.setAlignment(label7, Pos.TOP_LEFT);
        StackPane.setMargin(label7, new Insets(330, 8, 8, 8));

        //Create shapes
        Rectangle paddle = new Rectangle(270, 430, 100, 15);
        Circle ball = new Circle(320, 300, 10);

        //Add shapes into layers
        gameLayer.getChildren().addAll(ball, paddle, winloseText, score, livesLabel);
        manualLayer.getChildren().addAll(label2, label3, label4, label5, label6, label7, goBack);

        //Create both scenes
        Scene gameScene = new Scene(gameLayer, 640, 480);
        Scene manualScene = new Scene(manualLayer, 640, 480);

        //Create grid for bricks
        int rows = 4;
        int columns = 6;
        double brickWidth = 90;
        double brickHeight = 25;
        double spacing = 10; //Space between each brick
        double gridStartX = 25; //Grids start X
        double gridStartY = 50; //Grids start Y

        //Loop for grid
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                //Find exact x and y of each brick
                double brickX = gridStartX + c * (brickWidth + spacing); //25 + 0 * (100)
                double brickY = gridStartY + r * (brickHeight + spacing);

                //Create and color brick
                Rectangle brick = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                brick.setFill(Color.RED);
                brick.setStroke(Color.BLACK);

                //Add brick into ArrayList and gameLayer
                bricks.add(brick);
                gameLayer.getChildren().add(brick);
            }
        }

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
                else if (rightPressed == true && (paddleX < 640 - paddle.getWidth())) {
                    paddle.setX(paddleX + paddleSpeed);
                }
            }
        };

        //Ball movement
        ballMove = new AnimationTimer() {
            @Override
            public void handle(long time) {
                x += ballSpeedX;
                y += ballSpeedY;
                ball.setCenterX(x); //This is to keep the ball from jolting at the very start
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
                    lives--;
                    livesLabel.setText("Balls: " + lives);
                    if (lives == 0) {
                        winloseText.setText("You Lose!");
                        ballMove.stop();
                    }
                    else {
                        ballMove.stop(); //Stop ball movement
                        x = 320; //Reset paddle location, ball location, flip ball direction
                        y = 360;
                        paddle.setX(270);
                        ballSpeedY = -Math.abs(ballSpeedY);
                        //Reset timer 
                        startTime[0] = 0;
                        ballStarted = false;
                        ballCountdown.start();
                    }
                }
                //Check if ball is touching paddle
                double paddleX = paddle.getX();
                if (y + 10 >= 430 && y + 10 <= 440) { //If ball y is touching top of paddle
                    if (x + 10 >= paddleX && x - 10 <= paddleX + 100) { //If ball is in paddle width
                        ballSpeedY = -Math.abs(ballSpeedY); //Always flip y direction
                        y = 420;
                        if (x + 10 >= paddleX && x - 10 <= paddleX + 30) { //If ball is between left edge to 1/3 of paddle
                            ballSpeedX = -Math.abs(ballSpeedY) - 1; //Aim ball direction more left
                        }
                        else if (x + 10 >= paddleX + 60 && x - 10 <= paddleX + 100) { //If ball is between 2/3 to right edge of paddle
                            ballSpeedX = Math.abs(ballSpeedX) + 1; //Aim ball direction more right
                        }
                    }
                }

                //Move ball
                ball.setCenterX(x);
                ball.setCenterY(y);

                //Brick collisions
                for (int i = bricks.size() - 1; i >= 0; i--) {
                    Rectangle brick = bricks.get(i);
                    //Find bounding edges of ball
                    double ballMinX = x - 10;
                    double ballMaxX = x + 10;
                    double ballMinY = y - 10;
                    double ballMaxY = y + 10;
                    
                    //Find bounding edges of brick to see which side the ball is hitting
                    double brickMinX = brick.getX();
                    double brickMaxX = brick.getX() + brickWidth;
                    double brickMinY = brick.getY();
                    double brickMaxY = brick.getY() + brickHeight;

                    //Check if ball and brick are overlapping on x and y levels
                    if (ballMaxX >= brickMinX && ballMinX <= brickMaxX) {
                        if (ballMaxY >= brickMinY && ballMinY <= brickMaxY) {
                            //Find how far the ball overlaps each side of the brick
                            double overlapRight = ballMaxX - brickMinX;
                            double overlapLeft = brickMaxX - ballMinX;
                            double overlapTop = ballMaxY - brickMinY;
                            double overlapBottom = brickMaxY - ballMinY;

                            //Find the smallest overlap distance to know which side was hit
                            double overlapX = Math.min(overlapLeft, overlapRight);
                            double overlapY = Math.min(overlapTop, overlapBottom);

                            //change direction correctly
                            if (overlapX < overlapY) { //Hits vertical side of brick (left or right)
                                if (ballSpeedX > 0) { //If hitting left, push ball left to not stick in brick
                                    ballSpeedX = -Math.abs(ballSpeedX);
                                    x = brickMinX - 10;
                                }
                                else { //If hitting right, push ball right to not stick
                                    ballSpeedX = Math.abs(ballSpeedX);
                                    x = brickMaxX + 10;
                                }
                            }
                            else { //Hits horizontal side of brick (top or bottom)
                                if (ballSpeedY < 0) { //If hitting bottom, push down to not stick
                                    ballSpeedY = Math.abs(ballSpeedY);
                                    y = brickMaxY + 10;
                                }
                                else { //If hitting top, push up to not stick
                                    ballSpeedY = -Math.abs(ballSpeedY);
                                    y = brickMinY - 10;
                                }
                            }
                            gameLayer.getChildren().remove(brick);
                            bricks.remove(i);
                            break; //Prevent ball from flipping direction twice if touching two breaks at once
                        }
                    }
                }
                if (bricks.size() == 0) {
                    winloseText.setText("You Win!");
                    ballMove.stop();
                }
            }
        };

        //Randomize ball speed and starting direction
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
        ballCountdown = new AnimationTimer() {
            @Override
            public void handle(long time) {
                if (startTime[0] == 0) { //Sets temp variable at current time
                    startTime[0] = time;
                    countdownTimer.setText("3");
                    if (!gameLayer.getChildren().contains(countdownTimer)) { //Only add timer if its not already on screen
                        gameLayer.getChildren().add(countdownTimer);
                    }
                }
                long elaspedTime = time - startTime[0]; //Check how long has passed since timer started
                if (elaspedTime >= 1_000_000_000L && elaspedTime <= 2_000_000_000L) {
                    countdownTimer.setText("2");
                }
                else if (elaspedTime >= 2_000_000_000L && elaspedTime <= 3_000_000_000L) {
                    countdownTimer.setText("1");
                }
                else if (elaspedTime >= 3_000_000_000L && elaspedTime <= 4_000_000_000L) {
                    countdownTimer.setText("GO");
                    if (!ballStarted) { //Ensures that the ball starts once and not 60 times
                        x = ball.getCenterX();
                        y = ball.getCenterY();
                        ballMove.start(); //Start ball movement after countdown ends
                        ballStarted = true;
                    }
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
            String inputText = livesInput.getText();
            if (!inputText.isEmpty()) {
                lives = Integer.parseInt(inputText);
            }
            else {
                lives = 3;
            }
            livesLabel.setText("Lives: " + lives);
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