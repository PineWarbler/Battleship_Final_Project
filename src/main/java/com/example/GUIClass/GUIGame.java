package com.example.GUIClass;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUIGame extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        new ShipPlacer();
    }
}

class ShipPlacer extends Stage{
    Button openOther = new Button("Open game loop window");
    HBox x = new HBox();

    ShipPlacer(){
        x.getChildren().add(openOther);
        this.setScene(new Scene(x, 300, 300));
        this.show();
        openOther.setOnAction(t -> new GameLoopStage());
    }
}

class GameLoopStage extends Stage {
    Label x = new Label("Second stage");
    VBox y = new VBox();

    GameLoopStage(){
        y.getChildren().add(x);
        this.setScene(new Scene(y, 300, 300));
        this.show();
    }
}

