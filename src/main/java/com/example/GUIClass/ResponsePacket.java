package com.example.GUIClass;

public class ResponsePacket {
    private cellStatus cs;
    private int sunkShipLength;

    /**
     * This class's purpose is to be a custom package of returned data from Player.processRequestFromOtherPlayer; can return a cell status and/or the length of a sunken ship
     * @param cs
     * @param sunkShipLength
     */
    public ResponsePacket(cellStatus cs, int sunkShipLength){
        this.cs = cs;
        this.sunkShipLength = sunkShipLength;
    }

    /**
     * alternative constructor; used if no ship has been sunk by the guess
     * @param cs
     */
    public ResponsePacket(cellStatus cs){
        this.cs = cs;
        this.sunkShipLength = -1;
    }

    public cellStatus getCellStatus(){
        return this.cs;
    }

    /**
     * returns the sunkShipLength if available; otherwise, throws an error
     * @return the length of the sunken ship
     * @throws IllegalArgumentException
     */
    public int getSunkShipLength() throws IllegalArgumentException{
        if(this.sunkShipLength == -1){
            throw new IllegalArgumentException("No ship has been sunk, so not sunkShipLength exists!");
        } else{
            return this.sunkShipLength;
        }
    }
}
