package com.example.GUIClass;

import java.util.Random;

public class Computer extends Player{

    private int[][] consecutiveHits;
    private Boolean wasHit;

    /**
     * Constructor for the Computer class
     */
    //Constructs a computer as "armada." Creates possible moves.
    public Computer(int edgeSize) {
        super("Armada", edgeSize);
        //Jon
    }


    /**
     * Generates a guess based on logic
     * @return
     */
    public int[] generateGuess(){
        //Jon
//        return new int[]{0, 0};
        Random rand = new Random();
        // for testing purposes when the actual guessing strategy has not been implemented
        return new int[] {rand.nextInt(10), rand.nextInt(10)};
    }






}
