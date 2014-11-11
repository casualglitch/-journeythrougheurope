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
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.AnimationTimer;
import properties_manager.PropertiesManager;
import java.lang.Thread;
import java.net.MalformedURLException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class JTEUI extends Pane {

    /**
     * The JTEUIState represents the four screen states that are possible for
     * the JourneyThroughEurope game application. Depending on which state is in
     * current use, different controls will be visible.
     */
    public enum JTEUIState {

        SPLASH_SCREEN_STATE,
        SETUP_SCREEN_STATE,
        PLAY_GAME_STATE,
        VIEW_HISTORY_STATE,
        VIEW_ABOUT_STATE,
        VIEW_FLIGHT_STATE,
    }

    //UIState
    private JTEUIState currentUIState; 
    private JTEUIState prevState;
    
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

    // SetupPane
    private GridPane playerPane  = new GridPane();
    private BorderPane setupPane = new BorderPane();
    private BorderPane pane1 = new BorderPane();
    private BorderPane pane2 = new BorderPane();
    private BorderPane pane3 = new BorderPane();
    private BorderPane pane4 = new BorderPane();
    private BorderPane pane5 = new BorderPane();
    private BorderPane pane6 = new BorderPane();
    
    // EastToolBar
    private VBox eastToolbar;
    private Button historyButton;
    private Button flightButton;
    private Button aboutButton;
    private Button saveButton;

    // GameScreen
    private Label JTELabel; //?
    private BorderPane gamePane = new BorderPane();
    private VBox cardPane;
    private ImageView topLeftMapImageView;
    private Label topLeftMapImageLabel;
    private AnchorPane mapPane;
    private GraphicsContext gc;

    // Image path
    private String ImgPath = "file:images/";

    //HistoryPane
    private StackPane historyPanel;
    private ScrollPane historyScrollPane;
    private JEditorPane historyPane;

    //AboutPane
    private StackPane aboutPanel;
    private ScrollPane aboutScrollPane;
    private JEditorPane aboutPane;
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
        currentUIState = JTEUIState.SPLASH_SCREEN_STATE;
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
    
    public JTEUIState getCurrentUIState() {
        return currentUIState;
    }
    
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
        for (int i = 0; i < menu.size(); i++) {

            // GET THE LIST OF MENU OPTIONS
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
                    if (option.equals("Quit")) {
                        eventHandler.respondToExitRequest(primaryStage);
                    } else if (option.equals("Start")) {
                        eventHandler.respondToSetupRequest();
                    } else if (option.equals("load")) {
                        eventHandler.respondToLoadGameRequest();
                    } else if (option.equals("About")) {
                        eventHandler.respondToSwitchScreenRequest(JTEUIState.VIEW_ABOUT_STATE);
                    }
                }
            });
            menuPane.getChildren().add(optionButton);
        }
        splashScreenPane.getChildren().add(menuPane);
        splashScreenPane.setMargin(menuPane, new Insets(350, 0, 0, 0));
        System.out.println("in the initSplashScreenPane");
        mainPane.setCenter(splashScreenPane);
        currentUIState = JTEUIState.SPLASH_SCREEN_STATE;
    }
    
    public void initSetupScreen() {
        FlowPane selectPane = new FlowPane();
        selectPane.setHgap(5);
        setupPane.setStyle("-fx-background-color:burlywood");
        String[] nums = new String[] {"2", "3", "4", "5", "6"};
        ComboBox<String> cb = new ComboBox<>();
        cb.setValue("6");
        ObservableList<String> items = FXCollections.observableArrayList(nums);
        cb.getItems().addAll(items); 
        cb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eventHandler.changePlayersRequest(cb.getValue());
            }
            });
        Button go = new Button("GO!");
        go.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eventHandler.respondToStartGameRequest();
            }

        });
        selectPane.getChildren().add(new Label("  Number of Players:"));
        selectPane.getChildren().add(cb);
        selectPane.getChildren().add(go);
        
        setupPane.setTop(selectPane);
        //setup each player pane starting with player 1
        final Image blackflag = new Image("file:images/flag_black.png");
        Label flag1 = new Label();
        flag1.setGraphic(new ImageView(blackflag));
        pane1.setLeft(flag1);
        pane1.setMargin(flag1, new Insets(100,0,0,0));
        //radiobutton group
        ToggleGroup tg1 = new ToggleGroup();
        RadioButton playerRB1 = new RadioButton("Player");
        RadioButton compRB1 = new RadioButton("Computer");
        playerRB1.setToggleGroup(tg1);
        compRB1.setToggleGroup(tg1);
        playerRB1.setSelected(true);
        VBox rb1 = new VBox();
        rb1.getChildren().add(playerRB1);
        rb1.getChildren().add(compRB1);
        pane1.setCenter(rb1);
        pane1.setMargin(rb1, new Insets(110,0,0,0));
        //name/textfield Pane
        VBox namePane1 = new VBox();
        Label name1 = new Label("Name:");
        namePane1.getChildren().add(name1);
        TextField text1 = new TextField("Player1");
        namePane1.getChildren().add(text1);
        pane1.setRight(namePane1);
        pane1.setMargin(namePane1, new Insets(110,10,0,0));
        //2
        final Image yellowflag = new Image("file:images/flag_yellow.png");
        Label flag2 = new Label();
        flag2.setGraphic(new ImageView(yellowflag));
        pane2.setLeft(flag2);
        pane2.setMargin(flag2, new Insets(100,0,0,0));
        //radiobutton group
        ToggleGroup tg2 = new ToggleGroup();
        RadioButton playerRB2 = new RadioButton("Player");
        RadioButton compRB2 = new RadioButton("Computer");
        playerRB2.setToggleGroup(tg2);
        compRB2.setToggleGroup(tg2);
        compRB2.setSelected(true);
        VBox rb2 = new VBox();
        rb2.getChildren().add(playerRB2);
        rb2.getChildren().add(compRB2);
        pane2.setCenter(rb2);
        pane2.setMargin(rb2, new Insets(110,0,0,0));
        //name/textfield Pane
        VBox namePane2 = new VBox();
        Label name2 = new Label("Name:");
        namePane2.getChildren().add(name2);
        TextField text2 = new TextField("Player2");
        namePane2.getChildren().add(text2);
        pane2.setRight(namePane2);
        pane2.setMargin(namePane2, new Insets(110,10,0,0));
        //3
        final Image blueflag = new Image("file:images/flag_blue.png");
        Label flag3 = new Label();
        flag3.setGraphic(new ImageView(blueflag));
        pane3.setLeft(flag3);
        pane3.setMargin(flag3, new Insets(100,0,0,0));
        //radiobutton group
        ToggleGroup tg3 = new ToggleGroup();
        RadioButton playerRB3 = new RadioButton("Player");
        RadioButton compRB3 = new RadioButton("Computer");
        playerRB3.setToggleGroup(tg3);
        compRB3.setToggleGroup(tg3);
        compRB3.setSelected(true);
        VBox rb3 = new VBox();
        rb3.getChildren().add(playerRB3);
        rb3.getChildren().add(compRB3);
        pane3.setCenter(rb3);
        pane3.setMargin(rb3, new Insets(110,0,0,0));
        //name/textfield Pane
        VBox namePane3 = new VBox();
        Label name3 = new Label("Name:");
        namePane3.getChildren().add(name3);
        TextField text3 = new TextField("Player3");
        namePane3.getChildren().add(text3);
        pane3.setRight(namePane3);
        pane3.setMargin(namePane3, new Insets(110,10,0,0));
        //4
        final Image redflag = new Image("file:images/flag_red.png");
        Label flag4 = new Label();
        flag4.setGraphic(new ImageView(redflag));
        pane4.setLeft(flag4);
        pane4.setMargin(flag4, new Insets(100,0,0,0));
        //radiobutton group
        ToggleGroup tg4 = new ToggleGroup();
        RadioButton playerRB4 = new RadioButton("Player");
        RadioButton compRB4 = new RadioButton("Computer");
        playerRB4.setToggleGroup(tg4);
        compRB4.setToggleGroup(tg4);
        compRB4.setSelected(true);
        VBox rb4 = new VBox();
        rb4.getChildren().add(playerRB4);
        rb4.getChildren().add(compRB4);
        pane4.setCenter(rb4);
        pane4.setMargin(rb4, new Insets(110,0,0,0));
        //name/textfield Pane
        VBox namePane4 = new VBox();
        Label name4 = new Label("Name:");
        namePane4.getChildren().add(name4);
        TextField text4 = new TextField("Player4");
        namePane4.getChildren().add(text4);
        pane4.setRight(namePane4);
        pane4.setMargin(namePane4, new Insets(110,10,0,0));
        //5
        final Image greenflag = new Image("file:images/flag_green.png");
        Label flag5 = new Label();
        flag5.setGraphic(new ImageView(greenflag));
        pane5.setLeft(flag5);
        pane5.setMargin(flag5, new Insets(100,0,0,0));
        //radiobutton group
        ToggleGroup tg5 = new ToggleGroup();
        RadioButton playerRB5 = new RadioButton("Player");
        RadioButton compRB5 = new RadioButton("Computer");
        playerRB5.setToggleGroup(tg5);
        compRB5.setToggleGroup(tg5);
        compRB5.setSelected(true);
        VBox rb5 = new VBox();
        rb5.getChildren().add(playerRB5);
        rb5.getChildren().add(compRB5);
        pane5.setCenter(rb5);
        pane5.setMargin(rb5, new Insets(110,0,0,0));
        //name/textfield Pane
        VBox namePane5 = new VBox();
        Label name5 = new Label("Name:");
        namePane5.getChildren().add(name5);
        TextField text5 = new TextField("Player5");
        namePane5.getChildren().add(text5);
        pane5.setRight(namePane5);
        pane5.setMargin(namePane5, new Insets(110,10,0,0));
        //6
        final Image whiteflag = new Image("file:images/flag_white.png");
        Label flag6 = new Label();
        flag6.setGraphic(new ImageView(whiteflag));
        pane6.setLeft(flag6);
        pane6.setMargin(flag6, new Insets(100,0,0,0));
        //radiobutton group
        ToggleGroup tg6 = new ToggleGroup();
        RadioButton playerRB6 = new RadioButton("Player");
        RadioButton compRB6 = new RadioButton("Computer");
        playerRB6.setToggleGroup(tg6);
        compRB6.setToggleGroup(tg6);
        compRB6.setSelected(true);
        VBox rb6 = new VBox();
        rb6.getChildren().add(playerRB6);
        rb6.getChildren().add(compRB6);
        pane6.setCenter(rb6);
        pane6.setMargin(rb6, new Insets(110,0,0,0));
        //name/textfield Pane
        VBox namePane6 = new VBox();
        Label name6 = new Label("Name:");
        namePane6.getChildren().add(name6);
        TextField text6 = new TextField("Player6");
        namePane6.getChildren().add(text6);
        pane6.setRight(namePane6);
        pane6.setMargin(namePane6, new Insets(110,10,0,0));
        //show 6 player pane
        for (int c=0; c<3; c++) {
            playerPane.getColumnConstraints().add(new ColumnConstraints(310));
        }
        for (int r=0; r<2; r++) {
                playerPane.getRowConstraints().add(new RowConstraints(260));
        }
        playerPane.setAlignment(Pos.CENTER);
        playerPane.add(pane1, 0, 0);
        playerPane.add(pane2, 1, 0);
        playerPane.add(pane3, 2, 0);
        playerPane.add(pane4, 0, 1);
        playerPane.add(pane5, 1, 1);
        playerPane.add(pane6, 2, 1);
        playerPane.setGridLinesVisible(true);
        setupPane.setCenter(playerPane);
        mainPane.setCenter(setupPane);
    }

    public void setupPlayerGrid(String num) {
        //change number of player panes shown
        switch(num) {
            case "2":
                pane1.setVisible(true);
                pane2.setVisible(true);
                pane3.setVisible(false);
                pane4.setVisible(false);
                pane5.setVisible(false);
                pane6.setVisible(false);
                break;
            case "3":
                pane1.setVisible(true);
                pane2.setVisible(true);
                pane3.setVisible(true);
                pane4.setVisible(false);
                pane5.setVisible(false);
                pane6.setVisible(false);
                break;
            case "4":
                pane1.setVisible(true);
                pane2.setVisible(true);
                pane3.setVisible(true);
                pane4.setVisible(true);
                pane5.setVisible(false);
                pane6.setVisible(false);
                break;
            case "5":
                pane1.setVisible(true);
                pane2.setVisible(true);
                pane3.setVisible(true);
                pane4.setVisible(true);
                pane5.setVisible(true);
                pane6.setVisible(false);
                break;
            case "6":
                pane1.setVisible(true);
                pane2.setVisible(true);
                pane3.setVisible(true);
                pane4.setVisible(true);
                pane5.setVisible(true);
                pane6.setVisible(true);
                break;
            default:  
        }
    }

    /**
     * This method initializes the language-specific game controls, which
     * includes the three primary game screens.
     */
    public void initJTEUI(){
        // FIRST REMOVE THE SPLASH SCREEN
        //mainPane.getChildren().clear();
        mainPane.setCenter(null);

        // GET THE UPDATED TITLE
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String title = props.getProperty(JTEPropertyType.GAME_TITLE_TEXT);
        primaryStage.setTitle(title);


        // OUR WORKSPACE WILL STORE EITHER THE GAME, STATS,
        // OR HELP UI AT ANY ONE TIME
        initWorkspace();
        initSetupScreen();
        initGameScreen();
        initHistoryPane();
        initAboutPane();

        // WE'LL START OUT WITH THE GAME SCREEN
        //changeWorkspace(JTEUIState.PLAY_GAME_STATE);
    }

    /**
     * This function initializes all the controls that go in the north toolbar.
     */
    private void initEastToolbar() {
        // MAKE THE NORTH TOOLBAR, WHICH WILL HAVE FOUR BUTTONS
         
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
    private Button initToolbarButton(VBox toolbar, JTEPropertyType prop) {
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
        workspace = new Pane();
        mainPane.setCenter(workspace);
    }

    public Image loadImage(String imageName) {
        Image img = new Image(ImgPath + imageName);
        return img;
    }
    
    public void scaleImage() {
        
    }

    /**
     * This function selects the UI screen to display based on the uiScreen
     * argument. Note that we have 3 such screens: game, stats, and help.
     *
     * @param uiScreen The screen to be switched to.
     */
    public void changeWorkspace(JTEUIState uiScreen) {
        switch (uiScreen) {
            case SPLASH_SCREEN_STATE:
                mainPane.setCenter(splashScreenPane);
                break;
            case SETUP_SCREEN_STATE:
                mainPane.setCenter(setupPane);
                break;
            case PLAY_GAME_STATE:
                mainPane.setCenter(gamePane);
                break;
            case VIEW_ABOUT_STATE:
                initAboutPane();
                mainPane.setCenter(aboutPanel);
                break;
            case VIEW_HISTORY_STATE:
                mainPane.setCenter(historyPanel);
                break;
            case VIEW_FLIGHT_STATE:
                break;
            default:
        }
    }
    public void initGameScreen() {
        currentUIState = JTEUIState.PLAY_GAME_STATE;
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String topLeftMapImagePath = props.getProperty(JTEPropertyType.TOP_LEFT_MAP_IMG_NAME);
        gamePane.setStyle("-fx-background-color:white");
        
        //cardPane
        cardPane = new VBox();
        Label player1 = new Label();
        cardPane.getChildren().add(player1);
        gamePane.setLeft(cardPane);
        
        //mapPane
        mapPane = new AnchorPane();
        Image topLeftMapImage = loadImage(topLeftMapImagePath);
        topLeftMapImageView = new ImageView(topLeftMapImage);
        topLeftMapImageView.setFitWidth(topLeftMapImage.getWidth()/4);
        topLeftMapImageView.setPreserveRatio(true);
        
        topLeftMapImageLabel = new Label();
        topLeftMapImageLabel.setGraphic(topLeftMapImageView);
        //mapPane.getChildren().add(topLeftMapImageLabel);
        mapPane.getChildren().add(topLeftMapImageLabel);
        AnchorPane.setTopAnchor(topLeftMapImageLabel, 0.0);
        //gamePane.setCenter(topLeftMapImageLabel);
        //gamePane.setMargin(topLeftMapImageLabel, new Insets(0,0,0,230));
        gamePane.setCenter(mapPane);
        gamePane.setMargin(mapPane, new Insets(0,0,0,230));     
        
        //eastToolbar
        eastToolbar = new VBox();
        eastToolbar.setStyle("-fx-background-color:white");
        eastToolbar.setAlignment(Pos.CENTER);
        eastToolbar.setPadding(new Insets(0,30,0,00));
        eastToolbar.setSpacing(10.0);

        // MAKE AND INIT THE FLIGHT BUTTON
        flightButton = initToolbarButton(eastToolbar, JTEPropertyType.FLIGHT_IMG_NAME);
        flightButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eventHandler.respondToSwitchScreenRequest(JTEUIState.VIEW_FLIGHT_STATE);
            }
        });
        
        // MAKE AND INIT THE GAME HISTORY BUTTON
        historyButton = initToolbarButton(eastToolbar, JTEPropertyType.HISTORY_IMG_NAME);
        historyButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                eventHandler.respondToSwitchScreenRequest(JTEUIState.VIEW_HISTORY_STATE);
            }
        });

        // MAKE AND INIT THE ABOUT BUTTON
        aboutButton = initToolbarButton(eastToolbar, JTEPropertyType.ABOUTJTE_IMG_NAME);
        aboutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eventHandler.respondToSwitchScreenRequest(JTEUIState.VIEW_ABOUT_STATE);
            }
        });
        
        // MAKE AND INIT THE SAVE BUTTON
        saveButton = initToolbarButton(eastToolbar, JTEPropertyType.SAVE_IMG_NAME);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eventHandler.respondToSaveGameRequest();
            }
        });
        // AND NOW PUT THE EAST TOOLBAR IN THE FRAME
        gamePane.setRight(eastToolbar);
        mainPane.setCenter(gamePane);
    }

    /**
     * This method initializes the history pane controls for use.
     */
    private void initHistoryPane() {
        prevState = getCurrentUIState();
        historyPane = new JEditorPane();
        historyPane.setEditable(false);
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(historyPane);
        historyScrollPane = new ScrollPane();
        historyScrollPane.setContent(swingNode);
        historyPanel = new StackPane();
        // NOW LOAD THE HELP HTML
        historyPane.setContentType("text/html");
        
        historyScrollPane.setFitToHeight(true);
        historyScrollPane.setFitToWidth(true);
        
        //TODO: 
        loadPage(historyPane, JTEPropertyType.HISTORY_FILE_NAME);
        
        //MAKE CLOSE BUTTON
        Button closeButton = new Button("CLOSE");
        closeButton.setPadding(marginlessInsets);
        
        VBox historyToolbar = new VBox();
        historyToolbar.setAlignment(Pos.CENTER);
        historyToolbar.getChildren().add(closeButton);
        historyToolbar.setStyle("-fx-background-color:white");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToSwitchScreenRequest(prevState);
            }

        });
        historyPanel.getChildren().add(historyScrollPane);
        historyPanel.getChildren().add(historyToolbar);
        historyPanel.setMargin(historyToolbar, new Insets(250, 0, 0, 0));
        mainPane.setCenter(historyPanel);
    }

    /**
     * This method initializes the about pane and all of its controls.
     */
    public void initAboutPane() {
        prevState = getCurrentUIState();
        aboutPane = new JEditorPane();
        aboutPane.setEditable(false);
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(aboutPane);
        aboutScrollPane = new ScrollPane();
        aboutScrollPane.setContent(swingNode);
        aboutPanel = new StackPane();
        // NOW LOAD THE HELP HTML
        aboutPane.setContentType("text/html");
        
        aboutScrollPane.setFitToHeight(true);
        aboutScrollPane.setFitToWidth(true);

        //aboutPanel.setCenter(aboutScrollPane);
        loadPage(aboutPane, JTEPropertyType.ABOUT_FILE_NAME);
        //mainPane.setCenter(aboutScrollPane);
        
        //MAKE BACK BUTTON
        Button back = new Button("BACK");
        back.setPadding(marginlessInsets);
        
        VBox aboutToolbar = new VBox();
        aboutToolbar.setAlignment(Pos.CENTER);
        aboutToolbar.getChildren().add(back);
        aboutToolbar.setStyle("-fx-background-color:white");
        back.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                eventHandler.respondToSwitchScreenRequest(prevState);
            }

        });
        aboutPanel.getChildren().add(aboutScrollPane);
        aboutPanel.getChildren().add(aboutToolbar);
        aboutPanel.setMargin(aboutToolbar, new Insets(250, 0, 0, 0));
        mainPane.setCenter(aboutPanel);
        //workspace.getChildren().add(aboutPanel);
    }

    /**
     * This method loads the HTML page that corresponds to the fileProperty
     * argument and puts it into the jep argument for display.
     *
     * @param jep The pane that will display the loaded HTML.
     *
     * @param fileProperty The file property, whose name can then be retrieved
     * from the property manager.
     */
    public void loadPage(JEditorPane jep, JTEPropertyType fileProperty) {
        // GET THE FILE NAME
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String fileName = props.getProperty(fileProperty);
        try {
            // LOAD THE HTML INTO THE EDITOR PANE
            String fileHTML = JTEFileLoader.loadTextFile(fileName);
            jep.setText(fileHTML);
            //System.out.println(jep.getText());
        } catch (IOException ioe) {
            //errorHandler.processError(JTEPropertyType.INVALID_URL_ERROR_TEXT);
        }
    }
}
