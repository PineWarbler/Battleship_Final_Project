import java.util.*;

public class Computer extends Player{

    private int consecutiveMisses;
    private int consecutiveHits;
    private ArrayList<int[]> possibleMoves;
    private int boardSize;
    private ArrayList<int[]> possibleShipLocations;

    private ArrayList<int[]> lastHits;
    private ArrayList<int[]> tempPossibleMoves;

    private boolean missFlag;

    private final Difficulty diff;


    private ArrayList<int[]> searchMatrix;

    private TreeSet<Integer> shipLengths;



    /**
     * Constructor for the Computer class
     */
    //Constructs a computer as "armada." Creates possible moves.
    public Computer(int edgeSize, Difficulty diff) {
        super("Armada", edgeSize);
        this.boardSize = edgeSize;
//        alreadyGuessed = new ArrayList<>();
        consecutiveHits = 0;
        consecutiveMisses=0;
        possibleMoves = new ArrayList<>();
        possibleShipLocations = new ArrayList<>();
        //lastHit = new int[2];
        lastHits=new ArrayList<>();
        tempPossibleMoves = new ArrayList<>();
        missFlag = false;
        this.diff=diff;
        shipLengths=new TreeSet<>(List.of(5,4,3,3,2));


        for(int i = 0;i<edgeSize;i++){
            for(int j = 0; j<edgeSize;j++){
                possibleMoves.add( new int[]{i,j});
                possibleShipLocations.add(new int[]{i,j});
            }
        }
        searchMatrix = new ArrayList<>();
        for(int i = 0;i<edgeSize;i=i+2){
            for(int j = 0; j<edgeSize;j=j+2){
                searchMatrix.add(new int[]{i,j});
            }
        }

        for(int i = 1;i<edgeSize;i=i+2){
            for(int j = 1; j<edgeSize;j=j+2){
                searchMatrix.add(new int[]{i,j});
            }
        }

        for(int[] i : searchMatrix){
            System.out.println(Arrays.toString(i));
        }


        generateShipBoard();
    }


    public boolean wasHit(int[] coord){

        if(this.upperBoard.getCellStatus(coord) == cellStatus.HIT){
            return true;
        }
        else{
            return false;
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


    public void removeCoords(int[][] coords, ArrayList<int[]> moves){
        for(int i = 0; i<coords.length;i++){
            //System.out.println(Arrays.toString(coords[i]));

            for(int j = 0; j<moves.size();j++){
                if(Arrays.equals(coords[i],moves.get(j))){
                    moves.remove(j);
                    j--;
                }
            }
        }
    }

    public void removeCoord(int[] coord, ArrayList<int[]> moves){

            //System.out.println(Arrays.toString(coords[i]));

            for(int j = 0; j<moves.size();j++){
                if(Arrays.equals(coord,moves.get(j))){
                    moves.remove(j);
                    j--;
                }
            }
    }


    public void generateTempPossibleMoves(){
        tempPossibleMoves = new ArrayList<>();
        if (consecutiveHits == 1) {

//

//            int xcord = lastHit[1];
//            int ycord = lastHit[0];


            int xcord = lastHits.get(lastHits.size()-1)[1];
            int ycord =  lastHits.get(lastHits.size()-1)[0];


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
            boolean vert = (lastHits.get(lastHits.size() - 1))[1] == (lastHits.get(lastHits.size() - 2))[1];
            boolean horiz = (lastHits.get(lastHits.size() - 1))[0] == (lastHits.get(lastHits.size() - 2))[0];

            if (vert) {
                int xcord = (lastHits.get(lastHits.size() - 1))[1];

                TreeSet<Integer> ytrend = new TreeSet<>();
                for (int i = 0; i < consecutiveHits; i++) {
                    ytrend.add((lastHits.get(lastHits.size() - 1 - i))[0]);
                }
                if ((ytrend.first() - 1 >= 0) && (wasHit(new int[]{ytrend.first(),xcord}))){

                    int[] down = {ytrend.first() - 1, xcord};

                    if (isPossible(down,possibleMoves)) {
                        tempPossibleMoves.add(down);
                    }
                }
                if ((ytrend.last() + 1 <= boardSize - 1) && (wasHit(new int[]{ytrend.last(),xcord}))){

                    int[] up = {ytrend.last() + 1, xcord};


                    if (isPossible(up,possibleMoves)) {
                        tempPossibleMoves.add(up);
                    }
                }
            }
            if (horiz) {
                int ycord = (lastHits.get(lastHits.size() - 1))[0];

                TreeSet<Integer> xtrend = new TreeSet<>();
                for (int i = 0; i < consecutiveHits; i++) {
                    xtrend.add((lastHits.get(lastHits.size() - 1 - i))[1]);
                }

                if ((xtrend.first() - 1 >= 0) && (wasHit(new int[]{ycord,xtrend.first()}))) {

                    int[] left = {ycord, xtrend.first() - 1};

                    if (isPossible(left,possibleMoves)) {
                        tempPossibleMoves.add(left);
                    }
                }
                if ((xtrend.last() + 1 <= boardSize - 1 ) && (wasHit(new int[]{ycord,xtrend.last()}))) {

                    int[] right = {ycord, xtrend.last() + 1};

                    if (isPossible(right,possibleMoves)) { //bug
                        tempPossibleMoves.add(right);
                    }
                }

            }
        }
    }




    /**
     * Generates a guess based on logic
     * @return
     */
    public int[] generateGuess(){
        //Jon



        //System.out.println("At beginning of guess:" );
//        for(int[] i : tempPossibleMoves){
//            System.out.println("" + i[0] + i[1]);
//        }
        //System.out.println("hits" + consecutiveHits);


        if(diff==Difficulty.EASY){
            Random rnd = new Random();
            int ran = 0;
            if (possibleMoves.size() > 0) {
                ran = rnd.nextInt(possibleMoves.size());
            }

            alreadyGuessed.add(possibleMoves.get(ran));
            int[] guess = possibleMoves.get(ran);
            possibleMoves.remove(ran);
            return guess;

        }
        else if(diff==Difficulty.MEDIUM){
            if ((consecutiveHits > 1 && consecutiveHits < 6) && missFlag && tempPossibleMoves.size() != 0) {
                //possibleMoves.remove(lastEndGuess);

                //System.out.println("used flag");
                removeCoord(tempPossibleMoves.get(0), possibleMoves);
                alreadyGuessed.add(tempPossibleMoves.get(0));
                missFlag = false;
                int[] tempCoord = tempPossibleMoves.get(0);

                generateTempPossibleMoves();
                return tempCoord;


            } else if (consecutiveHits == 1 && missFlag) {
                Random rnd = new Random();
                int ran = rnd.nextInt(tempPossibleMoves.size());

                removeCoord(tempPossibleMoves.get(ran), possibleMoves);
                alreadyGuessed.add(tempPossibleMoves.get(ran));

                int[] tempCoord = tempPossibleMoves.get(ran);
                tempPossibleMoves.remove(ran);


                generateTempPossibleMoves();
                return tempCoord;
            }


            //generateTempPossibleMoves();


            Random rnd = new Random();
            int ran = 0;


            if (tempPossibleMoves.size() > 1) {
                //System.out.println("size over 1");
                ran = rnd.nextInt(tempPossibleMoves.size());
                //possibleMoves.remove(tempPossibleMoves.get(ran));
                removeCoord(tempPossibleMoves.get(ran), possibleMoves);
                int[] tempCoord = tempPossibleMoves.get(ran);

//            if((tempPossibleMoves.size()==2) && (consecutiveHits>1 && consecutiveHits<6)){
//                System.out.println("hits" + consecutiveHits);
//
//                endFlag = true;
//                tempPossibleMoves.remove(ran);
////                lastEndGuess = tempPossibleMoves.get(0);
//                //System.out.println(Arrays.toString(lastEndGuess));
//            }
                tempPossibleMoves.remove(ran);

                alreadyGuessed.add(tempCoord);
                return tempCoord;
            } else if (tempPossibleMoves.size() == 0) {
                //System.out.println("size is 0");
                if (possibleMoves.size() > 0) {
                    ran = rnd.nextInt(possibleMoves.size());
                }

                alreadyGuessed.add(possibleMoves.get(ran));
                int[] guess = possibleMoves.get(ran);
                possibleMoves.remove(ran);
                return guess;

            } else {
                //System.out.println("else");
                //possibleMoves.remove(tempPossibleMoves.get(ran));
                removeCoord(tempPossibleMoves.get(ran), possibleMoves);
                alreadyGuessed.add(tempPossibleMoves.get(ran));
                int[] tempCoord = tempPossibleMoves.get(ran);
                tempPossibleMoves.remove(ran);

                return tempCoord;
            }
        }else{
            if ((consecutiveHits > 1 && consecutiveHits < 6) && missFlag && tempPossibleMoves.size() != 0) {
                //possibleMoves.remove(lastEndGuess);

                //System.out.println("used flag");
                removeCoord(tempPossibleMoves.get(0), possibleMoves);
                removeCoord(tempPossibleMoves.get(0), searchMatrix);
                alreadyGuessed.add(tempPossibleMoves.get(0));
                missFlag = false;
                int[] tempCoord = tempPossibleMoves.get(0);

                generateTempPossibleMoves();
                return tempCoord;


            } else if (consecutiveHits == 1 && missFlag) {
                Random rnd = new Random();
                int ran = rnd.nextInt(tempPossibleMoves.size());

                removeCoord(tempPossibleMoves.get(ran), possibleMoves);
                removeCoord(tempPossibleMoves.get(ran), searchMatrix);
                alreadyGuessed.add(tempPossibleMoves.get(ran));

                int[] tempCoord = tempPossibleMoves.get(ran);
                tempPossibleMoves.remove(ran);


                generateTempPossibleMoves();
                return tempCoord;
            }


            //generateTempPossibleMoves();


            Random rnd = new Random();
            int ran = 0;


            if (tempPossibleMoves.size() > 1) {
                //System.out.println("size over 1");
                ran = rnd.nextInt(tempPossibleMoves.size());
                //possibleMoves.remove(tempPossibleMoves.get(ran));
                removeCoord(tempPossibleMoves.get(ran), possibleMoves);
                removeCoord(tempPossibleMoves.get(ran), searchMatrix);
                int[] tempCoord = tempPossibleMoves.get(ran);
                removeCoord(tempCoord,searchMatrix);

//            if((tempPossibleMoves.size()==2) && (consecutiveHits>1 && consecutiveHits<6)){
//                System.out.println("hits" + consecutiveHits);
//
//                endFlag = true;
//                tempPossibleMoves.remove(ran);
////                lastEndGuess = tempPossibleMoves.get(0);
//                //System.out.println(Arrays.toString(lastEndGuess));
//            }
                tempPossibleMoves.remove(ran);


                alreadyGuessed.add(tempCoord);
                return tempCoord;
            } else if (tempPossibleMoves.size() == 0) {
                //System.out.println("size is 0");
                if (searchMatrix.size() > 0) {
                    ran = rnd.nextInt(searchMatrix.size());
                }

                alreadyGuessed.add(possibleMoves.get(ran));
                int[] guess = searchMatrix.get(ran);
                removeCoord(guess,possibleMoves);
                searchMatrix.remove(ran);
                return guess;

            } else {
                //System.out.println("else");
                //possibleMoves.remove(tempPossibleMoves.get(ran));
                removeCoord(tempPossibleMoves.get(ran), possibleMoves);
                alreadyGuessed.add(tempPossibleMoves.get(ran));
                int[] tempCoord = tempPossibleMoves.get(ran);
                tempPossibleMoves.remove(ran);
                removeCoord(tempCoord,searchMatrix);

                return tempCoord;
            }
        }

    }





    public void generateShipBoard(){
        // TODO: generate a starting board

        ArrayList<Integer> allowedShipLengths = new ArrayList<>(List.of(5,4,3,3,2));


        char[] dirs = {'N','S','E','W'};


        int shipsPlaced = 0;

        while(shipsPlaced<5){
            Random rnd = new Random();
            int ran = rnd.nextInt(possibleShipLocations.size());
            int[] bowLoc = possibleShipLocations.get(ran);

            ran = rnd.nextInt(dirs.length);

            char dir = dirs[ran];


            int[][] coords = this.lowerBoard.getShipBoard().convertFromPivotAndDirection(bowLoc,dir,allowedShipLengths.get(0));

            try {
                if ((this.lowerBoard.getShipBoard().isInBounds(coords)) && (this.lowerBoard.getShipBoard().areCoordsUnoccupied(coords))) {
                    this.lowerBoard.getShipBoard().insertShip(coords, new Ship(allowedShipLengths.get(0)));
                    removeCoords(coords, possibleShipLocations);
                    allowedShipLengths.remove(0);
                    shipsPlaced++;
                }
            }catch(Exception ignored){}
        }
    }

@Override
    public void updateShipList(int size){

        shipLengths.remove(((Integer) size));
        consecutiveHits=0;
    }

@Override
public void processResponseFromOtherPlayer(int[] coord, cellStatus responseStatus){
    if(responseStatus==cellStatus.HIT){
        this.upperBoard.markAsHit(coord);
        //lastHit=coord;
        lastHits.add(coord);

        consecutiveHits++;
        consecutiveMisses=0;
        generateTempPossibleMoves();
        missFlag = false;

        if((consecutiveHits==shipLengths.last())){//store ship sizes and remove or maybe not???
            consecutiveHits=0;
        }
    } else if (responseStatus==cellStatus.MISS){
        this.upperBoard.markAsMissed(coord);
        consecutiveMisses++;
//        if(!missFlag && (tempPossibleMoves.size()==0)){
//            consecutiveHits=0;
//        }
        if((consecutiveHits>1) && consecutiveMisses==2){
            consecutiveHits=0;
        }
        else{
            generateTempPossibleMoves();
        }
        missFlag = true;
    }

    if(tempPossibleMoves.size()==0){
        consecutiveHits=0;
    }
}







}
