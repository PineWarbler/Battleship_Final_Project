public class Game {

    /**
     * gets opening ship coordinates from user
     */
    public void SetUp(){
        // I'm not sure to what extent this method will be tied to the GUI class
    }

    /**
     * Gets the user input from console or gui
     * @return int[] of coordinates on grid
     */
    public int[] getUsersGuess(){
        //TODO: JON


        // via GUI?


        //Prompt in console
        //Scan
        //"A6"
        //int[] (Map)

        return null;
    }

    /**
     * loops through turns until there is a winner
     */
    public void gameLoop(){
        // create two player objects
        Player p1=new Player("bob");
        Player p2=new Player("sally");

        Player askingPlayer = p1; // or can choose randomly who goes first
        Player respondingPlayer = p2;
        // get opening ship positions for both
        while(!p1.hasLost() && !p2.hasLost()){

            int[] currPlayersGuess = getUsersGuess();
            cellStatus cs = respondingPlayer.processRequestFromOtherPlayer(currPlayersGuess);
            askingPlayer.processResponseFromOtherPlayer(currPlayersGuess, cs);

            // TODO: toggle turn between askingPlayer and respondingPlayer or vice versa
        }

    }
}
