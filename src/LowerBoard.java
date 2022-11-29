public class LowerBoard {
    // `LowerBoard` has both a `HitOrMissHistoryBoard` and a `ShipBoard`.
    // Analogous to real-life game setup.

    private static int edgeSize;
    private HitOrMissHistoryBoard histBoard;
    private ShipBoard shipBoard;

    /**
     * Constructor for the lower board
     * @param historyBoard
     * @param shipBoard
     */
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
        cellStatus status = this.shipBoard.processIncomingGuess(coord); // decrements health of ship and returns hit/miss

        if(status == cellStatus.HIT){
            this.histBoard.markAsHit(coord);
            return cellStatus.HIT;
        }
        else if(status==cellStatus.MISS){
            this.histBoard.markAsMissed(coord);
            return cellStatus.MISS;
        }

        return status;
    }

    /**
     * inserts ship into the ShipBoard
     * @param coord coordinates of points which the ship should occupy
     * @param ship the ship to be placed
     */
    public void placeShip(int[] coord, Ship ship){
        //TODO: Sabella
        // UMM...[REYNOLDS] I don't think we need this method as long as Player.lowerBoard is not private
        // then we can call player1.lowerBoard.getShipBoard().processIncomingGuess();
    }

    /**
     * Gets health of the ships
     * @return health
     */
    public int getHealth(){
        return this.shipBoard.getHealth();
    }

    /**
     * Gets the ship board
     * @return shipBoard
     */
    public ShipBoard getShipBoard(){
        return this.shipBoard;
    }

    /**
     * Prints out lower board
     */
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

                    boolean isShipInCell = shipBoard.isShip(new int[]{i-1, j-1});
                    cellStatus cs = this.histBoard.cellStatuses[i-1][j-1];
                    //⧆□▣⧇⬜⬛
                    if((cs == cellStatus.HIT) && isShipInCell){
                        sb.append("\u001B[97m"+"\u001B[1m" + "▣"+ "\033[0m");

                    }
                    // why would there be a hit on a cell without a ship? I don't think we need the else if below...
//                    else if(this.histBoard.cellStatuses[i-1][j-1] == cellStatus.HIT){
//                            sb.append("\u001B[32m" + "O"+ "\033[0m");
//                        }
                    else if(isShipInCell && (cs == cellStatus.NONE)){
                        sb.append("\u001B[47m"+"\u001B[1m" + "□"+ "\033[0m");
                    }

                    else if(cs == cellStatus.MISS){
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



