package journeythrougheurope.ui;

import java.io.DataInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;

import application.Main.JTEPropertyType;
import java.util.logging.Level;
import java.util.logging.Logger;

import properties_manager.PropertiesManager;

import journeythrougheurope.file.JTEFileLoader;
import journeythrougheurope.game.JTEGameStateManager;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class JTEEventHandler {

    private JTEUI ui;
    private Stack<int[][]> MoveRecorder;
    private Timeline timeline;
    public long timefinal;

    public Timeline getTimeline() {
        return timeline;
    }

    /**
     * Constructor that simply saves the ui for later.
     *
     * @param initUI
     */
    public JTEEventHandler(JTEUI initUI) {
        ui = initUI;
        MoveRecorder = new Stack<int[][]>();
    }

    /**
     * This method responds to when the user wishes to switch between the Game,
     * Stats, and Help screens.
     *
     * @param uiState The ui state, or screen, that the user wishes to switch
     * to.
     * @throws IOException
     */
    public void respondToSwitchScreenRequest(JTEUI.JTEUIState uiState) {
            ui.changeWorkspace(uiState);
    }

    /**
     * This method responds to when the user presses the new game method.
     */
    public void respondToNewGameRequest() {
        JTEGameStateManager gsm = ui.getGSM();
    }

    public void respondToSetupRequest() {
        ui.initJTEUI();
        ui.changeWorkspace(JTEUI.JTEUIState.SETUP_SCREEN_STATE);
    }
    
    public void respondToStartGameRequest() {
        ui.changeWorkspace(JTEUI.JTEUIState.PLAY_GAME_STATE);
        JTEGameStateManager gsm = ui.getGSM();
    }

    /**
     * This method responds to when the user requests to exit the application.
     *
     * @param window The window that the user has requested to close.
     */
    public void respondToExitRequest(Stage primaryStage) {
        System.exit(0);
    }

    public void respondToKeyEvent(KeyEvent t) {
        
    }

    void respondToWin(Stage primaryStage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void respondToLoadGameRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void respondToGoHomeRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void changePlayersRequest(String value) {
        ui.setupPlayerGrid(value);
    }

    void respondToSaveGameRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
