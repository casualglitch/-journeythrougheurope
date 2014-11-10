package journeythrougheurope.ui;

import application.Main;
import application.Main.JTEPropertyType;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JEditorPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;

import journeythrougheurope.file.JTEFileLoader;
import journeythrougheurope.game.JTEGameData;
import journeythrougheurope.game.JTEGameStateManager;
import application.Main.JTEPropertyType;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.AnimationTimer;
import properties_manager.PropertiesManager;
import java.lang.Thread;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javax.sound.sampled.AudioSystem;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicTableUI.KeyHandler;

public class JTEUI extends Pane {
    /**
     * The JTEUIState represents the four screen states that are possible for
     * the JourneyThroughEurope game application. Depending on which state is in
     * current use, different controls will be visible.
     */
    public enum JTEUIState {

        SPLASH_SCREEN_STATE,
        PLAY_GAME_STATE,
        VIEW_HISTORY_STATE,
        VIEW_ABOUT_STATE,
        VIEW_FLIGHT_STATE,
    }

    // mainStage
    private Stage primaryStage;

    // mainPane
    private BorderPane mainPane;

    // mainPane weight && height
    private int paneWidth;
    private int paneHeigth;
    
    // SplashScreen
    private ImageView splashScreenImageView;
    private StackPane splashScreenPane;
    private Label splashScreenImageLabel;
    private ImageView splashGameImageView;
    private Label splashGameImageLabel;
    private VBox menuPane;
    private ArrayList<Button> menuButtons;

    // EastToolBar
    private VBox eastToolbar;
    private Button historyButton;
    private Button flightButton;
    private Button aboutButton;
    private Button saveButton;

    // GamePane
    private Label JTELabel;
    private Button newGameButton;
    private HBox letterButtonsPane;
    private HashMap<Character, Button> letterButtons;
    private BorderPane gamePanel = new BorderPane();
    private GraphicsContext gc;
    Stack<int[][]> undo = new Stack<int[][]>();

    // Image path
    private String ImgPath = "file:images/";
    
    // images
    final Image JTEImage = new Image("file:images/JTE.png");
   // final Image splashImage = new Image("file:images/splash.gif");
    final Image gameImage = new Image("file:images/Game.jpg");

    //HistoryPane
    private ScrollPane historyScrollPane;
    private JEditorPane historyPane;

    //AboutPane
    private BorderPane aboutPanel;
    private JScrollPane aboutScrollPane;
    private JEditorPane aboutPane;
    private Button homeButton;
    private Pane workspace;

    // Padding
    private Insets marginlessInsets;


    // THIS CLASS WILL HANDLE ALL ACTION EVENTS FOR THIS PROGRAM
    private JTEEventHandler eventHandler;
    private JTEGameStateManager gsm;
    private JTEFileLoader fileLoader;

    
    // ANIMATION
    double AnimaLength = 0.5;
    
    public class Position {

        private int x;
        private int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public JTEUI() throws IOException {
        gsm = new JTEGameStateManager(this);
        eventHandler = new JTEEventHandler(this);
        //errorHandler = new JTEErrorHandler(primaryStage);
        //docManager = new JTEDocumentManager(this);
        initMainPane();
        initSplashScreen();
    }

    public void SetStage(Stage stage) {
        primaryStage = stage;
    }

    public BorderPane GetMainPane() {
        return this.mainPane;
    }

    public JTEGameStateManager getGSM() {
        return gsm;
    }

    //public JTEDocumentManager getDocManager() {
    //    return docManager;
    //}
    public JEditorPane getAboutPane() {
        return aboutPane;
    }

    public void initMainPane() {
        marginlessInsets = new Insets(5, 5, 5, 5);
        mainPane = new BorderPane();

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        paneWidth = Integer.parseInt(props
                .getProperty(JTEPropertyType.WINDOW_WIDTH));
        paneHeigth = Integer.parseInt(props
                .getProperty(JTEPropertyType.WINDOW_HEIGHT));
        mainPane.resize(paneWidth, paneHeigth);
        mainPane.setPadding(marginlessInsets);
    }

    public void initSplashScreen() throws IOException {
        // INIT THE SPLASH SCREEN CONTROLS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String splashScreenImagePath = props
                .getProperty(JTEPropertyType.SPLASH_SCREEN_IMAGE_NAME);
        String splashGameImagePath = props
                .getProperty(JTEPropertyType.SPLASH_GAME_IMAGE_NAME);
        props.addProperty(JTEPropertyType.INSETS, "5");
        String str = props.getProperty(JTEPropertyType.INSETS);
        
        splashScreenPane = new StackPane();
        //background splash screen image
        Image splashScreenImage = loadImage(splashScreenImagePath);
        splashScreenImageView = new ImageView(splashScreenImage);

        splashScreenImageLabel = new Label();
        splashScreenImageLabel.setGraphic(splashScreenImageView);
        splashScreenPane.getChildren().add(splashScreenImageLabel);
        //overlay splash game image
        Image splashGameImage = loadImage(splashGameImagePath);
        splashGameImageView = new ImageView(splashGameImage);
        
        splashGameImageLabel = new Label();
        splashGameImageLabel.setGraphic(splashGameImageView);
        // move the label position to fix the pane
        splashGameImageLabel.setLayoutY(+150);
        splashScreenPane.getChildren().add(splashGameImageLabel);

        // GET THE LIST OF MENU OPTIONS
        ArrayList<String> menu = props
                .getPropertyOptionsList(JTEPropertyType.MENU_OPTIONS);
        ArrayList<String> menuImages = props
                .getPropertyOptionsList(JTEPropertyType.MENU_IMAGE_NAMES);
        
        menuPane = new VBox();
        menuPane.setAlignment(Pos.CENTER);
        menuButtons = new ArrayList<Button>();
        for (int i = 0; i < menu.size(); i++) {

            // GET THE LIST OF LANGUAGE OPTIONS
            String option = menu.get(i);
            String optionImageName = menuImages.get(i);
            Image optionImage = loadImage(optionImageName);
            ImageView langImageView = new ImageView(optionImage);

            // AND BUILD THE BUTTON
            Button optionButton = new Button();
            optionButton.setGraphic(langImageView);

            // CONNECT THE BUTTON TO THE EVENT HANDLER
            optionButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    try {
                        // TODO Auto-generated method stub
                        // System.out.println(lang);
                        eventHandler.respondToSelectMenu(option);
                    } catch (IOException ex) {
                        Logger.getLogger(JTEUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            menuButtons.add(optionButton);
            menuPane.getChildren().add(optionButton);
        }
        splashScreenPane.getChildren().add(menuPane);
        
        mainPane.setCenter(splashScreenPane);
    }

    /**
     * This method initializes the language-specific game controls, which
     * includes the three primary game screens.
     */
    public void initJTEUI() throws IOException {
        // FIRST REMOVE THE SPLASH SCREEN
        //mainPane.getChildren().clear();
        mainPane.setCenter(null);

        // GET THE UPDATED TITLE
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String title = props.getProperty(JTEPropertyType.GAME_TITLE_TEXT);
        primaryStage.setTitle(title);

        // THEN ADD ALL THE STUFF WE MIGHT NOW USE
        initNorthToolbar();

        // OUR WORKSPACE WILL STORE EITHER THE GAME, STATS,
        // OR HELP UI AT ANY ONE TIME
        initWorkspace();
        //initGameScreen();
        //initStatsPane();
        //initHelpPane();

        // WE'LL START OUT WITH THE GAME SCREEN
        changeWorkspace(JTEUIState.PLAY_GAME_STATE);

    }

    /**
     * This function initializes all the controls that go in the north toolbar.
     */
    private void initNorthToolbar() {
        /*// MAKE THE NORTH TOOLBAR, WHICH WILL HAVE FOUR BUTTONS
        northToolbar = new HBox();
        northToolbar.setStyle("-fx-background-color:lightgray");
        northToolbar.setAlignment(Pos.CENTER);
        northToolbar.setPadding(marginlessInsets);
        //northToolbar.setSpacing(10.0);

        // MAKE AND INIT THE BACK BUTTON
        backButton = initToolbarButton(northToolbar,
                JTEPropertyType.BACK_IMG_NAME);
        //setTooltip(backButton, JTEPropertyType.GAME_TOOLTIP);
        backButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler
                        .respondToSwitchScreenRequest(JTEUIState.SPLASH_SCREEN_STATE);
            }
        });

        // MAKE AND INIT THE UNDO BUTTON
        undoButton = initToolbarButton(northToolbar,
                JTEPropertyType.UNDO_IMG_NAME);
        //setTooltip(undoButton, JTEPropertyType.HELP_TOOLTIP);
        undoButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler
                        .respondToSwitchScreenRequest(JTEUIState.VIEW_ABOUT_STATE);
            }

        });

        // MAKE AND INIT THE STATS BUTTON
        statsButton = initToolbarButton(northToolbar,
                JTEPropertyType.STATS_IMG_NAME);
        //setTooltip(statsButton, JTEPropertyType.STATS_TOOLTIP);

        statsButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler
                        .respondToSwitchScreenRequest(JTEUIState.VIEW_HISTORY_STATE);
            }

        });

        // MAKE AND INIT THE TIME BUTTON
        timeButton = initToolbarButton(northToolbar,
                JTEPropertyType.TIME_IMG_NAME);
        //setTooltip(timeButton, JTEPropertyType.EXIT_TOOLTIP);
        timeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                //eventHandler.respondToExitRequest(primaryStage);
            }

        });

        // AND NOW PUT THE NORTH TOOLBAR IN THE FRAME
        mainPane.setTop(northToolbar);
        //mainPane.getChildren().add(northToolbar);*/
    }

    /**
     * This method helps to initialize buttons for a simple toolbar.
     *
     * @param toolbar The toolbar for which to add the button.
     *
     * @param prop The property for the button we are building. This will
     * dictate which image to use for the button.
     *
     * @return A constructed button initialized and added to the toolbar.
     */
    private Button initToolbarButton(HBox toolbar, JTEPropertyType prop) {
        // GET THE NAME OF THE IMAGE, WE DO THIS BECAUSE THE
        // IMAGES WILL BE NAMED DIFFERENT THINGS FOR DIFFERENT LANGUAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imageName = props.getProperty(prop);

        // LOAD THE IMAGE
        Image image = loadImage(imageName);
        ImageView imageIcon = new ImageView(image);

        // MAKE THE BUTTON
        Button button = new Button();
        button.setGraphic(imageIcon);
        button.setPadding(marginlessInsets);

        // PUT IT IN THE TOOLBAR
        toolbar.getChildren().add(button);

        // AND SEND BACK THE BUTTON
        return button;
    }

    /**
     * The workspace is a panel that will show different screens depending on
     * the user's requests.
     */
    private void initWorkspace() {
        // THE WORKSPACE WILL GO IN THE CENTER OF THE WINDOW, UNDER THE NORTH
        // TOOLBAR
        workspace = new Pane();
        mainPane.setCenter(workspace);
        //mainPane.getChildren().add(workspace);
        //System.out.println("in the initWorkspace");
    }

    public Image loadImage(String imageName) {
        Image img = new Image(ImgPath + imageName);
        return img;
    }

    /**
     * This function selects the UI screen to display based on the uiScreen
     * argument. Note that we have 3 such screens: game, stats, and help.
     *
     * @param uiScreen The screen to be switched to.
     */
    public void changeWorkspace(JTEUIState uiScreen) throws IOException {
        switch (uiScreen) {
            case SPLASH_SCREEN_STATE:
                mainPane.getChildren().clear();
                while (undo.isEmpty() == false) {
                    undo.pop();
                }
                initSplashScreen();
                break;
            case VIEW_ABOUT_STATE:
                //TODO
                break;
            case PLAY_GAME_STATE:
                initGameScreen();
                mainPane.setCenter(gamePanel); // or renderer??
                break;
            case VIEW_HISTORY_STATE:
                //TODO
                //mainPane.setCenter(statsScrollPane);
                break;
            default:
        }
    }

    public void initGameScreen() {
        
    }

}
