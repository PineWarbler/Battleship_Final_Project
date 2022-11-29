package com.example.GUIClass;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Player {

    protected String name;
    protected int health;

    protected LowerBoard lowerBoard;
    protected HitOrMissHistoryBoard upperBoard;

    protected ArrayList<int[]> alreadyGuessed; // stores already guessed coordinates

    protected boolean sunkShip;
    protected int sunkShipSize;


    /**
     * Player constructor
     * @param name
     */
    public Player(String name, int edgeSize){

        this.name = name;
        this.health = 0;
        this.lowerBoard = new LowerBoard(new HitOrMissHistoryBoard(edgeSize), new ShipBoard(edgeSize));
        this.upperBoard = new HitOrMissHistoryBoard(edgeSize);
        alreadyGuessed = new ArrayList<>();
        sunkShip = false;
        sunkShipSize = 0;
    }

    /**
     * Boolean returns true if the player has lost
     * @return true when health <= 0
     */
    public boolean hasLost(){
        return this.getHealth()<=0;
    }

    public void recordAnswer(int[] coord){
        this.alreadyGuessed.add(coord); // append to end of list
    }

    /**
     * marks the {@link #upperBoard} with response from other player about whether this player's coord guess hit other player's ship
     * @param coord the coordinate of the guess
     */
    public void processResponseFromOtherPlayer(int[] coord, cellStatus responseStatus){
        if(responseStatus==cellStatus.HIT){
            this.upperBoard.markAsHit(coord);
        } else if (responseStatus==cellStatus.MISS){
            this.upperBoard.markAsMissed(coord);
        }
    }

    /**
     * used when another player asks this player if his/her shot is a hit
     * @param coord is the guessed coordinate
     * @return whether the coord is a hit or a miss <b>(should not return `NONE` enum type!)</b>
     */
    public ResponsePacket processRequestFromOtherPlayer(int[] coord){
        cellStatus status =this.lowerBoard.processIncomingGuess(coord);

        if(this.lowerBoard.getShipBoard().isShip(coord)) {
            if (this.lowerBoard.getShipBoard().identifyShip(coord).isShipSunk()) {
                sunkShip = true;
                sunkShipSize = this.lowerBoard.getShipBoard().identifyShip(coord).getLength();
                return new ResponsePacket(status, sunkShipSize);
            }

        }
        return new ResponsePacket(status);
    } //sout here maybe?

    /**
     * checks to see if this player has already guessed a coordinate
     * @param coord
     * @return
     */
    public boolean hasBeenAlreadyGuessed(int[] coord){
        for(int[] a : alreadyGuessed){
            if(Arrays.equals(coord, a)){
                return true;
            }
        }
        return false;
    }
    // --------------- GETTERS AND SETTERS ----------------
    public LowerBoard getLowerBoard(){
        return this.lowerBoard;
    }

    public HitOrMissHistoryBoard getUpperBoard(){
        return this.upperBoard;
    }

    public void setLowerBoard(LowerBoard lowerBoard) {
        this.lowerBoard = lowerBoard;
    }

    public void setUpperBoard(HitOrMissHistoryBoard upperBoard) {
        this.upperBoard = upperBoard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return this.lowerBoard.getHealth();
    }

    public void updateShipList(int size){} //empty method here, so it can be overridden in computer
}
