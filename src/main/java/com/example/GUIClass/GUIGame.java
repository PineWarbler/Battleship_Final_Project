package com.example.GUIClass;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUIGame extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        new SettingsStage();
    }
}

class SettingsStage extends Stage {
    VBox vb = new VBox();
    Label difficultyLabel = new Label("Computer difficulty:");
    RadioButton stoopitRadio = new RadioButton("stoopit");
    RadioButton smartRadio = new RadioButton("smart");
    RadioButton godRadio = new RadioButton("god-mode");
    Button startGame = new Button("Start Game");
    ToggleGroup tg = new ToggleGroup();

    SettingsStage() {
        smartRadio.setSelected(true); // default mode is `smart`
        // adding the radio buttons to a toggle group makes only one button selectable at a time (what we want)
        stoopitRadio.setToggleGroup(tg);
        smartRadio.setToggleGroup(tg);
        godRadio.setToggleGroup(tg);

        vb.getChildren().addAll(difficultyLabel, stoopitRadio, smartRadio, godRadio, startGame);

        startGame.setOnAction(new EventHandler<ActionEvent>() {
            // this is for the startGame button
            @Override
            public void handle(ActionEvent actionEvent) {
                String mode = "";
                if (stoopitRadio.isSelected()) {
                    mode = "stoopit";
                    System.out.println("Stoopit was selected.");
                } else if (smartRadio.isSelected()) {
                    mode = "smart";
                } else if (godRadio.isSelected()) {
                    mode = "god";
                }
                this.hide();
                new ShipPlacerStage(mode);
            }
        });
        this.setScene(new Scene(vb, 500, 500));
        this.show();
    }
}

class ShipPlacerStage extends Stage{
    Button openOther = new Button("Open game loop window");
    HBox x = new HBox();

    ShipPlacerStage(String mode){
        // the `mode` parameter only here to be forwarded to the GameLoopStage
        x.getChildren().add(openOther);
        this.setScene(new Scene(x, 300, 300));
        this.show();
        openOther.setOnAction(t -> new GameLoopStage(mode));
    }
}

class GameLoopStage extends Stage {
    Label x = new Label("Second stage");
    VBox y = new VBox();

    GameLoopStage(String mode){
        if(mode.equals("god")){
            // play omnisciently
        }
        y.getChildren().add(x);
        this.setScene(new Scene(y, 300, 300));
        this.show();

    }
}

