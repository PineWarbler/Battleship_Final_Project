import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.Arrays;

public class Computer extends Player{

    private ArrayList<int[]> lastMoves;
    private int consecutiveHits;
    private ArrayList<int[]> possibleMoves;
    private int boardSize;


    public static void main(String[] args) {
        Computer p = new Computer(10);

        p.consecutiveHits = 2;
        p.lastMoves.add( new int[]{3,5});
        p.lastMoves.add(new int[]{4,5});

        int[] guess = p.generateGuess();
        System.out.println("" + guess[0]+","+guess[1]);


        int[] guess1 = {0,2};
        int[] guess2 = {0,2};
        System.out.println(Arrays.equals(guess1,guess2));
    }



    /**
     * Constructor for the Computer class
     */
    //Constructs a computer as "armada." Creates possible moves.
    public Computer(int edgeSize) {
        super("Armada", edgeSize);
        this.boardSize = edgeSize;
        lastMoves = new ArrayList<>();
        consecutiveHits = 0;
        possibleMoves = new ArrayList<>();

        for(int i = 0;i<edgeSize;i++){
            for(int j = 0; j<edgeSize;j++){
                possibleMoves.add( new int[]{i,j});
            }
        }


    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (!(o instanceof MyClassName)) {
//            return false;
//        }
//        MyClassName m = (MyClassName) o;
//        if (this.variable != m.variable) {
//            return false;
//        }
//        return true;
//    }



    /**
     * Generates a guess based on logic
     * @return
     */
    public int[] generateGuess(){
        //Jon

        if(consecutiveHits==0){
            Random rnd = new Random();
            int ran = 0;
            if(possibleMoves.size()>0){
                ran = rnd.nextInt(possibleMoves.size());
            }

            lastMoves.add(possibleMoves.get(ran));

            int[] guess = possibleMoves.get(ran);
            possibleMoves.remove(ran);

            return guess;
        }
        else {
            ArrayList<int[]> tempPossibleMoves = new ArrayList<>();


            if (consecutiveHits == 1) {

                int[] lastHit = lastMoves.get(lastMoves.size() - 1);

                int xcord = lastHit[1];
                int ycord = lastHit[0];


                if (xcord > 0) {
                    int[] left = {ycord, xcord - 1};
                    if (possibleMoves.contains(left)) {
                        tempPossibleMoves.add(left);
                    }
                }

                if (xcord + 1 <= this.boardSize - 1) {
                    int[] right = {ycord, xcord + 1};
                    if (possibleMoves.contains(right)) {
                        tempPossibleMoves.add(right);
                    }
                }

                if (ycord > 0) {
                    int[] down = {ycord - 1, xcord};
                    if (possibleMoves.contains(down)) {
                        tempPossibleMoves.add(down);
                    }
                }

                if (ycord + 1 <= boardSize - 1) {
                    //System.out.println("" + ((char)(((int) ycord)+1)) + xcord);
                    int[] up = {ycord + 1, xcord};
                    if (possibleMoves.contains(up)) {
                        tempPossibleMoves.add(up);
                    }
                }
            } else if(consecutiveHits>1){
                boolean vert = (lastMoves.get(lastMoves.size() - 1))[1] == (lastMoves.get(lastMoves.size() - 2))[1];
                boolean horiz = (lastMoves.get(lastMoves.size() - 1))[0] == (lastMoves.get(lastMoves.size() - 2))[0];

                if (vert) {
                    //System.out.println("vert");
                    int xcord = (lastMoves.get(lastMoves.size() - 1))[1];

                    TreeSet<Integer> ytrend = new TreeSet<>();
                    for (int i = 0; i < consecutiveHits; i++) {
                        ytrend.add((lastMoves.get(lastMoves.size() - 1 - i))[0]);
                    }

                    for(int i : ytrend){
                        System.out.println("" + i);

                    }

                    if (ytrend.first() - 1 > 0) {

                        int[] down = {ytrend.first() - 1, xcord};

                        if (possibleMoves.contains(down)) {
                            tempPossibleMoves.add(down);
                        }
                    }
                    if (ytrend.last() + 1 <= boardSize - 1) {

                        int[] up = {ytrend.last() + 1, xcord};


                        if (possibleMoves.contains(up)) {
                            tempPossibleMoves.add(up);
                        }
                    }
                }


                if (horiz) {
                    //System.out.println("horiz");
                    int ycord = (lastMoves.get(lastMoves.size() - 1))[0];

                    TreeSet<Integer> xtrend = new TreeSet<>();
                    for (int i = 0; i < consecutiveHits; i++) {
                        xtrend.add((lastMoves.get(lastMoves.size() - 1 - i))[1]);
                    }

                    if (xtrend.first() - 1 > 0) {

                        int[] left = {ycord, xtrend.first() - 1};

                        if (possibleMoves.contains(left)) {
                            tempPossibleMoves.add(left);
                        }
                    }
                    if (xtrend.last() + 1 <= boardSize - 1) {

                        int[] right = {ycord, xtrend.last() + 1};

                        if (possibleMoves.contains(right)) { //bug
                            tempPossibleMoves.add(right);
                        }
                    }

                }
            }


            for(int[] i : tempPossibleMoves){
                System.out.println("" + i[0]+"," + i[1]);

            }
            Random rnd = new Random();
            int ran = 0;
            if (tempPossibleMoves.size() > 1) {
                ran = rnd.nextInt(tempPossibleMoves.size());
                possibleMoves.remove(tempPossibleMoves.get(ran));
                lastMoves.add(tempPossibleMoves.get(ran));
                return tempPossibleMoves.get(ran);
            } else if (tempPossibleMoves.size() == 0) {
                if (possibleMoves.size() > 0) {
                    ran = rnd.nextInt(possibleMoves.size());
                }

                lastMoves.add(possibleMoves.get(ran));
                int[] guess = possibleMoves.get(ran);
                possibleMoves.remove(ran);
                return guess;

            } else {
                possibleMoves.remove(tempPossibleMoves.get(ran));
                //System.out.println(possibleMoves);
                lastMoves.add(tempPossibleMoves.get(ran));
                return tempPossibleMoves.get(ran);
            }
        }

    }
}
