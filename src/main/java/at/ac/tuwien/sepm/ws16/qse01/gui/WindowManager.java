package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.application.ShotFrameManager;
import at.ac.tuwien.sepm.ws16.qse01.gui.model.LoginRedirectorModel;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;

/**
 * A class that will control stages and serves as a means of communication between all controllers.
 */
@Controller
public class WindowManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowManager.class);

    public static final int END_APPLICATION=0;
    public static final int SHOW_MAINSCENE=1;
    public static final int SHOW_SHOOTINGSCENE=2;
    public static final int SHOW_SETTINGSCENE=3;
    public static final int SHOW_MINIATURESCENE=4;
    public static final int SHOW_CUSTOMERSCENE=5;
    public static final int SHOW_RECOVERYSCENE=6;


    private SpringFXMLLoader springFXMLLoader;
    private ShotFrameManager shotFrameManager;
    private LoginRedirectorModel loginRedirectorModel;
    private Stage mainStage;
    private Scene adminLoginScene;
    private Scene mainScene;
    private Scene shootingScene;
    private Scene settingScene;
    private Scene miniaturScene;
    private Scene pictureFullScene;
    private Scene customerScene;
    private Scene kamerafilterScene;
    private Scene recoveryScene;
    private boolean activeShootingAvailable;
    private int fontSize;
    private FullScreenImageController pictureController;
    private ShootingAdminController shootingAdminController;
    private CameraFilterController cameraFilterController;
    private CustomerFrameController customerFrameController;

    @Autowired
    public WindowManager(SpringFXMLLoader springFXMLLoader, ShotFrameManager shotFrameManager, LoginRedirectorModel loginRedirectorModel){
        this.springFXMLLoader=springFXMLLoader;
        this.shotFrameManager = shotFrameManager;
        this.loginRedirectorModel = loginRedirectorModel;
        activeShootingAvailable=false;
        fontSize =0;
    }

    /**
     * Starts the WindowManager instance, which will open the stages and prepare all necessary scenes.
     * @param mainStage the MainStage, which will be used to show the Scenes that the users directly interact with.
     * @throws IOException
     */
    public void start(Stage mainStage) throws IOException{
        this.mainStage=mainStage;
        double screenWidth=Screen.getPrimary().getBounds().getWidth();
        double screenHeight=Screen.getPrimary().getBounds().getHeight();
        LOGGER.info("PrimaryScreen Bounds: Width: "+screenWidth+" Height: "+screenHeight);

        setFontSize(screenWidth,screenHeight);
        if(fontSize ==0){
            LOGGER.debug("font size - non fitting screen size");
            fontSize =16;
        }


        //Creating ImageFullscreenscene
       SpringFXMLLoader.FXMLWrapper<Object, FullScreenImageController> pictureWrapper = springFXMLLoader.loadAndWrap("/fxml/fullscreenFrame.fxml", FullScreenImageController.class);
        Parent root = (Parent) pictureWrapper.getLoadedObject();
        URL cssf= this.getClass().getResource("/css/fullscreen.css");
        LOGGER.debug("CSSF -"+cssf);
        root.setStyle("-fx-font-size:"+fontSize+"px;");
        root.getStylesheets().add(cssf.toExternalForm());
        this.pictureFullScene=new Scene(root ,screenWidth,screenHeight);
        this.pictureController = pictureWrapper.getController();

        //Creating Main-Scene
        SpringFXMLLoader.FXMLWrapper<Object, MainFrameController> mfWrapper = springFXMLLoader.loadAndWrap("/fxml/mainFrame.fxml", MainFrameController.class);
        Parent parentmain = (Parent) mfWrapper.getLoadedObject();
        URL css= this.getClass().getResource("/css/main.css");
        LOGGER.debug("CSSM -"+css);
        int sice = (fontSize *3);
        parentmain.setStyle("-fx-font-size:"+sice+"px;");
        parentmain.getStylesheets().add(css.toExternalForm());
        this.mainScene=new Scene(parentmain,screenWidth,screenHeight);

        //Creating Shooting-Scene
        SpringFXMLLoader.FXMLWrapper<Object, ShootingAdminController> shootingWrapper = springFXMLLoader.loadAndWrap("/fxml/shootingFrame.fxml", ShootingAdminController.class);
        Parent parentsf = (Parent) shootingWrapper.getLoadedObject();
        URL csssf= this.getClass().getResource("/css/basicstyle.css");
        LOGGER.debug("CSSSF -"+csssf);
        parentsf.setStyle("-fx-font-size:"+fontSize+"px;");
        parentsf.getStylesheets().add(csssf.toExternalForm());
        this.shootingScene=new Scene(parentsf,screenWidth,screenHeight);
        this.shootingAdminController = shootingWrapper.getController();


        //Creating Setting-Scene
        SpringFXMLLoader.FXMLWrapper<Object, SettingFrameController> settingWrapper =
                springFXMLLoader.loadAndWrap("/fxml/settingFrame.fxml", SettingFrameController.class);
        Parent parentsett = (Parent) settingWrapper.getLoadedObject();
        //Anmerkung: Css für Einstellungen wird erst dann hinzugefügt, wenn einstellungen-gui fertig ist. - Deniz
        URL csssett = this.getClass().getResource("/css/profilesetting.css");
        LOGGER.debug("CSSSETT:"+csssett);
        parentsett.setStyle("-fx-font-size:"+ fontSize +"px;");
        parentsett.getStylesheets().add(csssett.toExternalForm());
        this.settingScene = new Scene(parentsett,screenWidth,screenHeight);

        //Creating Login-Scene
        SpringFXMLLoader.FXMLWrapper<Object, LoginFrameController> adminLoginWrapper = springFXMLLoader.loadAndWrap("/fxml/loginFrame.fxml",LoginFrameController.class);
        Parent parentad = (Parent) adminLoginWrapper.getLoadedObject();
        URL cssad= this.getClass().getResource("/css/basicstyle.css");
        LOGGER.debug("CSSAD -"+cssad);
        parentad.setStyle("-fx-font-size:"+ fontSize +"px;");
        parentad.getStylesheets().add(cssad.toExternalForm());
        this.adminLoginScene = new Scene(parentad,screenWidth,screenHeight);

        //Creating Miniatur-Scene
        SpringFXMLLoader.FXMLWrapper<Object, MiniaturFrameController> miniWrapper =
                springFXMLLoader.loadAndWrap("/fxml/miniaturFrame.fxml", MiniaturFrameController.class);
        Parent parentmin = (Parent) miniWrapper.getLoadedObject();
        URL cssmin = this.getClass().getResource("/css/miniatur.css");
        LOGGER.debug("CSSSETT:"+cssmin);
        parentmin.setStyle("-fx-font-size:"+ fontSize +"px;");
        parentmin.getStylesheets().add(cssmin.toExternalForm());
        this.miniaturScene=new Scene(parentmin,screenWidth,screenHeight);
        //costumer scene
        SpringFXMLLoader.FXMLWrapper<Object, CustomerFrameController> costumerWrapper =
                springFXMLLoader.loadAndWrap("/fxml/costumerFrame.fxml", CustomerFrameController.class);
        Parent parentcos = (Parent) costumerWrapper.getLoadedObject();
        URL csscos= this.getClass().getResource("/css/costumer.css");
        LOGGER.debug("CSSCOS -"+csscos);
        parentcos.setStyle("-fx-font-size:"+ fontSize*3 +"px;");
        parentcos.getStylesheets().add(csscos.toExternalForm());
        this.customerScene = new Scene(parentcos,screenWidth,screenHeight);
        customerFrameController = costumerWrapper.getController();

        //creat Camera filter scene
        SpringFXMLLoader.FXMLWrapper<Object, CameraFilterController> kameraFilterFXMLWrapper =
                springFXMLLoader.loadAndWrap("/fxml/kameraFilterFrame.fxml", CameraFilterController.class);
        Parent parentkaf = (Parent) kameraFilterFXMLWrapper.getLoadedObject();
        URL csskaf= this.getClass().getResource("/css/camerafilter.css");
        LOGGER.debug("CSSKAF -"+csskaf);
        parentkaf.setStyle("-fx-font-size:"+ fontSize +"px;");
        parentkaf.getStylesheets().add(csskaf.toExternalForm());
        this.kamerafilterScene = new Scene(parentkaf,screenWidth,screenHeight);
        cameraFilterController = kameraFilterFXMLWrapper.getController();

        //create Recovery scene
        SpringFXMLLoader.FXMLWrapper<Object, RecoveryController> recoveryControllerFXMLWrapper =
                springFXMLLoader.loadAndWrap("/fxml/recoveryFrame.fxml", RecoveryController.class);
        Parent parentrec = (Parent) recoveryControllerFXMLWrapper.getLoadedObject();
        URL cssrec = this.getClass().getResource("/css/recovery.css");
        LOGGER.debug("CSSREC:"+cssrec);
        parentrec.setStyle("-fx-font-size:"+ fontSize*1.5 +"px;");
        parentrec.getStylesheets().add(cssrec.toExternalForm());
        this.recoveryScene=new Scene(parentrec,screenWidth,screenHeight);

        try {
            miniWrapper.getController().init(mainStage);
        } catch (ServiceException e) {
            LOGGER.error("start - ",e);
        }



        this.mainStage.setTitle("Fotostudio");
        if(activeShootingAvailable){
            showAdminLogin(SHOW_CUSTOMERSCENE,END_APPLICATION);
        } else {
            showAdminLogin(SHOW_MAINSCENE, END_APPLICATION);
        }
        this.mainStage.setFullScreen(true);
        this.mainStage.show();
        this.mainStage.setFullScreenExitHint("");

    }

    /**
     * Sets the scene specified by the given integer. For use in combination with static integers provided by WindowManager for identification of the scenes.
     * If a number is given that is not assigned as number for a scene the mainScene will be set.
     * @param sceneToShow the number of the scene that shall be set.
     */
    public void showScene(int sceneToShow){
        if(sceneToShow==SHOW_CUSTOMERSCENE) {
            customerFrameController.refresh();
        }
        if(sceneToShow == SHOW_SHOOTINGSCENE){
            shootingAdminController.inactivemode();
        }
        switch (sceneToShow){
            case END_APPLICATION: closeStages();
                break;
            case SHOW_SHOOTINGSCENE: mainStage.setScene(shootingScene);
                break;
            case SHOW_MINIATURESCENE: mainStage.setScene(miniaturScene);
                break;
            case SHOW_CUSTOMERSCENE: mainStage.setScene(customerScene);
                break;
            case SHOW_SETTINGSCENE: mainStage.setScene(settingScene);
                break;
            case SHOW_RECOVERYSCENE: mainStage.setScene(recoveryScene);
                break;
            default: mainStage.setScene(mainScene);
                break;
        }
        mainStage.setFullScreen(true);
    }


    /**
     * Sets the adminLoginScene as Scene in the mainStage.
     * @param sceneToShow one of the static numbers defined in WindowManager for choosing a frame, representing the window which shall be shown next if the credentials are correct
     * @param callingScene one of the static numbers defined in WindowManager for choosing a frame, representing the window which shall be shown next if the back-button is clicked.
     */
    public void showAdminLogin(int sceneToShow, int callingScene){
        loginRedirectorModel.setScenes(sceneToShow, callingScene);
        if(callingScene ==SHOW_CUSTOMERSCENE){
            shootingAdminController.inactivemode();
        }
        mainStage.setScene(adminLoginScene);
        mainStage.setFullScreen(true);
    }

    /**
     * Sets the FullScreenScene as Scene in the mainStage.
     * @param imgID the ID of the img that will be shown.
     */
    public void showFullscreenImage(int imgID){
        mainStage.setScene(pictureFullScene);
        mainStage.setFullScreen(true);
        pictureController.changeImage(imgID);
    }

    public void showKameraFilterSceen(int idK, int idF, boolean greenscreen){
        mainStage.setScene(kamerafilterScene);
        mainStage.setFullScreen(true);
        cameraFilterController.currentlyChosen(idK,idF,greenscreen);
    }
    /**
     * Closes the mainStage and all shotStages, which leads to the application being closed, too.
     */
    public void closeStages(){
        mainStage.close();
        shotFrameManager.closeFrames();
    }

    /**
     * If an active shooting is available on startup of the application, this method is called to notify the WindowManager of this fact.
     */
    public void notifyActiveShootingAvailable(){
       activeShootingAvailable=true;
    }

    /**
     * Returns the mainStage.
     * @return the mainStage
     */
    public Stage getStage(){
        return this.mainStage;
    }


    /**
     * sets the initial font size depending on the screen Width and high
     * using percentages of screen differences to the initial screen size (1280x800)
     *
     * @param screenWidth the width bound of the current monitor
     * @param screenHeight the height bound of the current monitor
     */
    public void setFontSize(double screenWidth, double screenHeight){
        int initialsize = 16;
        if(screenWidth>=1920.0 && screenHeight>=1080.0){
            fontSize =(int)(initialsize*1.29);
        }else if(screenWidth>=1366.0 && screenHeight>=768.0){
            fontSize =(int)(initialsize*1.07);
        }else if(screenWidth>=1280.0 && screenHeight>=800.0){
            fontSize = initialsize;
        }
    }

}