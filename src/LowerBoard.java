public class LowerBoard {
    // `LowerBoard` has both a `HitOrMissHistoryBoard` and a `ShipBoard`.
    // Analogous to real-life game setup.

    private HitOrMissHistoryBoard histBoard;
    private ShipBoard shipBoard;

    public LowerBoard(HitOrMissHistoryBoard historyBoard, ShipBoard shipBoard){
        // TODO: complete this constructor
    }


    /**
     * Does two things: updates history board <b>and</b> updates ship board of lower board<br>
     * calls {@link #shipBoard}'s <code>processGuess</code> method
     * @param coord
     * @return whether cell is a hit or not
     */
    public cellStatus processIncomingGuess(int[][] coord) {
        // TODO: complete this method according to the Javadoc above
        // should call shipBoard.processIncomingGuess and
        // should call histBoard.markAsHit/markAsMiss depending on output of shipBoard.isShip()
    }

    /**
     * inserts ship into the ShipBoard
     * @param coord coordinates of points which the
     * @param ship the ship to be placed
     */
    public void placeShip(int[][] coord, Ship ship){
        // TODO: should call shipBoard.insertShip
    }

    public int getHealth(){
        return this.shipBoard.getHealth();
    }
}
