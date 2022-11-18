import java.util.Scanner;

public class ConsoleDriver {
    static final int edgeSize = 10;
    /**
     * allows user to input ship locations via the console
     * @param p
     * @param allowedShipLengths a list of allowed ship lengths; may contain multiple ships of the same length
     * @return
     */
    public static Player setUp(Player p, int[] allowedShipLengths) {

        LowerBoard lb = new LowerBoard(new HitOrMissHistoryBoard(edgeSize), new ShipBoard(edgeSize));

        Scanner sc = new Scanner(System.in);
        System.out.println("-- Choose the Locations of " + p.getName() + " 's ships--");
        for(int i = 0; i<allowedShipLengths.length; i++) {
            System.out.println("Enter row pivot coordinate: ");
            int pivRow = sc.nextInt();
            System.out.println("Enter column pivot coordinate: ");
            int pivCol = sc.nextInt();
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
        for(int i = 0; i<allowedShipLengths.length; i++) {
            Ship newShip = new Ship(allowedShipLengths[i]);
            lb.getShipBoard().insertShip(new int[]{i, 0}, 'E', newShip);
        }
        p.setLowerBoard(lb);
        HitOrMissHistoryBoard ub = new HitOrMissHistoryBoard(edgeSize);
        p.setUpperBoard(ub);
        return p;
    }

    public static void main(String[] args) {

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
        Player p2=new Player("sally", edgeSize); // the other player should be a computer (but that's not completed yet)

        int[] allowedShipLengths = new int[]{5, 4, 3, 3, 2};
//        setUp(p1, allowedShipLengths);
        p1 = setUpAuto(p1, allowedShipLengths);
        p1.getLowerBoard().printBoard();

        p2 = setUpAuto(p2, allowedShipLengths);
        p2.getLowerBoard().printBoard();

        Player askingPlayer = p1; // or can choose randomly who goes first
        Player respondingPlayer = p2;
        // get opening ship positions for both
//        while(!p1.hasLost() && !p2.hasLost()){
//
//            int[] currPlayersGuess = getUsersGuess();
//            cellStatus cs = respondingPlayer.processRequestFromOtherPlayer(currPlayersGuess);
//            askingPlayer.processResponseFromOtherPlayer(currPlayersGuess, cs);
//
//            // TODO: toggle turn between askingPlayer and respondingPlayer or vice versa
//        }

    }


}
