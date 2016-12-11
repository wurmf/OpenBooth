package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;
import at.ac.tuwien.sepm.ws16.qse01.camera.impl.CameraHandlerImpl;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.scene.Camera;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * A class that will control stages and serves as a means of communication between all controllers.
 */
@Controller
public class WindowManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowManager.class);

    private SpringFXMLLoader springFXMLLoader;
    private ApplicationContext applicationContext;
    private Stage mainStage;
    private Scene mainScene;
    private Scene shootingScene;
    private Scene adminLoginScene;
    private Scene profileScene;
    private Scene miniaturScene;
    private Scene pictureFullScene;
    private List<Stage> shotStageList;

    @Autowired
    public WindowManager(SpringFXMLLoader springFXMLLoader){
        this.springFXMLLoader=springFXMLLoader;
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



        //TODO: replace this part with ShotFrameManager. WindowManager#closeStages must also be changed.

        /* Creating shotFrame */
        shotStageList=new LinkedList<>();
        int anz = 1;
        int x = 200;
        for(int i=0; i<anz; i++) { // Anzahl der Kameras...
            Stage stage = new Stage();
            stage.setTitle("Shot Frame "+(i+1));
            stage.setScene(new Scene((Parent) springFXMLLoader.load("/fxml/shotFrame.fxml"),400,400));
            stage.setFullScreen(false);
            stage.initOwner(mainStage);
            stage.setX(x);
            shotStageList.add(stage);
            stage.show();

            x += 200;
        }

        //Creating ImageFullscreenscene
        SpringFXMLLoader.FXMLWrapper<Object, FullScreenImageController> pictureWrapper = springFXMLLoader.loadAndWrap("/fxml/pictureFrame.fxml", FullScreenImageController.class);
        Parent root = (Parent) pictureWrapper.getLoadedObject();
        this.pictureFullScene=new Scene(root ,screenWidth,screenHeight);

        //Creating Main-Scene
        SpringFXMLLoader.FXMLWrapper<Object, MainFrameController> mfWrapper = springFXMLLoader.loadAndWrap("/fxml/mainFrame.fxml", MainFrameController.class);
        this.mainScene=new Scene((Parent) mfWrapper.getLoadedObject(),screenWidth,screenHeight);

        //Creating Shooting-Scene
        SpringFXMLLoader.FXMLWrapper<Object, ShootingAdminController> shootingWrapper = springFXMLLoader.loadAndWrap("/fxml/shootingFrame.fxml", ShootingAdminController.class);
        this.shootingScene=new Scene((Parent) shootingWrapper.getLoadedObject(),screenWidth,screenHeight);

        //Creating Profile-Scene
        SpringFXMLLoader.FXMLWrapper<Object, ProfileFrameController> profileWrapper =
                springFXMLLoader.loadAndWrap("/fxml/profileFrame.fxml", ProfileFrameController.class);
        this.profileScene = new Scene((Parent) profileWrapper.getLoadedObject(),screenWidth,screenHeight);

        //Creating Login-Scene
        SpringFXMLLoader.FXMLWrapper<Object, LoginFrameController> adminLoginWrapper = springFXMLLoader.loadAndWrap("/fxml/loginFrame.fxml",LoginFrameController.class);
        this.adminLoginScene = new Scene((Parent) adminLoginWrapper.getLoadedObject(),screenWidth,screenHeight);

        //Creating Miniatur-Scene
        SpringFXMLLoader.FXMLWrapper<Object, MiniaturFrameController> miniWrapper =
                springFXMLLoader.loadAndWrap("/fxml/miniaturFrame.fxml", MiniaturFrameController.class);
        this.miniaturScene=new Scene((Parent) miniWrapper.getLoadedObject(),screenWidth,screenHeight);

        try {
            miniWrapper.getController().init(mainStage);
        } catch (ServiceException e) {
            LOGGER.error("start - "+e);
        }


        //springFXMLLoader.loadAndWrap("/fxml/shotFrame.fxml", ShotFrameController.class).getController().refreshShot();
        try {
            CameraHandler cameraHandler = this.applicationContext.getBean(CameraHandlerImpl.class);
            //CameraHandlerImpl cameraHandler= new CameraHandlerImpl(springFXMLLoader.loadAndWrap("/fxml/shotFrame.fxml", ShotFrameController.class).getController(),new ImageServiceImpl(), new ShootingServiceImpl());
            cameraHandler.getImages();
        } catch (Exception e) {
            LOGGER.info("start - Getting camera - "+e);
        }

        this.mainStage.setTitle("Fotostudio");
        this.mainStage.setScene(mainScene);
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
        mainStage.setScene(mainScene);
        mainStage.setFullScreen(true);
    }
    /**
     * Sets the profileScene as Scene in the mainStage.
     */
    public void showProfileScene(){
        mainStage.setScene(profileScene);
        mainStage.setFullScreen(true);
    }
    /**
     * Sets the miniaturScene as Scene in the mainStage.
     */
    public void showMiniatureFrame(){
        mainStage.setScene(miniaturScene);
        mainStage.setFullScreen(true);
    }
    /**
     * Closes the mainStage and all shotStages, which leads to the application being closed, too.
     */
    public void closeStages(){
        shotStageList.forEach(Stage::close);
        mainStage.close();
    }

    public void showFullscreenImage(){
        mainStage.setScene(pictureFullScene);
        mainStage.setFullScreen(true);
    }

    /**
     * Returns the mainStage.
     * @return the mainStage
     */
    public Stage getStage(){
        return this.mainStage;
    }
}