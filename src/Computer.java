import java.util.TreeSet;

public class Computer extends  Player{

    private int[][] consecutiveHits;
    private Boolean wasHit;

    /**
     * Constructor for the Computer class
     */
    //Constructs a computer as "armada." Creates possible moves.
    public Computer() {
        super("Armada");
        //Jon
    }


    /**
     * Generates a guess based on logic
     * @return
     */
    public int[] generateGuess(){
        //Jon
        return new int[]{0, 0};
    }






}
