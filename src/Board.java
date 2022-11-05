public abstract class Board {

    private final int edgeSize;

    public Board(int edgeSize){
        this.edgeSize = edgeSize;
    }

    // please override these methods in `LowerBoard` because it contains two boards and must modify both boards
    public abstract void markAsHit(int[][] coord);
    // don't need to have markAsMissed here because ShipBoard does not keep track of misses; only hits (for which it decrements ship health)

}