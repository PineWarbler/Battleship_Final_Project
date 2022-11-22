package com.example.GUIClass;

public class Ship {
    private int health; // how many segments of ship are not hit
    private int length; // how many segments ship occupies
    int[] bowCoord, sternCoord; // these are necessary to determine the direction in which the ship is pointed (could be one of two directions)

    /**
     *Ship constructor
      * @param length
     * @param bowCoord
     * @param sternCoord
     */
    public Ship(int length,int[] bowCoord, int[] sternCoord){
        this.health = length;
        this.length = length;
        this.bowCoord = bowCoord;
        this.sternCoord = sternCoord;
    }

    public Ship(int length){
        this.length = length;
        this.health = length;
    }

    /**
     * removes from health when ship has been hit
     */
    public void decrementHealth(){
        this.health--;
    }

    /**
     * gets the health of the player
     * @return number of un-hit spaces on the player's ships
     */
    public int getHealth(){
        return this.health;
    }


    /**
     * Gets the hash ID of the ships
     * @return
     */
    public int getHashID(){
        // adding one to all multiplication elements so that no elements are zero.  Otherwise, if one element was zero, hashes of different ships would all return zero.
        // health should not be included in the hash because it changes during the game
        try {
            return 17 + (int) 11 * (length + 1) * (bowCoord[1] + 1) * (bowCoord[0] + 1) * (sternCoord[1] + 1) * (sternCoord[0] + 1);
        } catch (Exception e){
            return 17 + (int) 11 * (length + 1); // this is if bowCoord and sternCoords are null
        }
    }

    /**
     * compares two ship hashIDs.  Useful for identifying a ship that's been hit based on the hash found in the ShipBoard arrayList
     * @param otherHashID hashID of a ship to be compared
     * @return True if otherHashID equals this ship's hashID; False otherwise
     */
    public boolean compareHashID(int otherHashID){

        return otherHashID == this.getHashID();
    }

    /**
     * Gets the bowCoord
     * @return bowCoord
     */
    public int[] getBowCoord() {
        return this.bowCoord;
    }

    /**
     * Gets sternCoord
     * @return sternCoord
     */
    public int[] getSternCoord() {
        return this.sternCoord;
    }

    /**
     * Gets the length of the ships
     * @return length
     */
    public int getLength(){
        return this.length;
    }

    public void setBowCoord(int[] bowCoord) {
        this.bowCoord = bowCoord;
    }

    public void setSternCoord(int[] sternCoord) {
        this.sternCoord = sternCoord;
    }
}
