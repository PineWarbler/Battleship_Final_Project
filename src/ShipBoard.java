import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ShipBoard stores the locations of ships on the board in an array of integers/hashes <b>and</b> as an arraylist of ships;
 * does not keep track of hit/miss locations (that's the job of {@link HitOrMissHistoryBoard})
 */
public class ShipBoard extends Board {
    private ArrayList<Ship> shipList; // could infer these from a <set> of hashes in array and match those to each ship
    protected int[][] hashArray; // used to store hashes of ships in a grid pattern; cells accessed by (row, column)

    private static final int emptyHash = 17; // should be prime.  Hash of an empty cell

    /**
     * ShipBoard constructor
     * @param edgeSize
     */
    public ShipBoard(int edgeSize) {
        super(edgeSize);
        this.shipList = new ArrayList<>();
        hashArray = new int[edgeSize][edgeSize];
        this.initializeAsEmpty(hashArray);
    }

    /**
     * fills all elements in {@link #hashArray} with the {@link #emptyHash}
     * @param ha the hashArray
     */
    public void initializeAsEmpty(int[][] ha){
        for(int i = 0; i<ha.length; i++){ // for each column coord
            for(int j = 0; j<ha[0].length; j++){ // for each row coord
                this.hashArray[i][j] = emptyHash;
            }
        }
    }

    /**
     * Checks each coordinate to see if all are unoccupied; otherwise returns false
     * @param coords
     * @return whether the requested coordinates are unoccupied by another ship
     */
    public boolean areCoordsUnoccupied(int[][] coords){
        for(int[] coord : coords){
            int coordRow = coord[0];
            int coordCol = coord[1];
            if(this.hashArray[coordRow][coordCol] != emptyHash){ // a ship has a non-empty hash
                return false; // because coordinate is already occupied
            }
        }
        return true; // because the method hasn't returned false yet
    }
    /**
     * Inserts a ship into board by placing its hashes into {@link #hashArray}
     * @param occupancyCoords a 2D array of zero-based-indexed board coordinates which a single ship occupies
     * @param ship the ship to be inserted
     * @throws IllegalArgumentException if any of the requested board slots is already occupied by another ship
     */
    public void insertShip(int[][] occupancyCoords, Ship ship) throws IllegalArgumentException {
        if(!areCoordsUnoccupied(occupancyCoords)){
            throw new IllegalArgumentException("One or more of the requested occupancy coordinates is already occupied and unavailable.");
        } else {
            int hashID = ship.getHashID();
            for (int[] coord : occupancyCoords) {
                int coordRow = coord[0];
                int coordCol = coord[1];
                this.hashArray[coordRow][coordCol] = hashID;
            }
            // make sure to include bow and stern coords also!
            ship.setBowCoord(occupancyCoords[0]);
            ship.setBowCoord(occupancyCoords[occupancyCoords.length-1]);
            this.shipList.add(ship); // add the ship to the list
        }
    }

    /**
     * Inserts a ship into board by placing its hashes into {@link #hashArray}
     * @param pivotCoord either the bow or stern coord
     * @param direction options: 'N', 'S', 'E', 'W' like compass direction; direction in which rest of ship lies relative to the {@code startCoord}
     * @throws IllegalArgumentException if any of the requested board slots is already occupied by another ship
     */
    public void insertShip(int[] pivotCoord, char direction, Ship ship) throws IllegalArgumentException {
        // first compile a list of the requested coordinates and make sure they're unoccupied
        int[][] occupancyCoords = convertFromPivotAndDirection(pivotCoord, direction, ship.getLength());
        insertShip(occupancyCoords, ship); // this method will raise the required exception if necessary
    }

    /**
     * Stores vector components for movement inside a 2D field
     * @param pivotCoord
     * @param direction
     * @param length
     * @return coordinates that are occupied
     */
    public int[][] convertFromPivotAndDirection(int[] pivotCoord, char direction, int length){
        // this hashmap stores vector components for movement inside a 2D field
        // vector components : (ΔRow, ΔColumn); note that positive Δ is towards bottom/right of 2D field!
        HashMap<Character, int[]> dir2Delta = new HashMap<>();
        dir2Delta.put('N', new int[]{-1, 0});
        dir2Delta.put('S', new int[]{1, 0});
        dir2Delta.put('E', new int[]{0, 1});
        dir2Delta.put('W', new int[]{0, -1});

        int[][] occupancyCoords = new int[length][2]; // array to contain # of elements given by `length` which are each 2 long
        occupancyCoords[0] = pivotCoord; // populate first element with the pivot coordinate
        for(int i = 1; i<length; i++){
            int[] previousCoord = occupancyCoords[i-1];
            occupancyCoords[i] = addVectors(previousCoord, dir2Delta.get(direction));
        }
        return occupancyCoords;
    }

    /**
     * Performs element-wise addition on two input vectors
     * @param m1 one vector
     * @param m2 the second vector
     * @return
     */
    private int[] addVectors(int[] m1, int[] m2){
        return new int[]{m1[0]+m2[0], m1[1]+m2[1]};
    }

    /**
     * Decrements the health of the ship at 'coord' if hit
     * @param coord
     */
    @Override
    public void markAsHit(int[] coord) {
        identifyShip(this.hashArray[coord[0]][coord[1]]).decrementHealth();

    }

    /**
     * Determines whether or not there is a ship present or not
     * @param coord
     * @return
     */
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
        for (Ship i : shipList){
            if(i.getHashID() == hashID){
                return i;
            }
        }
        throw new IllegalArgumentException("No matching ship is found.");
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


    /**
     * Prints ship board
     */
    @Override
    public void printBoard() {
        // weir

        int top = 1;
        int side = 65;
        for(int i = 0; i<super.edgeSize+1;i++){
            StringBuilder sb = new StringBuilder();

            if(i==0){
                sb.append(" ");
                sb.append(" | ");
                for(int j=1;j<super.edgeSize+1;j++){
                    if(j<10){
                        sb.append(top);
                        top++;
                        if (j != super.edgeSize) {
                            sb.append(" | ");
                        }
                    }else{
                        sb.append(top);
                        top++;
                        if (j != super.edgeSize) {
                            sb.append("| ");
                        }
                    }
                }
            }
            else {


                sb.append((char) side);
                sb.append(" | ");
                side++;


                for (int j = 1; j < super.edgeSize+1; j++) {


                    if(this.hashArray[i-1][j-1]!=0){
                        sb.append("\u001B[47m"+"\u001B[1m" + "⬜"+ "\033[0m");
                    }
                    else{
                        sb.append(" ");
                    }
                    if (j != super.edgeSize ) {
                        sb.append(" | ");
                    }

                }
            }
            System.out.println(sb);
            System.out.print("——————");
            for(int k = 1;k<super.edgeSize;k++){
                System.out.print("————");
            }
            System.out.println("");
        }
        System.out.println("");
    }


}
