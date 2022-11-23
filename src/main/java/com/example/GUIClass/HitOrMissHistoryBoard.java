package com.example.GUIClass;

public class HitOrMissHistoryBoard extends Board {

    protected cellStatus[][] cellStatuses; // this stores a nxn representation of board cells (to be filled later)

    HitOrMissHistoryBoard(int edgeSize){
        // weir

        super(edgeSize);

        cellStatuses = new cellStatus[super.edgeSize][super.edgeSize];

        for(int i = 0; i<super.edgeSize;i++){
            for(int j = 0;j<super.edgeSize;j++){
                this.cellStatuses[i][j] = cellStatus.NONE;
            }
        }


    }
    @Override
    public void markAsHit(int[] coord){

        //weir - assumes normal indexing system
        this.cellStatuses[coord[0]][coord[1]] = cellStatus.HIT;



    }


    public void markAsMissed(int[] coord){
        //weir - assumes normal indexing system

        this.cellStatuses[coord[0]][coord[1]] = cellStatus.MISS;
    }

    public cellStatus getCellStatus(int[] coord){
        return this.cellStatuses[coord[0]][coord[1]];
    }

    /**
     *
     */
    @Override
    public void printBoard() {
        //This works for edge sizes <100, then the spacing is weird due to character limits

        int top = 1;
        int side = 65;
        for(int i = 0; i<super.edgeSize+1;i++){
            StringBuilder sb = new StringBuilder();

            if(i==0){
                sb.append(" ");
                sb.append(" | ");
                for(int j=1;j<super.edgeSize+1;j++){
                    if(j<10){
                        sb.append(top);
                        top++;
                        if (j != super.edgeSize) {
                            sb.append(" | ");
                        }
                    }else{
                        sb.append(top);
                        top++;
                        if (j != super.edgeSize) {
                            sb.append("| ");
                        }
                    }
                }
            }
            else {


                sb.append((char) side);
                sb.append(" | ");
                side++;


                for (int j = 1; j < super.edgeSize+1; j++) {

//                    if (this.board[i-1][j-1]) {
//                        sb.append("\u001B[32m" + "T"+ "\033[0m");
//                    } else {
//                        sb.append("\u001B[31m" + "F" + "\033[0m");
//                    }


                    if(this.cellStatuses[i-1][j-1] == cellStatus.HIT){
                        sb.append("\u001B[97m"+"\u001B[1m" + "O"+ "\033[0m");
                    }
                    else if(this.cellStatuses[i-1][j-1] == cellStatus.MISS){
                        sb.append("\u001B[31m"+"\u001B[1m" + "X" + "\033[0m");
                    }
                    else{
                        sb.append(" ");
                    }
                    if (j != super.edgeSize ) {
                        sb.append(" | ");
                    }

                }
            }
            System.out.println(sb);
            System.out.print("——————");
            for(int k = 1;k<super.edgeSize;k++){
                System.out.print("————");
            }
            System.out.println("");
        }
        System.out.println("");
    }
}
