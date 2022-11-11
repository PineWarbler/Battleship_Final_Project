public class Ship {
    private int health; // how many segments of ship are not hit
    private int length; // how many segments ship occupies
    int[] bowCoord, sternCoord; // these are necessary to determine the direction in which the ship is pointed (could be one of two directions)

    public Ship(int length){
        // TODO: complete this constructor by initializing `health`, `length`, and coord matrices
        this.health = health;
        this.length = length;
        bowCoord = new int [length];
        sternCoord = new int[length];

    }

    public void decrementHealth(){
        this.health--;
    }

    public int getHealth(){
        return this.health;
    }


    public int getHashID(){
        // TODO: complete this method. (Hint: should be similar to hashCode())
    }

    /**
     * compares two ship hashIDs.  Useful for identifying a ship that's been hit based on the hash found in the ShipBoard arrayList
     * @param otherHashID hashID of a ship to be compared
     * @return True if otherHashID equals this ship's hashID; False otherwise
     */
    public boolean compareHashID(int otherHashID){
        // TODO: complete this method according to Javadoc above
    }

    public int[] getBowCoord() {
        return this.bowCoord;
    }

    public int[] getSternCoord() {
        return this.sternCoord;
    }


}
