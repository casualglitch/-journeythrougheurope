package journeythrougheurope.game;

import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import journeythrougheurope.ui.JTEUI;
import journeythrougheurope.ui.JTEUI.Position;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class JTEGameData {

    // START AND END TIME WILL BE USED TO CALCULATE THE
    // TIME IT TAKES TO PLAY THIS GAME
    GregorianCalendar startTime;
    GregorianCalendar endTime;

    long playtime = 0;
    public int gameState;
    private int playerTurn;
    private int numTurn;
    public Player[] players;
    private int numPlayers;

    // THESE ARE USED FOR FORMATTING THE TIME OF GAME
    final long MILLIS_IN_A_SECOND = 1000;
    final long MILLIS_IN_A_MINUTE = 1000 * 60;
    final long MILLIS_IN_AN_HOUR = 1000 * 60 * 60;
    ArrayList<Position> Terminals;
    /*
     * Construct this object when a game begins.
     */
    public JTEGameData(int numberOfPlayers) {
        startTime = new GregorianCalendar();
        endTime = null;
        gameState = 2;// 1 win 0 lost
        players = new Player[numberOfPlayers];
        playerTurn = 1;
        numTurn = 1;
        numPlayers = numberOfPlayers;
    }

    // ACCESSOR METHODS
    public int getPlayerTurn() {
        return playerTurn;
    }
    
    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public int nextPlayerTurn() {
        if (playerTurn < numPlayers) {
            playerTurn++;
            System.out.println("It is Player " + playerTurn + "'s turn.");
        }
        else {
            playerTurn = 1;
        }
        return playerTurn;
    }

    public int getNumTurn() {
        return numTurn;
    }

    public void setNumTurn(int numTurn) {
        this.numTurn = numTurn;
    }
    
    
    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }
    
    
    /**
     * Gets the total time (in milliseconds) that this game took.
     *
     * @return The time of the game in milliseconds.
     */
    public long getTimeOfGame() {
        // IF THE GAME ISN'T OVER YET, THERE IS NO POINT IN CONTINUING
        if (endTime == null) {
            return -1;
        }

        // THE TIME OF THE GAME IS END-START
        long startTimeInMillis = startTime.getTimeInMillis();
        long endTimeInMillis = endTime.getTimeInMillis();

        // CALC THE DIFF AND RETURN IT
        long diff = (endTimeInMillis - startTimeInMillis) / 1000000;
        return diff;
    }

    /**
     * Called when a player quits a game before ending the game.
     */
    public void giveUp() {
        endTime = new GregorianCalendar();
    }

    /**
     * Builds and returns a textual summary of this game.
     *
     * @return A textual summary of this game, including the secred word, the
     * time of the game, and a listing of all the guesses.
     */
    @Override
    public String toString() {
        // CALCULATE GAME TIME USING HOURS : MINUTES : SECONDS
        long timeInMillis = this.getTimeOfGame();
        long hours = timeInMillis / MILLIS_IN_AN_HOUR;
        timeInMillis -= hours * MILLIS_IN_AN_HOUR;
        long minutes = timeInMillis / MILLIS_IN_A_MINUTE;
        timeInMillis -= minutes * MILLIS_IN_A_MINUTE;
        long seconds = timeInMillis / MILLIS_IN_A_SECOND;

        // THEN ADD THE TIME OF GAME SUMMARIZED IN PARENTHESES
        String minutesText = "" + minutes;
        if (minutes < 10) {
            minutesText = "0" + minutesText;
        }
        String secondsText = "" + seconds;
        if (seconds < 10) {
            secondsText = "0" + secondsText;
        }
        String time = hours + ":" + minutesText + ":" + secondsText;
        // TODO add game data
        return time;
    }

    /**
     * Check if the game was won.
     *
     * @param terminals
     */
    public void CheckGameWon(int[][] grid, ArrayList<Position> terminals) {
        Terminals = terminals;
        boolean flag = false;
        for (Position pos : terminals) {
            if (grid[pos.getX()][pos.getY()] != 2) {
                flag = true;
            }
        }
        if (!flag && gameState != 0) {
            gameState = 1;
        }
    }

    public boolean isWon() {
        return gameState == 1;
    }

    /**
     * Check if the game was lost.
     *
     * @param grid
     */
    public void CheckGameLost(int[][] grid) {
        int i, j;
        boolean flag = false;
        for (i = 0; i < grid.length; i++) {
            for (j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 2) {
                    boolean up = false, down = false, left = false, right = false;
                    if (i - 1 < 0) {
                        left = true;
                    } else if (grid[i - 1][j] == 1 || grid[i - 1][j] == 2) {
                        left = true;
                    }
                    if (j - 1 < 0) {
                        up = true;
                    } else if (grid[i][j - 1] == 1 || grid[i][j - 1] == 2) {
                        up = true;
                    }
                    if (i + 1 >= grid[0].length) {
                        right = true;
                    } else if (grid[i + 1][j] == 1 || grid[i + 1][j] == 2) {
                        right = true;
                    }
                    if (j + 1 > grid.length) {
                        down = true;
                    } else if (grid[i][j + 1] == 1 || grid[i][j + 1] == 2) {
                        down = true;
                    }
                    if (!((left && up) || (up && right) || (right && down) || (down && left))) // gameState = 2;
                    {
                        flag = true;
                    }
                }
            }
        }
        if (!flag && gameState != 1) {
            gameState = 0;
        }
    }

    public boolean isLost() {
        return gameState == 0;
    }

    public boolean isValidMove(KeyEvent t, int[][] grid,
            ArrayList<JTEUI.Position> terminals) {
        int x = 0;
        int y = 0;
        switch (t.getCode().toString()) {
            case "LEFT": {
                x = -1;
                y = 0;
                break;
            }
            case "RIGHT": {
                x = 1;
                y = 0;
                break;
            }
            case "UP": {
                x = 0;
                y = -1;
                break;
            }
            case "DOWN": {
                x = 0;
                y = 1;
                break;
            }
        }
        if (updateGrid(grid, x, y, terminals)) {
            CheckGameWon(grid, terminals);
            if (!isWon()) {
                CheckGameLost(grid);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean updateGrid(int[][] grid, int x, int y, ArrayList<Position> terminals) {
        Terminals = terminals;
        int hposx = 0;
        int hposy = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 4) { // is human
                    hposx = i;
                    hposy = j;
                }
            }
        }
        int checkx = x + hposx;
        int checky = y + hposy;
        if (grid[checkx][checky] == 0 || grid[checkx][checky] == 3) { // is empty
            if (isterminal(hposx, hposy)) {															// target
                grid[hposx][hposy] = 3;
            } else {
                grid[hposx][hposy] = 0;
            }
            grid[checkx][checky] = 4;
            return true;
        } else if (grid[checkx][checky] == 1) { // is wall
            return false;
        } else if (grid[checkx][checky] == 2) {
            if (grid[checkx + x][checky + y] == 0
                    || grid[checkx + x][checky + y] == 3) {
                grid[checkx + x][checky + y] = 2;
                grid[checkx][checky] = 4;
                if (isterminal(hposx, hposy)) {															// target
                    grid[hposx][hposy] = 3;
                } else {
                    grid[hposx][hposy] = 0;
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isterminal(int x, int y) {
        for (int i = 0; i < Terminals.size(); i++) {
            if (Terminals.get(i).getX() == x && Terminals.get(i).getY() == y) {
                return true;
            }
        }
        return false;
    }
}
