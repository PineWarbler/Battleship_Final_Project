package com.example.GUIClass;


import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeMap;

public class ConsoleDriver {


    static TreeMap<Character, Integer> rowMap = new TreeMap<Character, Integer>();


    static TreeMap<Integer, Integer> colMap = new TreeMap<Integer, Integer>();




    static final int edgeSize = 10;
    /**
     * allows user to input ship locations via the console
     * @param p
     * @param allowedShipLengths a list of allowed ship lengths; may contain multiple ships of the same length
     * @return
     */
    public static Player setUp(Player p, int[] allowedShipLengths) {

        LowerBoard lb = new LowerBoard(new HitOrMissHistoryBoard(edgeSize), new ShipBoard(edgeSize));


        TreeMap<Character, Integer> rowMap = new TreeMap<Character, Integer>();
        for(char c = 'A'; c<'H'; c++){
            rowMap.put(c, ((int) c) - 65);
        }

        TreeMap<Integer, Integer> colMap = new TreeMap<Integer, Integer>();
        for(int k = 1; k<11; k++){
            colMap.put(k,k-1);
        }



        Scanner sc = new Scanner(System.in);
        for(int i = 0; i<allowedShipLengths.length; i++) {
            lb.getShipBoard().printBoard();
            System.out.println("-- Choose the Location of ship length " + allowedShipLengths[i] + "--");
            System.out.println("Enter row pivot coordinate (A-J): ");




            int pivRow = rowMap.get(sc.next().charAt(0));


            System.out.println("Enter column pivot coordinate (1-10): ");



            int pivCol = colMap.get(sc.nextInt());

            System.out.println("Enter pivot direction (N,S,E,or W): ");
            char pivDir = sc.next().charAt(0);

            Ship newShip = new Ship(allowedShipLengths[i]);

            lb.getShipBoard().insertShip(new int[]{pivRow, pivCol}, pivDir, newShip);
        }
        System.out.println("The final board looks like: ");
        lb.getShipBoard().printBoard();

        p.setLowerBoard(lb);
        HitOrMissHistoryBoard ub = new HitOrMissHistoryBoard(edgeSize);
        p.setUpperBoard(ub);
        return p;
    }

    /**
     * places ships in an arbitrary pattern for testing purposes; no user input
     * @param p
     * @param allowedShipLengths
     * @return
     */
    public static Player setUpAuto(Player p, int[] allowedShipLengths) {
        LowerBoard lb = new LowerBoard(new HitOrMissHistoryBoard(edgeSize), new ShipBoard(edgeSize));


        Ship newShip = new Ship(allowedShipLengths[0]);
        lb.getShipBoard().insertShip(new int[]{1, 3}, 'E', newShip);

        newShip = new Ship(allowedShipLengths[1]);
        lb.getShipBoard().insertShip(new int[]{1, 9}, 'S', newShip);

        newShip = new Ship(allowedShipLengths[2]);
        lb.getShipBoard().insertShip(new int[]{3, 6}, 'W', newShip);

        newShip = new Ship(allowedShipLengths[3]);
        lb.getShipBoard().insertShip(new int[]{6, 5}, 'S', newShip);

        newShip = new Ship(allowedShipLengths[4]);
        lb.getShipBoard().insertShip(new int[]{8, 1}, 'E', newShip);







        p.setLowerBoard(lb);
        HitOrMissHistoryBoard ub = new HitOrMissHistoryBoard(edgeSize);
        p.setUpperBoard(ub);
        return p;
    }

    /**
     * Gets integer array of guess from a player via the console; does error checking to make sure inputs are valid and records response as already guessed in the {@link Player} class
     * @param p
     * @return
     */
    public static int[] getUsersGuess(Player p){
        Scanner sc = new Scanner(System.in);
        boolean validInput = false;
        while(!validInput) {

            System.out.println("Enter row letter of guess: ");
            char rowChar = sc.next().charAt(0);



            System.out.println("Enter column number of guess: ");
            int colNum = sc.nextInt();


            // check to see if guess is a valid character and if it's not been guessed before; otherwise, ask until valid input
            int[] potentialGuess = new int[]{rowChar - 65, colNum-1}; // -1 to revert to zero-based indexing
            if ((((rowChar - 65) < 10) && rowChar > 64) && ((colNum < 11) && (colNum>0))) {
                if(!p.hasBeenAlreadyGuessed(potentialGuess)){
                    p.recordAnswer(potentialGuess);
                    validInput = true;
                    return potentialGuess;
                } else{
                    System.out.println("You've already guessed that position.  Try again.");
                }
            } else{
                System.out.println("Invalid input.  Try again.");
            }
        }
        return null;
    }

    /**
     * asks the user for his/her preferred username
     * @param p the person whose name is being requested
     * @return
     */
    public static String getUserName(Player p){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your username: ");
        return sc.next();
    }

    public static Difficulty getDifficulty() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter computer difficulty (EASY, MEDIUM, HARD): ");
        Difficulty diff = Difficulty.NONE;
        boolean finished = false;
        while (!finished) {
            try {
                return Difficulty.valueOf(sc.next());
            } catch (Exception ignored) {}
            System.out.println("Invalid input! Please enter EASY, MEDIUM, or HARD: ");
        }

        return Difficulty.NONE;
    }


    public static void main(String[] args) {

        for(char c = 'A'; c<'H'; c++){
            rowMap.put(c, ((int) c) - 65);
        }

        for(int k = 1; k<11; k++){
            colMap.put(k,k-1);
        }




        // DO NOT NEED THIS


        // create console-based version of game (required by Dr. Hutchins)
        // bulk of logic should be elsewhere. [REYNOLDS: should be similar to contents of Game.java]
        // this should only have I/O logic
        // meaning talk to user, get user input, show changes to user

        // e.g.
        // create an instance of type Game
        // while loop allowed
        // ask for user input
        // call game.displayBoard() (of other similar method)

        // create two player objects
        Player p1=new Player("bob", edgeSize);


        p1.setName(getUserName(p1));



        Difficulty diff = getDifficulty();
        Computer p2 = new Computer(edgeSize,diff);

        int[] allowedShipLengths = new int[]{ 5, 4, 3, 3, 2};

        // get opening board positions for both players
        p1 = setUpAuto(p1, allowedShipLengths);
        p1.getLowerBoard().printBoard();

        p2.getLowerBoard().printBoard();

        Player askingPlayer = p1; // or can choose randomly who goes first
        Player respondingPlayer = p2;

        while(!p1.hasLost() && !p2.hasLost()){
            System.out.println("It's " + askingPlayer.getName() + "'s turn to guess.");
            int[] currPlayersGuess;
            if(askingPlayer.getName().equals("Armada")){
                currPlayersGuess = p2.generateGuess();
                System.out.println(askingPlayer.getName() + " guesses " + ((char) (currPlayersGuess[0] + 65)) + (currPlayersGuess[1]+1));
            } else {
                System.out.println("Upper Board:");
                askingPlayer.upperBoard.printBoard();
                System.out.println("Lower Board:");
                askingPlayer.lowerBoard.printBoard();
                currPlayersGuess = getUsersGuess(askingPlayer);
            }

            cellStatus cs = respondingPlayer.processRequestFromOtherPlayer(currPlayersGuess);
            System.out.println(askingPlayer.getName() + "'s guess was a " + cs);
            if(respondingPlayer.sunkShip && (respondingPlayer.getName().equals("Armada"))){
                System.out.println("Ship sunk!");
            }

            if(respondingPlayer.sunkShip && !(respondingPlayer.getName().equals("Armada"))){

                askingPlayer.updateShipList(respondingPlayer.sunkShipSize);
            }
            respondingPlayer.sunkShip=false;



            askingPlayer.processResponseFromOtherPlayer(currPlayersGuess, cs);


            if(askingPlayer == p1){
                askingPlayer = p2;
                respondingPlayer = p1;
            } else{
                askingPlayer = p1;
                respondingPlayer = p2;
            }
        }

        System.out.println(p1.getName() + "'s Ship Board");
        p1.getLowerBoard().getShipBoard().printBoard();

        System.out.println(p2.getName() + "'s Ship Board");
        p2.getLowerBoard().getShipBoard().printBoard();

        System.out.println(respondingPlayer.getName() + " has won!");





    }


}
