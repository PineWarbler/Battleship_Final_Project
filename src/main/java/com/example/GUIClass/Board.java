package com.example.GUIClass;

public abstract class Board {

    protected final int edgeSize;

    /**
     * Creates an empty board of size edgeSize by edgeSize
     * @param edgeSize
     */
    public Board(int edgeSize){
        this.edgeSize = edgeSize;
    }

    /**
     * Takes in coord and marks that location as hit
     * @param coord
     */
    // please override these methods in `LowerBoard` because it contains two boards and must modify both boards
    public abstract void markAsHit(int[] coord);
    // don't need to have markAsMissed here because ShipBoard does not keep track of misses; only hits (for which it decrements ship health)

    /**
     * Prints out board
     */
    public abstract void printBoard();
}