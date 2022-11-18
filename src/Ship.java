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
        // TODO: complete this constructor by initializing `health`, `length`, and coord matrices
        this.health = length;
        this.length = length;
        this.bowCoord = bowCoord;
        this.sternCoord = sternCoord;
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
        return (int) 11 * health * length * bowCoord[1] * bowCoord[0] * sternCoord[1] * sternCoord[0];
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


}
