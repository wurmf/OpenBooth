package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;
import at.ac.tuwien.sepm.ws16.qse01.camera.impl.CameraHandlerImpl;
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

    @Autowired
    public WindowManager(SpringFXMLLoader springFXMLLoader){
        this.springFXMLLoader=springFXMLLoader;
    }

    public void prepare(Stage mainStage, ApplicationContext applicationContext) throws IOException{
        this.mainStage=mainStage;
        this.applicationContext=applicationContext;
        double screenWidth=Screen.getPrimary().getBounds().getWidth();
        double screenHeight=Screen.getPrimary().getBounds().getHeight();
        LOGGER.info("PrimaryScreen Bounds: Width: "+screenWidth+" Height: "+screenHeight);

        SpringFXMLLoader.FXMLWrapper<Object, ShootingAdminController> shootingWrapper =
                springFXMLLoader.loadAndWrap("/fxml/shootingFrame.fxml", ShootingAdminController.class);
        this.shootingScene=new Scene((Parent) shootingWrapper.getLoadedObject(),screenWidth,screenHeight);


        SpringFXMLLoader.FXMLWrapper<Object, MainFrameController> mfWrapper = springFXMLLoader.loadAndWrap("/fxml/mainFrame.fxml", MainFrameController.class);
        this.mainStage.setTitle("Fotostudio");
        this.mainScene=new Scene((Parent) mfWrapper.getLoadedObject(),screenWidth,screenHeight);
        this.mainStage.setScene(mainScene);
        this.mainStage.setFullScreen(true);



        //TODO: 1) creating camera table
        //      2) number of frames to open = number of existing camera in DB

        /* Creating shotFrame */
        int anz = 1;
        int x = 200;
        for(int i=0; i<anz; i++) { // Anzahl der Kameras...
            Stage stage = new Stage();
            stage.setTitle("Shot Frame "+(i+1));
            stage.setScene(new Scene((Parent) springFXMLLoader.load("/fxml/shotFrame.fxml"),400,400));
            //stage.initModality(Modality.WINDOW_MODAL);
            stage.setFullScreen(false);
            stage.initOwner(mainStage);
            stage.setX(x);
            stage.show();

            x += 200;
        }

        //Creating Profile-Scene
        SpringFXMLLoader.FXMLWrapper<Object, ProfileFrameController> profileWrapper =
                springFXMLLoader.loadAndWrap("/fxml/profileFrame.fxml", ProfileFrameController.class);
        this.profileScene = new Scene((Parent) profileWrapper.getLoadedObject(),screenWidth,screenHeight);

        //Creating Login-Scene
        SpringFXMLLoader.FXMLWrapper<Object, LoginFrameController> adminLoginWrapper = springFXMLLoader.loadAndWrap("/fxml/loginFrame.fxml",LoginFrameController.class);
        this.adminLoginScene = new Scene((Parent) adminLoginWrapper.getLoadedObject(),screenWidth,screenHeight);

        //springFXMLLoader.loadAndWrap("/fxml/shotFrame.fxml", ShotFrameController.class).getController().refreshShot();
        try {
            CameraHandler cameraHandler = this.applicationContext.getBean(CameraHandlerImpl.class);
            //CameraHandlerImpl cameraHandler= new CameraHandlerImpl(springFXMLLoader.loadAndWrap("/fxml/shotFrame.fxml", ShotFrameController.class).getController(),new ImageServiceImpl(), new ShootingServiceImpl());
            cameraHandler.getImages();
        } catch (Exception e) {
            LOGGER.info("Getting camera - "+e);
        }


        this.mainStage.show();
    }

    public void showAdminLogin(){
        mainStage.setScene(adminLoginScene);
        mainStage.setFullScreen(true);
    }
    public void showShootingAdministration(){
        mainStage.setScene(shootingScene);
        mainStage.setFullScreen(true);
    }
    public void showMainFrame(){
        mainStage.setScene(mainScene);
        mainStage.setFullScreen(true);
    }
    public void showProfileStage(){
        mainStage.setScene(profileScene);
        mainStage.setFullScreen(true);
    }
    public void closeStage(){
        mainStage.close();
    }
    public Stage getStage(){
        return this.mainStage;
    }
}