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
import javafx.scene.layout.VBox; //added
import javafx.stage.Stage;
import javafx.scene.shape.Circle; //added
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos; //added (tells positions on elements)
import javafx.geometry.Insets; //added
import javafx.scene.text.Font; //added
import javafx.scene.paint.Color; //added
import javafx.scene.input.KeyCode; //added

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) {
        //Menu Setup
        Label l = new Label("Brick Breaker Game");
        l.setFont(new Font("", 30));
        l.setTextFill(Color.RED); //make background black and text white
        Button startButton = new Button("Start Game");
        StackPane menuLayer = new StackPane();
        StackPane.setAlignment(l, Pos.TOP_CENTER); //Positions title
        StackPane.setMargin(l, new Insets(50, 8, 8, 8));
        StackPane.setAlignment(startButton, Pos.BOTTOM_CENTER); //Positions button
        StackPane.setMargin(startButton, new Insets(8, 8, 50, 8));
        menuLayer.getChildren().add(l); //Adds all elements into the menu
        menuLayer.getChildren().add(startButton);
        Scene menuScene = new Scene(menuLayer, 640, 480);

        //Create game layer
        Pane gameLayer = new Pane();

        //Create shapes
        Rectangle paddle = new Rectangle(270, 430, 100, 15);
        Circle ball = new Circle(320, 200, 10);

        //Add shapes into game layer
        gameLayer.getChildren().add(ball);
        gameLayer.getChildren().add(paddle);

        Scene gameScene = new Scene(gameLayer, 640, 480);

        //Paddle movement
        gameScene.setOnKeyPressed(event -> {
            double paddleX = paddle.getX();
            int paddleSpeed = 15;
            if (event.getCode() == KeyCode.A) {
                if (paddleX > 0) { //Prevent going through left wall
                    paddle.setX(paddleX - paddleSpeed);
                }
            }
            else if (event.getCode() == KeyCode.D) { //repeart for arrow keys
                if (paddleX < 640 - paddle.getWidth()) { //Prevent going past right wall
                    paddle.setX(paddleX + paddleSpeed);
                }
            }
        });

        //Button action on click
        startButton.setOnAction(e -> {
            stage.setScene(gameScene);
        });

        stage.setTitle("Brick Breaker Game");
        stage.setScene(menuScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}