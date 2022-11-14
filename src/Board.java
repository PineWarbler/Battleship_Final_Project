public abstract class Board {

    protected final int edgeSize;

    /**
     * Creates an empty board
     * @param edgeSize
     */
    public Board(int edgeSize){
        this.edgeSize = edgeSize;
    }

    /**
     * Keeps track of the hits on the board
     * @param coord
     */
    // please override these methods in `LowerBoard` because it contains two boards and must modify both boards
    public abstract void markAsHit(int[] coord);
    // don't need to have markAsMissed here because ShipBoard does not keep track of misses; only hits (for which it decrements ship health)

    /**
     * Prints out board
     */
    public abstract void printBoard();
}