package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.application.ShotFrameManager;
import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;
import at.ac.tuwien.sepm.ws16.qse01.camera.impl.CameraHandlerImpl;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;

/**
 * A class that will control stages and serves as a means of communication between all controllers.
 */
@Controller
public class WindowManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowManager.class);

    private SpringFXMLLoader springFXMLLoader;
    private ApplicationContext applicationContext;
    private ShotFrameManager shotFrameManager;
    private Stage mainStage;
    private Scene mainScene;
    private Scene shootingScene;
    private Scene adminLoginScene;
    private Scene profileScene;
    private Scene settingScene;
    private Scene miniaturScene;
    private Scene pictureFullScene;
    private Scene costumerScene;
    private boolean activeShootingAvailable;
    private int fontSize;
    private FullScreenImageController pictureController;

    @Autowired
    public WindowManager(SpringFXMLLoader springFXMLLoader, ShotFrameManager shotFrameManager){
        this.springFXMLLoader=springFXMLLoader;
        this.shotFrameManager = shotFrameManager;
        activeShootingAvailable=false;
        fontSize =0;
    }

    /**
     * Starts the WindowManager instance, which will open the stages and prepare all necessary scenes.
     * @param mainStage the MainStage, which will be used to show the Scenes that the users directly interact with.
     * @param applicationContext the applicationContext generated in the MainApplication
     * @throws IOException
     */
    public void start(Stage mainStage, ApplicationContext applicationContext) throws IOException{
        this.mainStage=mainStage;
        this.applicationContext=applicationContext;
        double screenWidth=Screen.getPrimary().getBounds().getWidth();
        double screenHeight=Screen.getPrimary().getBounds().getHeight();
        LOGGER.info("PrimaryScreen Bounds: Width: "+screenWidth+" Height: "+screenHeight);

        setFontSize(screenWidth,screenHeight);
        if(fontSize ==0){
            LOGGER.debug("font sice - non fitting screen sice");
            fontSize =16;
        }


        //TODO: replace this part with ShotFrameManager. WindowManager#closeStages must also be changed.
        // Anmerkung: shotframemanager wird in camerapackage erstellt und initializiert und die werden nicht in gleichem
        // Stage angezeigt sondern die haben eigene stages. - deniz

        //Creating ImageFullscreenscene
       SpringFXMLLoader.FXMLWrapper<Object, FullScreenImageController> pictureWrapper = springFXMLLoader.loadAndWrap("/fxml/fullscreenFrame.fxml", FullScreenImageController.class);
        Parent root = (Parent) pictureWrapper.getLoadedObject();
        URL cssf= this.getClass().getResource("/css/fullscreen.css");
        LOGGER.info("CSSF -"+cssf);
        root.setStyle("-fx-font-size:"+fontSize+"px;");
        root.getStylesheets().add(cssf.toExternalForm());
        this.pictureFullScene=new Scene(root ,screenWidth,screenHeight);
        this.pictureController = pictureWrapper.getController();

        //Creating Main-Scene
        SpringFXMLLoader.FXMLWrapper<Object, MainFrameController> mfWrapper = springFXMLLoader.loadAndWrap("/fxml/mainFrame.fxml", MainFrameController.class);
        Parent parentmain = (Parent) mfWrapper.getLoadedObject();
        URL css= this.getClass().getResource("/css/main.css");
        LOGGER.info("CSSM -"+css);
        int sice = (int)(fontSize *3);
        parentmain.setStyle("-fx-font-size:"+sice+"px;");
        parentmain.getStylesheets().add(css.toExternalForm());
        this.mainScene=new Scene(parentmain,screenWidth,screenHeight);

        //Creating Shooting-Scene
        SpringFXMLLoader.FXMLWrapper<Object, ShootingAdminController> shootingWrapper = springFXMLLoader.loadAndWrap("/fxml/shootingFrame.fxml", ShootingAdminController.class);
        Parent parentsf = (Parent) shootingWrapper.getLoadedObject();
        URL csssf= this.getClass().getResource("/css/basicstyle.css");
        LOGGER.info("CSSSF -"+csssf);
        parentsf.setStyle("-fx-font-size:"+fontSize+"px;");
        parentsf.getStylesheets().add(csssf.toExternalForm());
        this.shootingScene=new Scene(parentsf,screenWidth,screenHeight);

        //Creating Profile-Scene
        SpringFXMLLoader.FXMLWrapper<Object, ProfileFrameController> profileWrapper =
                springFXMLLoader.loadAndWrap("/fxml/profileFrame.fxml", ProfileFrameController.class);
        this.profileScene = new Scene((Parent) profileWrapper.getLoadedObject(),screenWidth,screenHeight);

        //Creating Setting-Scene
        SpringFXMLLoader.FXMLWrapper<Object, SettingFrameController> settingWrapper =
                springFXMLLoader.loadAndWrap("/fxml/settingFrame.fxml", SettingFrameController.class);
        Parent parentsett = (Parent) settingWrapper.getLoadedObject();
        URL csssett = this.getClass().getResource("/css/basicstyle.css");
        LOGGER.info("CSSSETT:"+csssett);
        parentsett.setStyle("-fx-font-size:"+ fontSize +"px;");
        parentsett.getStylesheets().add(csssett.toExternalForm());
        this.settingScene = new Scene(parentsett,screenWidth,screenHeight);

        //Creating Login-Scene
        SpringFXMLLoader.FXMLWrapper<Object, LoginFrameController> adminLoginWrapper = springFXMLLoader.loadAndWrap("/fxml/loginFrame.fxml",LoginFrameController.class);
        Parent parentad = (Parent) adminLoginWrapper.getLoadedObject();
        URL cssad= this.getClass().getResource("/css/basicstyle.css");
        LOGGER.info("CSSAD -"+cssad);
        parentad.setStyle("-fx-font-size:"+ fontSize +"px;");
        parentad.getStylesheets().add(cssad.toExternalForm());
        this.adminLoginScene = new Scene(parentad,screenWidth,screenHeight);

        //Creating Miniatur-Scene
        SpringFXMLLoader.FXMLWrapper<Object, MiniaturFrameController> miniWrapper =
                springFXMLLoader.loadAndWrap("/fxml/miniaturFrame.fxml", MiniaturFrameController.class);
        this.miniaturScene=new Scene((Parent) miniWrapper.getLoadedObject(),screenWidth,screenHeight);

        //costumer scene
        SpringFXMLLoader.FXMLWrapper<Object,CostumerFrameController> costumerWrapper =
                springFXMLLoader.loadAndWrap("/fxml/costumerFrame.fxml",CostumerFrameController.class);
        Parent parentcos = (Parent) costumerWrapper.getLoadedObject();
        URL csscos= this.getClass().getResource("/css/costumer.css");
        LOGGER.info("CSSCOS -"+csscos);
        parentcos.setStyle("-fx-font-size:"+ fontSize*2 +"px;");
        parentcos.getStylesheets().add(csscos.toExternalForm());
        this.costumerScene = new Scene(parentcos,screenWidth,screenHeight);

        try {
            miniWrapper.getController().init(mainStage);
        } catch (ServiceException e) {
            LOGGER.error("start - "+e);
        }




        this.mainStage.setTitle("Fotostudio");
        if(activeShootingAvailable){
            this.mainStage.setScene(costumerScene);
            //initShotFrameManager();
        } else {
            this.mainStage.setScene(mainScene);
        }
        this.mainStage.setFullScreen(true);
        this.mainStage.show();
        this.mainStage.setFullScreenExitHint("");
    }

    /**
     * Sets the adminLoginScene as Scene in the mainStage.
     */
    public void showAdminLogin(){
        mainStage.setScene(adminLoginScene);
        mainStage.setFullScreen(true);
    }
    /**
     * Sets the shootingScene as Scene in the mainStage.
     */
    public void showShootingAdministration(){
        mainStage.setScene(shootingScene);
        mainStage.setFullScreen(true);
    }
    /**
     * Sets the mainScene as Scene in the mainStage.
     */
    public void showMainFrame(){
        LOGGER.info("MainFrame set");
        mainStage.setScene(mainScene);
        mainStage.setFullScreen(true);
    }
    /**
     * Sets the profileScene as Scene in the mainStage.
     */
    public void showProfileScene(){
        mainStage.setScene(settingScene);
        mainStage.setFullScreen(true);
    }

    public void showCostumerScene(){
        mainStage.setScene(costumerScene);
        mainStage.setFullScreen(true);
    }
    /**
     * Sets the miniaturScene as Scene in the mainStage.
     */
    public void showMiniatureFrame(){
        LOGGER.info("MiniatureFrame set");
        mainStage.setScene(miniaturScene);
        mainStage.setFullScreen(true);
    }
    /**
     * Closes the mainStage and all shotStages, which leads to the application being closed, too.
     */
    public void closeStages(){
        mainStage.close();
        shotFrameManager.closeFrames();
    }

    public void showFullscreenImage(int imgID){
        mainStage.setScene(pictureFullScene);
        mainStage.setFullScreen(true);
        pictureController.changeImage(imgID);
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

    public void initShotFrameManager(){
        try {
            CameraHandler cameraHandler = this.applicationContext.getBean(CameraHandlerImpl.class);
            cameraHandler.getCameras();
            shotFrameManager.init();
            cameraHandler.getImages();
        } catch (Exception e) {
            LOGGER.info("start - Getting camera - "+e);
        } catch (UnsatisfiedLinkError e){
            LOGGER.error("initshotFrameManager-> Error "+e.getMessage());
        }
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