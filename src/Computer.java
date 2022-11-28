import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.Arrays;

public class Computer extends Player{

    private int consecutiveHits;
    private ArrayList<int[]> possibleMoves;
    private int boardSize;


    public static void main(String[] args) {
        Computer p = new Computer(10);

        p.consecutiveHits = 1;
       p.alreadyGuessed.add( new int[]{3,1});
//        p.alreadyGuessed.add(new int[]{3,1});
//        p.alreadyGuessed.add(new int[]{3,2});
//        p.alreadyGuessed.add(new int[]{3,3});
//        p.alreadyGuessed.add(new int[]{3,4});
//        p.alreadyGuessed.add(new int[]{3,5});

//        for(int[] i : p.possibleMoves){
//            System.out.println("" + i[0] + i[1]);
//        }



        int[] guess = p.generateGuess();
        System.out.println("" + guess[0]+","+guess[1]);



    }



    /**
     * Constructor for the Computer class
     */
    //Constructs a computer as "armada." Creates possible moves.
    public Computer(int edgeSize) {
        super("Armada", edgeSize);
        this.boardSize = edgeSize;
//        alreadyGuessed = new ArrayList<>();
        consecutiveHits = 0;
        possibleMoves = new ArrayList<>();

        for(int i = 0;i<edgeSize;i++){
            for(int j = 0; j<edgeSize;j++){
                possibleMoves.add( new int[]{i,j});
            }
        }


    }

    public boolean isPossible(int[] coord, ArrayList<int[]> moves){
        for(int[] a : moves){
            if(Arrays.equals(coord, a)){
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a guess based on logic
     * @return
     */
    public int[] generateGuess(){
        //Jon

        ArrayList<int[]> tempPossibleMoves = new ArrayList<>();
        if (consecutiveHits == 1) {

            int[] lastHit = alreadyGuessed.get(alreadyGuessed.size() - 1);

            int xcord = lastHit[1];
            int ycord = lastHit[0];


            if (xcord > 0) {
                int[] left = {ycord, xcord - 1};
                if (isPossible(left,possibleMoves)) {
                    tempPossibleMoves.add(left);
                }
            }

            if (xcord + 1 <= this.boardSize - 1) {
                int[] right = {ycord, xcord + 1};
                if (isPossible(right,possibleMoves)) {
                    tempPossibleMoves.add(right);
                }
            }

            if (ycord > 0) {
                int[] down = {ycord - 1, xcord};
                if (isPossible(down,possibleMoves)) {
                    tempPossibleMoves.add(down);
                }
            }

            if (ycord + 1 <= boardSize - 1) {
                int[] up = {ycord + 1, xcord};
                if (isPossible(up,possibleMoves)) {
                    tempPossibleMoves.add(up);
                }
            }
        } else if(consecutiveHits>1 && consecutiveHits<5){
            boolean vert = (alreadyGuessed.get(alreadyGuessed.size() - 1))[1] == (alreadyGuessed.get(alreadyGuessed.size() - 2))[1];
            boolean horiz = (alreadyGuessed.get(alreadyGuessed.size() - 1))[0] == (alreadyGuessed.get(alreadyGuessed.size() - 2))[0];

            if (vert) {
                int xcord = (alreadyGuessed.get(alreadyGuessed.size() - 1))[1];

                TreeSet<Integer> ytrend = new TreeSet<>();
                for (int i = 0; i < consecutiveHits; i++) {
                    ytrend.add((alreadyGuessed.get(alreadyGuessed.size() - 1 - i))[0]);
                }
                if (ytrend.first() - 1 > 0) {

                    int[] down = {ytrend.first() - 1, xcord};

                    if (isPossible(down,possibleMoves)) {
                        tempPossibleMoves.add(down);
                    }
                }
                if (ytrend.last() + 1 <= boardSize - 1) {

                    int[] up = {ytrend.last() + 1, xcord};


                    if (isPossible(up,possibleMoves)) {
                        tempPossibleMoves.add(up);
                    }
                }
            }
            if (horiz) {
                int ycord = (alreadyGuessed.get(alreadyGuessed.size() - 1))[0];

                TreeSet<Integer> xtrend = new TreeSet<>();
                for (int i = 0; i < consecutiveHits; i++) {
                    xtrend.add((alreadyGuessed.get(alreadyGuessed.size() - 1 - i))[1]);
                }

                if (xtrend.first() - 1 > 0) {

                    int[] left = {ycord, xtrend.first() - 1};

                    if (isPossible(left,possibleMoves)) {
                        tempPossibleMoves.add(left);
                    }
                }
                if (xtrend.last() + 1 <= boardSize - 1) {

                    int[] right = {ycord, xtrend.last() + 1};

                    if (isPossible(right,possibleMoves)) { //bug
                        tempPossibleMoves.add(right);
                    }
                }

            }
        }
        Random rnd = new Random();
        int ran = 0;
        if (tempPossibleMoves.size() > 1) {
            ran = rnd.nextInt(tempPossibleMoves.size());
            possibleMoves.remove(tempPossibleMoves.get(ran));
            alreadyGuessed.add(tempPossibleMoves.get(ran));
            return tempPossibleMoves.get(ran);
        } else if (tempPossibleMoves.size() == 0) {
            if (possibleMoves.size() > 0) {
                ran = rnd.nextInt(possibleMoves.size());
            }

            alreadyGuessed.add(possibleMoves.get(ran));
            int[] guess = possibleMoves.get(ran);
            possibleMoves.remove(ran);
            return guess;

        } else {
            possibleMoves.remove(tempPossibleMoves.get(ran));
            alreadyGuessed.add(tempPossibleMoves.get(ran));
            return tempPossibleMoves.get(ran);
        }

    }
}
