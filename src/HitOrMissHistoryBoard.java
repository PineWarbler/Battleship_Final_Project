public class HitOrMissHistoryBoard extends Board {

    private cellStatus[][] cellStatuses; // this stores a nxn representation of board cells (to be filled later)

    HitOrMissHistoryBoard(int edgeSize){
        super(edgeSize);
        // TODO: fill all elements of `cellStatuses` with cellStatus enum type `NONE`
    }
    @Override
    public void markAsHit(int[] coord){
        // TODO: change the correct cellStatus element to cellStatus enum type `HIT`
    }

    public void markAsMissed(int[] coord){
        // TODO: change the correct cellStatus element to cellStatus enum type `MISS`
    }

}
