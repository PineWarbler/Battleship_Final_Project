public class LowerBoard {
    // `LowerBoard` has both a `HitOrMissHistoryBoard` and a `ShipBoard`.
    // Analogous to real-life game setup.

    private static int edgeSize;
    private HitOrMissHistoryBoard histBoard;
    private ShipBoard shipBoard;

    public LowerBoard(HitOrMissHistoryBoard historyBoard, ShipBoard shipBoard){
        // TODO: complete this constructor
        edgeSize = historyBoard.edgeSize;

        this.histBoard = historyBoard;
        this.shipBoard = shipBoard;
    }


    /**
     * Does two things: updates history board <b>and</b> updates ship board of lower board<br>
     * calls {@link #shipBoard}'s <code>processGuess</code> method
     * @param coord
     * @return whether cell is a hit or not
     */
    public cellStatus processIncomingGuess(int[] coord) {
        //TODO: Jon
        // TODO: complete this method according to the Javadoc above
        // should call shipBoard.processIncomingGuess and
        // should call histBoard.markAsHit/markAsMiss depending on output of shipBoard.isShip()


        return null;
    }

    /**
     * inserts ship into the ShipBoard
     * @param coord coordinates of points which the
     * @param ship the ship to be placed
     */
    public void placeShip(int[] coord, Ship ship){
        //TODO: Sabella

        // TODO: should call shipBoard.insertShip
    }

    public int getHealth(){
        return this.shipBoard.getHealth();
    }


    public void printBoard(){

        //weir - not tested yet

        int top = 1;
        int side = 65;
        for(int i = 0; i<edgeSize+1;i++){
            StringBuilder sb = new StringBuilder();

            if(i==0){
                sb.append(" ");
                sb.append(" | ");
                for(int j=1;j<edgeSize+1;j++){
                    if(j<10){
                        sb.append(top);
                        top++;
                        if (j != edgeSize) {
                            sb.append(" | ");
                        }
                    }else{
                        sb.append(top);
                        top++;
                        if (j != edgeSize) {
                            sb.append("| ");
                        }
                    }
                }
            }
            else {


                sb.append((char) side);
                sb.append(" | ");
                side++;


                for (int j = 1; j < edgeSize+1; j++) {

//                    if (this.board[i-1][j-1]) {
//                        sb.append("\u001B[32m" + "T"+ "\033[0m");
//                    } else {
//                        sb.append("\u001B[31m" + "F" + "\033[0m");
//                    }

                    //⧆□▣⧇⬜⬛
                    if((this.histBoard.cellStatuses[i-1][j-1] == cellStatus.HIT)&&(shipBoard.hashArray[i-1][j-1]!=0)){
                        sb.append("\u001B[97m"+"\u001B[1m" + "▣"+ "\033[0m");

                    }

                    else if(this.histBoard.cellStatuses[i-1][j-1] == cellStatus.HIT){
                            sb.append("\u001B[32m" + "O"+ "\033[0m");
                        }
                    else if(shipBoard.hashArray[i-1][j-1]!=0){
                        sb.append("\u001B[47m"+"\u001B[1m" + "⬜"+ "\033[0m");
                    }

                    else if(this.histBoard.cellStatuses[i-1][j-1] == cellStatus.MISS){
                        sb.append("\u001B[31m" + "X" + "\033[0m");
                    }

                    else{
                        sb.append(" ");
                    }
                    if (j != edgeSize ) {
                        sb.append(" | ");
                    }

                }
            }
            System.out.println(sb);
            System.out.print("——————");
            for(int k = 1;k<edgeSize;k++){
                System.out.print("————");
            }
            System.out.println("");
        }
        System.out.println("");
    }

}



