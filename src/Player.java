public class Player {

    protected String name;
    protected int health;

    protected LowerBoard lowerBoard;
    protected HitOrMissHistoryBoard upperBoard;


    public Player(String name){

        this.name = name;
        this.health = 16;
        //make an empty lowerBoard and upperBoard by calling their constructors

        //TODO: Corn

        // TODO: complete this constructor.  Make sure to call this.getHealth to set the health value
    }

    public boolean hasLost(){
        return this.getHealth()<=0;
    }

    /**
     * marks the {@link #upperBoard} with response from other player about whether this player's coord guess hit other player's ship
     * @param coord the coordinate of the guess
     */
    public void processResponseFromOtherPlayer(int[] coord, cellStatus responseStatus){
        if(responseStatus==cellStatus.HIT){
            this.upperBoard.markAsHit(coord);
        } else if (responseStatus==cellStatus.MISS){
            this.upperBoard.markAsMissed(coord);
        }
    }

    /**
     * used when another player asks this player if his/her shot is a hit
     * @param coord is the guessed coordinate
     * @return whether the coord is a hit or a miss <b>(should not return `NONE` enum type!)</b>
     */
    public cellStatus processRequestFromOtherPlayer(int[] coord){
        return this.lowerBoard.processIncomingGuess(coord);
    }

    // --------------- GETTERS AND SETTERS ----------------
    public LowerBoard getLowerBoard(){
        return this.lowerBoard;
    }

    public HitOrMissHistoryBoard getUpperBoard(){
        return this.upperBoard;
    }

    public void setLowerBoard(LowerBoard lowerBoard) {
        this.lowerBoard = lowerBoard;
    }

    public void setUpperBoard(HitOrMissHistoryBoard upperBoard) {
        this.upperBoard = upperBoard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return this.lowerBoard.getHealth();
    }
}
