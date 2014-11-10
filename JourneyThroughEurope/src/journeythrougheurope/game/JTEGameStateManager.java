package journeythrougheurope.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import journeythrougheurope.file.JTEFileLoader;
import journeythrougheurope.ui.JTEUI;

public class JTEGameStateManager {

    // THE GAME WILL ALWAYS BE IN
    // ONE OF THESE STATES
    public enum JTEGameState {

        GAME_NOT_STARTED, GAME_IN_PROGRESS, GAME_OVER,
        LEVEL1,
        LEVEL2,
        LEVEL3,
        LEVEL4,
        LEVEL5,
        LEVEL6,
        LEVEL7
    }

    // STORES THE CURRENT STATE OF THIS GAME
    private JTEGameState currentGameState;

    // WHEN THE STATE OF THE GAME CHANGES IT WILL NEED TO BE
    // REFLECTED IN THE USER INTERFACE, SO THIS CLASS NEEDS
    // A REFERENCE TO THE UI
    private JTEUI ui;

    // THIS IS THE GAME CURRENTLY BEING PLAYED
    private JTEGameData gameInProgress;

    // HOLDS ALL OF THE COMPLETED GAMES. NOTE THAT THE GAME
    // IN PROGRESS IS NOT ADDED UNTIL IT IS COMPLETED
    public ArrayList<JTEGameData> gamesHistory;

    public int[] played = new int[7];
    public int[] wins = new int[7];
    public int[] losses = new int[7];
    public int[] fast = new int[7];

    public void statload() throws IOException {
       /* JTEFileLoader fileloader = new JTEFileLoader(ui);
        String[] re = fileloader.loadStat();
        for (int j = 0; j < 7; j++) {
            String[] temp = re[j].split(" ");
            played[j] = Integer.parseInt(temp[0]);
            wins[j] = Integer.parseInt(temp[1]);
            losses[j] = Integer.parseInt(temp[2]);
            fast[j] = Integer.parseInt(temp[3]);
        }*/
    }

    public JTEGameStateManager(JTEUI initUI) {
        ui = initUI;
        // WE HAVE NOT STARTED A GAME YET
        currentGameState = JTEGameState.GAME_NOT_STARTED;
        // NO GAMES HAVE BEEN PLAYED YET, BUT INITIALIZE
        // THE DATA STRCUTURE FOR PLACING COMPLETED GAMES
        gamesHistory = new ArrayList();
        // THE FIRST GAME HAS NOT BEEN STARTED YET
        gameInProgress = null;
    }

    // ACCESSOR METHODS
    /**
     * Accessor method for getting the game currently being played.
     *
     * @return The game currently being played.
     */
    public JTEGameData getGameInProgress() {
        return gameInProgress;
    }

    /**
     * Accessor method for getting the number of games that have been played.
     *
     * @return The total number of games that have been played during this game
     * session.
     */
    /**
     * Accessor method for getting all the games that have been completed.
     *
     * @return An Iterator that allows one to go through all the games that have
     * been played so far.
     */
    public Iterator<JTEGameData> getGamesHistoryIterator() {
        return gamesHistory.iterator();
    }

    /**
     * Accessor method for testing to see if any games have been started yet.
     *
     * @return true if at least one game has already been started during this
     * session, false otherwise.
     */
    public boolean isGameNotStarted() {
        return currentGameState == JTEGameState.GAME_NOT_STARTED;
    }

    /**
     * Accessor method for testing to see if the current game is over.
     *
     * @return true if the game in progress has completed, false otherwise.
     */
    public boolean isGameOver() {
        return currentGameState == JTEGameState.GAME_OVER;
    }

    /**
     * Accessor method for testing to see if the current game is in progress.
     *
     * @return true if a game is in progress, false otherwise.
     */
    public boolean isGameInProgress() {
        return currentGameState == JTEGameState.GAME_IN_PROGRESS;
    }

    public int[] getGamesPlayed() throws IOException {
       /* statload();
        Iterator<JTEGameData> it = gamesHistory.iterator();
        while (it.hasNext()) {
            // GET THE NEXT GAME IN THE SEQUENCE
            JTEGameData game = it.next();
            int level = game.levelState;
            played[level - 1]++;
        }*/
        int[] dummy = played.clone();
        return dummy;
    }

    /**
     * Counts and returns the number of wins during this game session.
     *
     * @return The number of games in that have been completed that the player
     * won.
     * @throws IOException
     */
    public int[] getWins() throws IOException {
        /*statload();
        // ITERATE THROUGH ALL THE COMPLETED GAMES
        Iterator<JTEGameData> it = gamesHistory.iterator();
        while (it.hasNext()) {
            // GET THE NEXT GAME IN THE SEQUENCE
            JTEGameData game = it.next();
            int level = game.levelState;
            // IF IT ENDED IN A WIN, INC THE COUNTER
            if (game.isWon()) {
                wins[level - 1]++;
            }
        }*/
        return wins.clone();
    }

    /**
     * Counts and returns the number of losses during this game session.
     *
     * @return The number of games in that have been completed that the player
     * lost.
     */
    public int[] getLosses() throws IOException {
        statload();/*
        // ITERATE THROUGH ALL THE COMPLETED GAMES
        Iterator<JTEGameData> it = gamesHistory.iterator();
        while (it.hasNext()) {
            // GET THE NEXT GAME IN THE SEQUENCE
            JTEGameData game = it.next();
            int level = game.levelState;
            // IF IT ENDED IN A LOSS, INC THE COUNTER
            if (game.isLost()) {
                losses[level - 1]++;
            }
        }*/
        return losses.clone();
    }

    /**
     * Finds the completed game that the player won that required the least
     * amount of time.
     *
     * @return The completed game that the player won requiring the least amount
     * of time.
     * @throws IOException
     */
    public int[] getFastestWin() throws IOException {
        statload();/*
        // GO THROUGH ALL THE GAMES THAT HAVE BEEN PLAYED
        Iterator<JTEGameData> it = gamesHistory.iterator();
        while (it.hasNext()) {
            // GET THE NEXT GAME IN THE SEQUENCE
            JTEGameData game = it.next();
            // WE ONLY CONSIDER GAMES THAT WERE WON
            if (game.isWon()) {
                // IF IT'S THE FIRST WIN FOUND, START OUT
                // WITH IT AS THE FASTEST UNTIL WE FIND ONE BETTER
                // OTHERWISE IF IT IS FASTER THEN
                // MAKE IT THE FASTEST     
                if (fast[game.levelState - 1] == 0) {
                    fast[game.levelState - 1] = (int) game.playtime;
                }
                if (game.playtime < fast[game.levelState - 1]) {
                    fast[game.levelState - 1] = (int) game.playtime;
                }
            }
        }*/
        // RETURN THE FASTEST GAME
        return fast.clone();
    }

    /**
     * This method starts a new game, initializing all the necessary data for
     * that new game as well as recording the current game (if it exists) in the
     * games history data structure. It also lets the user interface know about
     * this change of state such that it may reflect this change.
     */
    public void startNewGame(String levelstate) {
        // IS THERE A GAME ALREADY UNDERWAY?
        // YES, SO END THAT GAME AS A LOSS

        // IF THERE IS A GAME IN PROGRESS AND THE PLAYER HASN'T WON, THAT MEANS
        // THE PLAYER IS QUITTING, SO WE NEED TO SAVE THE GAME TO OUR HISTORY
        // DATA STRUCTURE. NOTE THAT IF THE PLAYER WON THE GAME, IT WOULD HAVE
        // ALREADY BEEN SAVED SINCE THERE WOULD BE NO GUARANTEE THE PLAYER WOULD
        // CHOOSE TO PLAY AGAIN
        if (isGameInProgress() && !gameInProgress.isWon()) {
            // QUIT THE GAME, WHICH SETS THE END TIME
            gameInProgress.giveUp();
        }
        // AND NOW MAKE A NEW GAME
        makeNewGame(levelstate);
    }

    /**
     * This method chooses a secret word and uses it to create a new game,
     * effectively starting it.
     *
     * @param levelstate
     */
    public void makeNewGame(String levelstate) {
        // create a game for a level
        int level = Integer.parseInt(levelstate.substring(5, levelstate.length() - 4));
        gameInProgress = new JTEGameData(level);
        gamesHistory.add(gameInProgress);
        // THE GAME IS OFFICIALLY UNDERWAY
        currentGameState = JTEGameState.GAME_IN_PROGRESS;
    }

    public void endGame() {
        // THE GAME IS OFFICIALLY UNDERWAY
        currentGameState = JTEGameState.GAME_OVER;
    }

}
