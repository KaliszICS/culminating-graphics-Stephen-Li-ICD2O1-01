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
import javafx.geometry.Pos; //added (tells VBox positions)

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) {
        //Menu Setup
        Label l = new Label("Brick Breaker Game");
        Button startButton = new Button("Start Game");
        Pane menuLayer = new Pane();
        menuLayer.getChildren().add(l);
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