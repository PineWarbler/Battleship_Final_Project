package com.example.GUIClass;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
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
//      //   to facilitate testing the later windows, will start off sequence at later link in chain
//        LowerBoard humanLB = new LowerBoard(new HitOrMissHistoryBoard(edgeSize), new ShipBoard(edgeSize));
//        HitOrMissHistoryBoard hb = humanLB.getHistBoard();
//        hb.markAsMissed(new int[]{1,1});
//        hb.markAsHit(new int[]{2,2});
//
//        LowerBoard computerLB = new LowerBoard(new HitOrMissHistoryBoard(edgeSize), new ShipBoard(edgeSize));
//        new postGameStage(humanLB, computerLB);
         new SettingsStage(); // this starts off the chain of windows: settings -> placeShips -> GameLoop -> post-Game
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
                Difficulty mode = Difficulty.NONE;
                if (stoopitRadio.isSelected()) {
                    mode = Difficulty.EASY;
                    System.out.println("Stoopit was selected.");
                } else if (smartRadio.isSelected()) {
                    mode = Difficulty.MEDIUM;
                } else if (godRadio.isSelected()) {
                    mode = Difficulty.HARD;
                }
                Stage s = (Stage) godRadio.getScene().getWindow(); // this and below line trick from https://stackoverflow.com/a/13602324
                s.close();
                new ShipPlacerStage(mode);
            }
        });
        this.setScene(new Scene(vb, 300, 300));
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

    /**
     *
     * @param mode does nothing in this method; merely included so that I can pass its value on to the next stage of the GUI
     */
    ShipPlacerStage(Difficulty mode){
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
        vb.setAlignment(Pos.CENTER);
        gp.setAlignment(Pos.CENTER);
        hb.setAlignment(Pos.CENTER);
        this.setScene(new Scene(vb));
        this.setMaximized(true);
        this.show();
//        openOther.setOnAction(t -> new GameLoopStage(mode));
    }
}

class GameLoopStage extends Stage {

    int gameCellWidthHeight = 20; // note that this will likely be smaller than the cell dimensions for ShipPlacerStage because need to fit two boards on screen instead of one
    Player human;
    Computer armada;

    Label x;
    VBox vb = new VBox();
    GridPane gpLowerBoard = new GridPane(); // used to store array of rectangles to represent the lower board
    GridPane gpUpperBoard = new GridPane(); // used to store array of rectangles and buttons to represent the upper board

    Rectangle[][] gpArrayUpperBoard = new Rectangle[edgeSize][edgeSize]; // used to easily change other cell properties
    Rectangle[][] gpArrayLowerBoard = new Rectangle[edgeSize][edgeSize]; // used to easily change other cell properties


    /**
     * draws the lowerBoard, which includes ships and hit locations
     */
    public void drawLowerBoard(){
        for(int i = 0; i<edgeSize; i++){
            for(int j = 0; j<edgeSize; j++){
                if(human.lowerBoard.getShipBoard().getHashArray()[j][i] != ShipBoard.emptyHash){ // don't know why switching indices works, but it does...
                    gpArrayLowerBoard[i][j].setFill(Color.PURPLE);
                } else {
                    gpArrayLowerBoard[i][j].setFill(Color.BLACK);
                }

                // also add a red dot to signify a hit on the lower board
                if(human.lowerBoard.getHistBoard().getCellStatus(new int[]{j, i}) == cellStatus.HIT){
                    Circle c = new Circle();
                    double radius = (gameCellWidthHeight / 2.0) * 0.666; // size is dependent on cell size
                    c.setRadius(radius);
                    c.setFill(Color.RED);
                    gpLowerBoard.add(c, i, j, 1, 1);
                    GridPane.setHalignment(c, HPos.CENTER);

                    // TODO: remove this else-if block when done debugging
                } else if(human.lowerBoard.getHistBoard().getCellStatus(new int[]{j, i}) == cellStatus.MISS){
                    Circle c = new Circle();
                    double radius = (gameCellWidthHeight / 2.0) * 0.666; // size is dependent on cell size
                    c.setRadius(radius);
                    c.setFill(Color.WHITE);
                    gpLowerBoard.add(c, i, j, 1, 1);
                    GridPane.setHalignment(c, HPos.CENTER);
                }
            }
        }
    }

    public void drawUpperBoard(){
        for(int i = 0; i<edgeSize; i++){
            for(int j = 0; j<edgeSize; j++){
                if(human.upperBoard.getCellStatus(new int[]{j, i}) == cellStatus.HIT){ // don't know why switching indices works, but it does...
                    gpArrayUpperBoard[i][j].setFill(Color.RED);
                } else if (human.upperBoard.getCellStatus(new int[]{j, i}) == cellStatus.MISS) {
                    gpArrayUpperBoard[i][j].setFill(Color.WHITE);
                } else{ // then it's a `none` enum type
                    gpArrayUpperBoard[i][j].setFill(Color.BLACK);
                }
            }
        }
    }

    public void masterHandler(String buttonGuessID) {
        // parse the buttonID for its coordinates
        ElementIDParser idParser = new ElementIDParser();
        ElementData data = idParser.parseID(buttonGuessID);

        // human guesses a coordinate...handle this guess
        int[] playerGuessCoord = new int[]{data.getRow(), data.getCol()};
        cellStatus response = armada.processRequestFromOtherPlayer(playerGuessCoord);
        human.processResponseFromOtherPlayer(playerGuessCoord, response);

        // computer guesses a coordinate...handle this guess
        int[] computerGuessCoord = armada.generateGuess();
        response = human.processRequestFromOtherPlayer(computerGuessCoord);
        armada.processResponseFromOtherPlayer(computerGuessCoord, response);

        // draw the updated boards on the screen
        drawUpperBoard();
        drawLowerBoard();

        if(human.hasLost() || armada.hasLost()){
            // disable further clicking
            gpUpperBoard.setDisable(true);
            gpLowerBoard.setDisable(true);

            // announce the winner with big words
            x.setFont(new Font("Arial", 30));
            if(human.hasLost()) {
                x.setText("The computer has won!");
            } else{
                x.setText("Congratulations! You've won!");
            }

            // make more button actions available
            Button viewOther = new Button("View Opponent's Board");
            viewOther.setOnAction(actionEvent -> {
                new postGameStage(human.lowerBoard, armada.lowerBoard); // let user see both/opposing side's ship positions
            });

            Button quit = new Button("Quit");
            quit.setOnAction(actionEvent -> {
                Stage s = (Stage) gpUpperBoard.getScene().getWindow(); // this and below line trick from https://stackoverflow.com/a/13602324
                s.close();
            });
            HBox hb2 = new HBox();
            hb2.setAlignment(Pos.CENTER);
            hb2.getChildren().addAll(quit, viewOther);
            vb.getChildren().add(hb2);

        }
    }

    /**
     *
     * @param mode can be `stoopit`, `smart`, or `god-mode`
     * @param shipBoard the assembled shipBoard as arranged by the user in {@link SettingsStage}
     */
    GameLoopStage(Difficulty mode, ShipBoard shipBoard){
//        if(mode.equals("god")){
//            // play omnisciently
//        }
        // make two player instances
        human = new Player("player", edgeSize);
//        armada = new Computer(edgeSize); needs a difficulty now

        // fill player instances with lowerboards and upper boards
        LowerBoard lb = new LowerBoard(new HitOrMissHistoryBoard(edgeSize), shipBoard);
        human.setLowerBoard(lb);

//        LowerBoard lb2 = new LowerBoard(new HitOrMissHistoryBoard(edgeSize), armada.generateShipBoard(edgeSize, mode));
//        armada.setLowerBoard(lb2); //dont need this I think

       // // TODO: remove this line once Computer.generateGuess() has been implemented!
        //armada.getLowerBoard().getShipBoard().insertShip(new int[]{0,0}, 'E', new Ship(5));

        x = new Label("You've chosen to play a game in " + mode + " mode.");
        vb.getChildren().add(x);

        // make an upper board. this needs to be clickable
        for(int i = 0; i< edgeSize; i++){
            for(int j = 0; j<edgeSize; j++){
                Rectangle r = new Rectangle();
                r.setWidth(gameCellWidthHeight);
                r.setHeight(gameCellWidthHeight);
                r.setId("Upper;" + j + ";" + i);

                Button b = new Button("", r);

                EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {

                        String eventID = e.getPickResult().getIntersectedNode().getId(); // this trick from: https://stackoverflow.com/a/42430200
                        System.out.println(eventID);

                        b.setDisable(true); // because has already been guessed

                        masterHandler(eventID); // this class will take care of turning cells the right color

                    }
                };
                r.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

                gpArrayUpperBoard[i][j] = r;

                // actually add the elements to the GUI layout
                gpUpperBoard.add(r, i, j, 1, 1);
                gpUpperBoard.add(b, i, j, 1, 1);
            }
        }

        // make a lower board. this does not need to be clickable.
        for(int i = 0; i< edgeSize; i++){
            for(int j = 0; j<edgeSize; j++){
                Rectangle r = new Rectangle();
                r.setWidth(gameCellWidthHeight);
                r.setHeight(gameCellWidthHeight);
                r.setFill(Color.BLACK);
//                r.setId("Lower;" + j + ";" + i); // haha. don't even need event handlers!!!

                Button b = new Button("", r);
                b.setAlignment(Pos.CENTER); // has no effect
                b.setContentDisplay(ContentDisplay.CENTER); // has no effect; this trick from: https://stackoverflow.com/a/18143660
                b.setAlignment(Pos.BASELINE_CENTER); // has no effect; from: https://stackoverflow.com/a/11600380

//                EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent e) {
//
//                        String eventID = e.getPickResult().getIntersectedNode().getId(); // this trick from: https://stackoverflow.com/a/42430200
//                        System.out.println(eventID);
//
//                        b.setDisable(true); // because has already been guessed
//                        masterHandler(eventID); // this class will take care of turning cells the right color
//                    }
//                };
//                r.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

                gpArrayLowerBoard[i][j] = r;

                // actually add the elements to the GUI layout
                gpLowerBoard.add(r, i, j, 1, 1);
                gpLowerBoard.add(b, i, j, 1, 1); // still render buttons to preserve same spacing as upper board
            }
        }


        vb.getChildren().add(gpUpperBoard);
        vb.getChildren().add(new Label("")); // blank line is to delineate between boards
        vb.getChildren().add(gpLowerBoard);

        // set all to center alignment
        vb.setAlignment(Pos.CENTER);
        gpLowerBoard.setAlignment(Pos.CENTER);
        gpUpperBoard.setAlignment(Pos.CENTER);

        // on first load, render lowerBoard
        drawLowerBoard();
        this.setScene(new Scene(vb));
        this.setMaximized(true);
        this.show();

    }
}

class postGameStage extends Stage{
    VBox vb;
    GridPane computerGP;
    GridPane humanGP;
    double postGameCellWidthHeight;

    /**
     * draws the lowerBoard, which includes ships and hit locations
     */
    public void drawLowerBoard(LowerBoard lb, GridPane gp){
        for(int i = 0; i<edgeSize; i++){
            for(int j = 0; j<edgeSize; j++){
                Rectangle r = new Rectangle((double) postGameCellWidthHeight, (double) postGameCellWidthHeight);
                r.setStroke(Color.WHITE);
                if(lb.getShipBoard().getHashArray()[j][i] != ShipBoard.emptyHash){ // don't know why switching indices works, but it does...
                    r.setFill(Color.PURPLE);

                } else {
                    r.setFill(Color.BLACK);
                }
                gp.add(r, i, j, 1,1); // add the rectangle after deciding color

                Circle c = new Circle();
                double radius = (postGameCellWidthHeight / 2.0) * 0.666; // size is dependent on cell size
                c.setRadius(radius);
                // also add a red dot to signify a hit on the lower board
                if(lb.getHistBoard().getCellStatus(new int[]{j, i}) == cellStatus.HIT){
                    c.setFill(Color.RED);
                    gp.add(c, i, j, 1,1); // add circle
                    GridPane.setHalignment(c, HPos.CENTER);
                } else if(lb.getHistBoard().getCellStatus(new int[]{j, i}) == cellStatus.MISS){
                    c.setFill(Color.WHITE);
                    gp.add(c, i, j, 1,1); // add circle
                    GridPane.setHalignment(c, HPos.CENTER);
                }

            }
        }
    }

    postGameStage(LowerBoard humanLowerBoard, LowerBoard computerLowerBoard){

        vb = new VBox();

        humanGP = new GridPane();
        humanGP.setAlignment(Pos.CENTER);

        computerGP = new GridPane();
        computerGP.setAlignment(Pos.CENTER);

        postGameCellWidthHeight = 30.0f;

        System.out.println("Display both boards in this stage.");
        Label title = new Label("Post-Game Review");
        title.setFont(new Font("Arial", 30));

        Label humanBoardLabel = new Label("Your Lower Board:");
        humanBoardLabel.setFont(new Font("Arial", 20));

        Label computerBoardLabel = new Label("Your Opponent's Lower Board:");
        computerBoardLabel.setFont(new Font("Arial", 20));

        vb.setAlignment(Pos.CENTER);

        Button quit = new Button("Exit");
        quit.setOnAction(actionEvent -> Platform.exit());

        // fill the gridpanes with ships and hits/misses
        drawLowerBoard(computerLowerBoard, computerGP);
        drawLowerBoard(humanLowerBoard, humanGP);

        // put the board titles with their respective boards
        VBox computerVB = new VBox(computerBoardLabel, computerGP);
        VBox humanVB = new VBox(humanBoardLabel, humanGP);

        HBox boardsRow = new HBox(computerVB, new Label("\t\t"), humanVB); // the empty label is for blank space
        boardsRow.setAlignment(Pos.CENTER);

        vb.getChildren().addAll(title, boardsRow, quit);

        this.setScene(new Scene(vb));
        this.setMaximized(true);
        this.show();
    }
}

