public class Player {

    private String name;
    private int health;

    private LowerBoard lowerBoard;
    private HitOrMissHistoryBoard upperBoard;

    public Player(String name){
        this.name = name;
    }

    public Player(String name, LowerBoard lowerBoard, HitOrMissHistoryBoard upperBoard){
        // TODO: complete this constructor
    }

    public boolean hasLost(){
        return this.getHealth()<=0;
    }

    /**
     * marks the {@link #upperBoard} with response from other player about whether this player's coord guess hit other player's ship
     * @param coord the coordinate of the guess
     */
    public void processResponseFromOtherPlayer(int[][] coord, cellStatus responseStatus){
        // TODO: complete this method according to the Javadoc above
        // hint: should call this.upperBoard.markAsHit/markAsMissed depending on `responseStatus`
    }

    /**
     * used when another player asks this player if his/her shot is a hit
     * @param coord is the guessed coordinate
     * @return whether the coord is a hit or a miss <b>(should not return `NONE` enum type!)</b>
     */
    public cellStatus processRequestFromOtherPlayer(int[][] coord){
        return this.lowerBoard.processIncomingGuess(coord);
    }

    // --------------- GETTERS AND SETTERS ----------------
    public void setLowerBoard(LowerBoard lowerBoard) {
        this.lowerBoard = lowerBoard;
    }

    public void setUpperBoard(HitOrMissHistoryBoard upperBoard) {
        this.upperBoard = upperBoard;
    }

    public void setHealth(int health) {
        // TODO: loop through ships on board and return sum of their healths
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }
}
