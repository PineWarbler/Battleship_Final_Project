package com.example.GUIClass;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
        //new GameLoopStage(Difficulty.EASY, new ShipBoard(edgeSize));
        new SettingsStage(); // this starts off the chain of windows: settings -> placeShips -> GameLoop -> post-Game
    }
}

class SettingsStage extends Stage {
    VBox vb = new VBox();
    Label difficultyLabel = new Label("Opponent Difficulty:");
    RadioButton stoopitRadio = new RadioButton("EASY");
    RadioButton smartRadio = new RadioButton("MEDIUM");
    RadioButton godRadio = new RadioButton("HARD");
    Button startGame = new Button("Start Game");
    ToggleGroup tg = new ToggleGroup();

    SettingsStage() {
        smartRadio.setSelected(true); // default mode is `smart`
        // adding the radio buttons to a toggle group makes only one button selectable at a time (what we want)
        stoopitRadio.setToggleGroup(tg);
        smartRadio.setToggleGroup(tg);
        godRadio.setToggleGroup(tg);
        vb.setAlignment(Pos.TOP_CENTER);
        difficultyLabel.setAlignment(Pos.CENTER_LEFT);
        difficultyLabel.setFont(Font.font("",30));
        difficultyLabel.setPadding(new Insets(0,0,10,0));
        this.centerOnScreen();


        VBox radioBox = new VBox();
        radioBox.getChildren().addAll(stoopitRadio,smartRadio,godRadio);
        radioBox.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(radioBox, new Insets(0,0,20,160));
        //ap.getChildren().add(radioBox);
        stoopitRadio.setFont(Font.font(20));
        stoopitRadio.setAlignment(Pos.CENTER_LEFT);
        smartRadio.setFont(Font.font(20));
        godRadio.setFont(Font.font(20));
        startGame.setFont(Font.font(30));


        Label titleSettingsStage = new Label("Welcome to Battleship!");
        titleSettingsStage.setPadding(new Insets(0,0,0,0));
        titleSettingsStage.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD,40));
        vb.getChildren().addAll(titleSettingsStage,difficultyLabel, radioBox, startGame);

        startGame.setOnAction(new EventHandler<ActionEvent>() {
            // this is for the startGame button
            @Override
            public void handle(ActionEvent actionEvent) {
                Difficulty mode = Difficulty.NONE;
                if (stoopitRadio.isSelected()) {
                    mode = Difficulty.EASY;
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
        AnchorPane outerAP = new AnchorPane(vb);
        vb.setPadding(new Insets(20,30,30,40));
        this.setScene(new Scene(outerAP));
        this.show();
    }
}

class ShipPlacerStage extends Stage{

    int shipLength;
    char pivotDir;
    int[] pivotCoord;
    HBox hb = new HBox();
    Button rotate = new Button("Rotate");
    Button confirmShipLocation = new Button("Confirm Ship Location");
    Label shipPrompt = new Label(""); // TODO: update this to prompt user to place ship of length n
    Label rotatePrompt = new Label("");

    VBox vb = new VBox();
    GridPane gp = new GridPane(); // used to store array of rectangles and buttons to represent the board
    Rectangle[][] gpArray = new Rectangle[edgeSize][edgeSize]; // used to easily change other cell properties

    Button done =  new Button("Start Game");

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
        rotate.setFont(Font.font(20));
        confirmShipLocation.setFont(Font.font(20));
        Label titleShipPlacerStage = new Label("Place your ships!");
        titleShipPlacerStage.setPadding(new Insets(0,0,10,0));
        titleShipPlacerStage.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD,40));
        hb.getChildren().addAll(rotate, confirmShipLocation, shipPrompt,rotatePrompt); // these all go on same row
        vb.getChildren().add(titleShipPlacerStage);

        gp.setPadding(new Insets(10,0,15,0));
        vb.getChildren().add(hb);
        vb.getChildren().add(gp);
        vb.getChildren().add(done);

        shipLength=0;
        pivotDir = 'N';
        pivotCoord = new int[]{0,0};

        shipPrompt.setText("\tShip Length: " + allowedShipLengths[0]); // prompt user to insert ship
        shipPrompt.setAlignment(Pos.CENTER_LEFT);
        shipPrompt.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD,20));

        rotatePrompt.setText("\tDirection: " + pivotDir); // prompt user to insert ship
        rotatePrompt.setAlignment(Pos.CENTER_LEFT);
        rotatePrompt.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD,20));


        // make these buttons disabled by default until ships are placed
        done.setFont(new Font(30));
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
                drawShipBoard();
                try{
                    pivotDir = dirMap.get(rotateClickCount[0] % dirMap.size());
                    rotatePrompt.setText("\tDirection: " + pivotDir);
                    potentialOccupancyCoords = shipBoard.convertFromPivotAndDirection(pivotCoord, pivotDir, shipLength);
                    if(shipBoard.isInBounds(potentialOccupancyCoords) && shipBoard.areCoordsUnoccupied(potentialOccupancyCoords)){
                        confirmShipLocation.setDisable(false); // because position is valid and can be confirmed
                        drawPotentialShip();
                    } else{
                        gpArray[pivotCoord[1]][pivotCoord[0]].setFill(Color.GRAY);
                        confirmShipLocation.setDisable(true);
                    }
                }catch(Exception ignored){}
            }
        });

        final int[] confirmCount = {0};
        confirmShipLocation.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                confirmCount[0]++;
                shipBoard.insertShip(potentialOccupancyCoords, potentialShip); // all the error checking has already been done

                confirmShipLocation.setDisable(true);
                drawShipBoard(); // with newly added ship
                if(confirmCount[0] < allowedShipLengths.length){
                    shipPrompt.setText("\tShip Length: " + allowedShipLengths[confirmCount[0]]); // update prompt to place next ship
                }

                if(confirmCount[0] == allowedShipLengths.length){
                    // then all the ships have been placed
                    rotate.setDisable(true);
                    done.setDisable(false);
                    shipPrompt.setText(""); // no more prompts
                    rotatePrompt.setText("");
                }
            }
        });

        done.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                done.setDisable(true); // only need to press done once!
//                shipBoard.printBoard();
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
                        drawShipBoard(); // draw all confirmed ships (erase any voided ship positions)
                        String eventID = e.getPickResult().getIntersectedNode().getId(); // this trick from: https://stackoverflow.com/a/42430200

                        r.setFill(Color.GRAY); // can immediately draw the pivot cell as a different color

                        shipLength = GUIGame.allowedShipLengths[confirmCount[0]]; // length of the ship being placed

                        ElementIDParser ip = new ElementIDParser();
                        ElementData data = ip.parseID(r.getId());

                        //System.out.println(shipLength);
                        pivotDir = dirMap.get(rotateClickCount[0] % dirMap.size()); // convert rotate click counts to pivot direction char

                        pivotCoord = new int[]{data.getRow(), data.getCol()};
                        potentialOccupancyCoords = shipBoard.convertFromPivotAndDirection(pivotCoord, pivotDir, shipLength);


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

    int round = 0;
    int gameCellWidthHeight = 25; // note that this will likely be smaller than the cell dimensions for ShipPlacerStage because need to fit two boards on screen instead of one
    Player human;
    Computer armada;

    Label upperMessage;
    Label lowerMessage; // used for blank line between boards or for printing `ship sunk` messages

    Label infoBoxLab = new Label("Game Info");
    Label playerHealth = new Label("Your health: ");
    Label compHealth = new Label("Opponent health: ");
    Label roundLab = new Label("Turns: " + round);

    Popup newPop = new Popup();

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

    /**
     * this task erases the `ship sunk` message.  Initialized each time a ship is sunk
     * @param delayMs the delay time in milliseconds
     */
    // modified from https://stackoverflow.com/a/45131202
    public void eraseMessage(long delayMs){
        Thread t = new Thread(() ->{
            try { Thread.sleep(delayMs); }catch(InterruptedException ignored){}
            Platform.runLater(() -> lowerMessage.setText(""));
        });
        t.setDaemon(true);
        t.start();
    }

    public void masterHandler(String buttonGuessID) {
        // parse the buttonID for its coordinates
        ElementIDParser idParser = new ElementIDParser();
        ElementData data = idParser.parseID(buttonGuessID);

        // human guesses a coordinate...handle this guess
        int[] playerGuessCoord = new int[]{data.getRow(), data.getCol()};
        ResponsePacket rp = armada.processRequestFromOtherPlayer(playerGuessCoord);
        cellStatus response = rp.getCellStatus();
        human.processResponseFromOtherPlayer(playerGuessCoord, response);

        // display `ship sunk` message if applicable
        try{
            int sunkShipLength = rp.getSunkShipLength();
            lowerMessage.setText("Sunk ship of length " + sunkShipLength + "!");
            eraseMessage(2500L);
        } catch (IllegalArgumentException ignored){
            // if failed, that's because no ship was sunk.  See documentation for ResponsePacket.getSunkShipLength()
        }

        // computer guesses a coordinate...handle this guess
        int[] computerGuessCoord = armada.generateGuess();
        response = human.processRequestFromOtherPlayer(computerGuessCoord).getCellStatus();
        armada.processResponseFromOtherPlayer(computerGuessCoord, response);

        round++;//increase round and update text;
        roundLab.setText("Turns: " + round);

        compHealth.setText("Opponent health: " + armada.getHealth()); //Update health labels after each "round"
        playerHealth.setText("Your health: " + human.getHealth());



        // draw the updated boards on the screen
        drawUpperBoard();
        drawLowerBoard();

        if(human.hasLost() || armada.hasLost()){
            // disable further clicking
            gpUpperBoard.setDisable(true);
            gpLowerBoard.setDisable(true);

            // announce the winner with big words
            upperMessage.setFont(new Font("Arial", 30));
            if(human.hasLost()) {
                upperMessage.setText("The computer has won!");
            } else{
                upperMessage.setText("Congratulations! You've won!");
            }


            // make more button actions available
            Button viewOther = new Button("View Opponent's Board");
            viewOther.setFont(Font.font(20));
            viewOther.setOnAction(actionEvent -> {
                newPop.hide();
                new postGameStage(human.lowerBoard, armada.lowerBoard); // let user see both/opposing side's ship positions
            });

            Button quit = new Button("Quit");
            quit.setFont(Font.font(20));
            quit.setOnAction(actionEvent -> {
                newPop.hide();
                Stage s = (Stage) gpUpperBoard.getScene().getWindow(); // this and below line trick from https://stackoverflow.com/a/13602324
                s.close();
            });

            HBox hb2 = new HBox();
            hb2.setAlignment(Pos.CENTER);
            hb2.getChildren().addAll(quit, viewOther);
            hb2.setSpacing(10);



            VBox popUpVB = new VBox();
            popUpVB.getChildren().addAll(upperMessage,hb2);
            popUpVB.setSpacing(10);
            upperMessage.setFont(Font.font("",FontWeight.EXTRA_BOLD,50));

            StackPane popStack = new StackPane(popUpVB);
            //StackPane.setMargin(popStack,new Insets(20,20,20,20));

            popStack.setPadding(new Insets(20,20,20,20));
            popStack.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            popStack.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM)));

            newPop.getContent().addAll(popStack);

            newPop.setAnchorX(530);
            newPop.setAnchorY(300);

            newPop.show(this);
            //vb.getChildren().add(hb2);



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
        armada = new Computer(edgeSize, mode); // will also generate a shipBoard during the object constructor process

        // fill player instances with lowerboards and upper boards
        LowerBoard lb = new LowerBoard(new HitOrMissHistoryBoard(edgeSize), shipBoard);
        human.setLowerBoard(lb);

//        LowerBoard lb2 = new LowerBoard(new HitOrMissHistoryBoard(edgeSize), armada.generateShipBoard(edgeSize, mode));
//        armada.setLowerBoard(lb2); //dont need this I think


        //upperMessage = new Label("Difficulty: " + mode);
        upperMessage = new Label("");
        vb.getChildren().add(upperMessage);

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
//
        // what if we only display "Ship sunk" for n seconds after the sunk?
//        Label sunkLabel = new Label("Ship Sunk!");
//        sunkLabel.setFont(new Font(25));
//        vb.getChildren().add(4,sunkLabel);

        Font font = Font.font("Verdana", FontWeight.EXTRA_BOLD, 20); // the string argument doesn't do anything for some reason
        Label upper = new Label("Upper Board");
        upper.setFont(font);
        vb.getChildren().add(upper);

        vb.getChildren().add(gpUpperBoard);

        // add ship sunk label or placeholding empty line
        lowerMessage = new Label(""); // initialize to empty line at first
        lowerMessage.setTextFill(Color.RED);
        lowerMessage.setFont(font);
        vb.getChildren().add(lowerMessage); // blank line is to delineate between boards. Will be used later to show `sunk ship` message

        Label lower = new Label("Lower Board");
        lower.setFont(font);
        vb.getChildren().add(lower);
        vb.getChildren().add(gpLowerBoard);

        // set all to center alignment
        vb.setAlignment(Pos.CENTER);
        gpLowerBoard.setAlignment(Pos.CENTER);
        gpUpperBoard.setAlignment(Pos.CENTER);

        // on first load, render lowerBoard
        drawLowerBoard();

        BorderPane bp = new BorderPane(vb);
        infoBoxLab.setFont(Font.font("",FontWeight.EXTRA_BOLD, 20));

        Label diff = new Label("Opponent difficulty: " + mode);
        diff.setFont(new Font(15));
        playerHealth.setText("Your health: " + human.getHealth());
        playerHealth.setFont(new Font(15));
        compHealth.setText("Opponent health: " + armada.getHealth());
        compHealth.setFont(new Font(15));
        roundLab.setFont(new Font(15));


        VBox leftBox = new VBox();
        leftBox.setAlignment(Pos.TOP_RIGHT);
        //VBox.setMargin(leftBox, new Insets(400,0,400,0));
        leftBox.setPadding(new Insets(200,-400,0,400));
        leftBox.getChildren().addAll(infoBoxLab,roundLab,diff,playerHealth,compHealth);
        bp.setLeft(leftBox);



        this.setScene(new Scene(bp));
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

        Label title = new Label("Post-Game Review");
        title.setFont(Font.font("Arial",FontWeight.EXTRA_BOLD,30));


        Label humanBoardLabel = new Label("Your Lower Board:");
        humanBoardLabel.setFont(new Font("Arial", 20));

        Label computerBoardLabel = new Label("Your Opponent's Lower Board:");
        computerBoardLabel.setFont(new Font("Arial", 20));

        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);

        Button quit = new Button("Exit");
        quit.setFont(Font.font(20));
        quit.setOnAction(actionEvent -> Platform.exit());

        // fill the gridpanes with ships and hits/misses
        drawLowerBoard(computerLowerBoard, computerGP);
        drawLowerBoard(humanLowerBoard, humanGP);

        // put the board titles with their respective boards
        VBox computerVB = new VBox(computerBoardLabel, computerGP);
        computerVB.setSpacing(10);
        computerVB.setAlignment(Pos.CENTER);
        VBox humanVB = new VBox(humanBoardLabel, humanGP);
        humanVB.setSpacing(10);
        humanVB.setAlignment(Pos.CENTER);

        HBox boardsRow = new HBox(computerVB, new Label("\t\t"), humanVB); // the empty label is for blank space
        boardsRow.setAlignment(Pos.CENTER);

        vb.getChildren().addAll(title, boardsRow, quit);

        this.setScene(new Scene(vb));
        this.setMaximized(true);
        this.show();
    }
}

