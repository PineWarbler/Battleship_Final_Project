import java.util.ArrayList;

/**
 * ShipBoard stores the locations of ships on the board in an array of integers/hashes <b>and</b> as an arraylist of ships;
 * does not keep track of hit/miss locations (that's the job of {@link HitOrMissHistoryBoard})
 */
public class ShipBoard extends Board {
    private ArrayList<Ship> shipList;
    private int[][] hashArray; // used to store hashes of ships in a grid pattern

    private static final int emptyHash = 17; // should be prime.  Hash of an empty cell

    public ShipBoard(int edgeSize) {
        super(edgeSize);
        this.shipList = new ArrayList<>();
        hashArray = new int[edgeSize][edgeSize];
    }

    /**
     * Inserts a ship into board by placing its hashes into {@link #hashArray}
     * @param occupancyCoords a 2D array of zero-based-indexed board coordinates which a single ship occupies
     * @param ship the ship to be inserted
     */
    public void insertShip(int[][] occupancyCoords, Ship ship){
        // TODO: complete this method, being sure to call ship's getHashID method!
    }

    @Override
    public void markAsHit(int[] coord) {
        // TODO: complete this method. decrement health of the ship at `coord` if hit.
    }

    public boolean isShip(int[] coord){
        return this.hashArray[coord[0]][coord[1]] != emptyHash;
    }


    /**
     * loops through ships to find the ship to whom the hashID belongs; returns that ship
     * @param hashID hashID of an unidentified ship on the board
     * @return the ship whose <code>hashID</code> matches the input <code>hashID</code>
     * @throws IllegalArgumentException if no matching ship is found (i.e. if {@code hashID}=={@link #emptyHash})
     */
    public Ship identifyShip(int hashID) throws IllegalArgumentException{
        // TODO: complete this method according to the Javadoc above
        // hint: should call isShip for efficiency
    }

    /**
     * Decrements the health of a ship if it's hit and return "HIT".  Otherwise, return "MISS".
     * @param coord coordinate of guess
     */
    public cellStatus processIncomingGuess(int[] coord){
        if(isShip(coord)) {
            // don't need a try/catch block here because `if` statement excludes the exception
            Ship shipAtCoord = identifyShip(this.hashArray[coord[0]][coord[1]]);
            shipAtCoord.decrementHealth();
            return cellStatus.HIT;
        }
        return cellStatus.MISS;
    }

    /**
     * @return the sum healths of the ships on the board
     */
    public int getHealth(){
        int healthSum = 0;
        for(Ship ship : shipList){
            healthSum += ship.getHealth();
        }
        return healthSum;
    }
}
