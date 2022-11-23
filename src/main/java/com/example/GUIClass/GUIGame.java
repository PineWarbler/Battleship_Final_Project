package com.example.GUIClass;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.GUIClass.GUIGame.allowedShipLengths;
import static com.example.GUIClass.GUIGame.edgeSize;


public class GUIGame extends Application {
    public static int edgeSize = 10;
    public static int[] allowedShipLengths = new int[]{5, 4, 3, 3, 2};

    @Override
    public void start(Stage stage) throws Exception {
        new SettingsStage(); // this starts off the chain of windows: settings -> placeShips -> GameLoop
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
                Stage s = (Stage) godRadio.getScene().getWindow(); // this and below line trick from https://stackoverflow.com/a/13602324
                s.close();
                new ShipPlacerStage(mode);
            }
        });
        this.setScene(new Scene(vb, 500, 500));
        this.show();
    }
}

class ShipPlacerStage extends Stage{

    HBox hb = new HBox();
    Button rotate = new Button("Rotate");
    Button confirmShipLocation = new Button("Confirm Ship Location");
    Label shipPrompt = new Label(""); // TODO: update this to prompt user to place ship of length n

    VBox vb = new VBox();
    GridPane gp = new GridPane(); // used to store array of rectangles and buttons to represent the board
    Rectangle[][] gpArray = new Rectangle[edgeSize][edgeSize]; // used to easily change other cell properties

    Button done = new Button("Done");

    ShipBoard shipBoard = new ShipBoard(edgeSize);
    Ship potentialShip;
    int[][] potentialOccupancyCoords;

    /**
     * draws a single ship whose position is being chosen by user
     */
    public void drawPotentialShip(){
        for(int[] coord : potentialOccupancyCoords){
            gpArray[coord[1]][coord[0]].setFill(Color.PURPLE); // don't know why switching indices works, but it does...
        }
    }

    /**
     * draws the positions of ships whose positions have already been confirmed
     */
    public void drawShipBoard(){
        for(int i = 0; i<edgeSize; i++){
            for(int j = 0; j<edgeSize; j++){
                if(shipBoard.getHashArray()[j][i] != ShipBoard.emptyHash){ // don't know why switching indices works, but it does...
                    gpArray[i][j].setFill(Color.PURPLE);
                } else{
                    gpArray[i][j].setFill(Color.BLACK);
                }
            }
        }
    }

    ShipPlacerStage(String mode){
        hb.getChildren().addAll(rotate, confirmShipLocation, shipPrompt); // these all go on same row
        vb.getChildren().add(hb);
        vb.getChildren().add(gp);
        vb.getChildren().add(done);

        // make these buttons disabled by default until ships are placed
        done.setDisable(true);
        confirmShipLocation.setDisable(true);


        // for mapping rotate button clicks to directions
        final int[] rotateClickCount = {0};
        Map<Integer, Character> dirMap = new HashMap<>();
        dirMap.put(0, 'N');
        dirMap.put(1, 'E');
        dirMap.put(2, 'S');
        dirMap.put(3, 'W');
        rotate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                rotateClickCount[0]++; // just increment number of clicks
            }
        });

        final int[] confirmCount = {0};
        confirmShipLocation.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                confirmCount[0]++;
                shipBoard.insertShip(potentialOccupancyCoords, potentialShip); // all the error checking has already been done
//                System.out.println(Arrays.deepToString(potentialOccupancyCoords));
//                System.out.println(Arrays.deepToString(shipBoard.getHashArray()));
                confirmShipLocation.setDisable(true);
                drawShipBoard(); // with newly added ship

                if(confirmCount[0] == allowedShipLengths.length){
                    // then all the ships have been placed
                    rotate.setDisable(true);
                    done.setDisable(false);
                }
            }
        });

        done.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                done.setDisable(true); // only need to press done once!
                shipBoard.printBoard();
//                System.out.println(Arrays.deepToString(shipBoard.getHashArray()));
                Stage s = (Stage) hb.getScene().getWindow(); // this and below line trick from https://stackoverflow.com/a/13602324
                s.close();
                // maybe even launch the next stage
                new GameLoopStage(mode, shipBoard);
            }
        });

        // add rectangles and buttons in a grid pattern
        for(int i = 0; i< edgeSize; i++){
            for(int j = 0; j<edgeSize; j++){
                Rectangle r = new Rectangle();
                r.setWidth(50);
                r.setHeight(50);
                r.setId("Upper;" + j + ";" + i);

                EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        System.out.println("Entering rect handler a12!");
                        drawShipBoard(); // draw all confirmed ships (erase any voided ship positions)
                        String eventID = e.getPickResult().getIntersectedNode().getId(); // this trick from: https://stackoverflow.com/a/42430200
                        System.out.println(eventID);
                        r.setFill(Color.GRAY); // can immediately draw the pivot cell as a different color
//                        System.out.println("Made gray!");

                        int shipLength = GUIGame.allowedShipLengths[confirmCount[0]]; // length of the ship being placed

                        ElementIDParser ip = new ElementIDParser();
                        ElementData data = ip.parseID(r.getId());

                        System.out.println(shipLength);
                        char pivotDir = dirMap.get(rotateClickCount[0] % dirMap.size()); // convert rotate click counts to pivot direction char
//                        pivotDir = 'S';
                        System.out.println("rotateClickCount: " + rotateClickCount[0]);
                        System.out.println("" + pivotDir);
                        System.out.println("dirMap size: " + dirMap.size());
                        int[] pivotCoord = new int[]{data.getRow(), data.getCol()};
                        potentialOccupancyCoords = shipBoard.convertFromPivotAndDirection(pivotCoord, pivotDir, shipLength);

                        System.out.println(Arrays.deepToString(potentialOccupancyCoords));

                        // if it's a valid placement, draw the ship position on the board
                        if(shipBoard.isInBounds(potentialOccupancyCoords) && shipBoard.areCoordsUnoccupied(potentialOccupancyCoords)){
                            confirmShipLocation.setDisable(false); // because position is valid and can be confirmed
                            potentialShip = new Ship(shipLength);
                            drawPotentialShip();
                        } else{
                            confirmShipLocation.setDisable(true);
                        }

                    }
                };
                r.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

                gp.add(r, i, j, 1, 1);
                gpArray[i][j] = r;
//                System.out.println(Arrays.deepToString(gpArray));
                Button b = new Button("", r);
//                b.setAlignment(Pos.CENTER);
                gp.add(b, i, j, 1, 1);
            }
        }
        // the `mode` parameter only here to be forwarded to the GameLoopStage
//        x.getChildren().add(openOther);
        this.setScene(new Scene(vb, 300, 300));
        this.show();
//        openOther.setOnAction(t -> new GameLoopStage(mode));
    }
}

class GameLoopStage extends Stage {
    Label x;
    VBox y = new VBox();

    /**
     *
     * @param mode can be `stoopit`, `smart`, or `god-mode`
     * @param shipBoard the assembled shipBoard as arranged by the user in {@link SettingsStage}
     */
    GameLoopStage(String mode, ShipBoard shipBoard){
//        if(mode.equals("god")){
//            // play omnisciently
//        }
        x = new Label("You've chosen to play a game in " + mode + " mode.");
        y.getChildren().add(x);
        this.setScene(new Scene(y, 300, 300));
        this.show();

    }
}

