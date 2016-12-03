package at.ac.tuwien.sepm.ws16.qse01.application;

import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;
import at.ac.tuwien.sepm.ws16.qse01.camera.impl.CameraHandlerImpl;
import at.ac.tuwien.sepm.ws16.qse01.gui.*;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ImageServiceImpl;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ShootingServiceImpl;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * The starting point of the sample application.
 *
 */
@Configuration
@ComponentScan("at.ac.tuwien.sepm")
public class MainApplication extends Application {
    private Stage adminLoginStage;
    private Stage profileStage;
    private Stage shootingStage;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    private AnnotationConfigApplicationContext applicationContext;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        LOGGER.info("Starting Application");


        applicationContext = new AnnotationConfigApplicationContext(MainApplication.class);
        SpringFXMLLoader springFXMLLoader = applicationContext.getBean(SpringFXMLLoader.class);

        SpringFXMLLoader.FXMLWrapper<Object, ShootingAdminController> shootingWrapper =
                springFXMLLoader.loadAndWrap("/fxml/shoutingFrame.fxml", ShootingAdminController.class);
        shootingStage = new Stage();
        shootingStage.setScene(new Scene((Parent) shootingWrapper.getLoadedObject()));
        shootingStage.setFullScreen(true);

        /*        SpringFXMLLoader.FXMLWrapper<Object, MainFrameController> mfWrapper =
                springFXMLLoader.loadAndWrap("/fxml/mainFrame.fxml", MainFrameController.class);
        mfWrapper.getController().setStageAndMain(primaryStage, this);
        primaryStage.setTitle("Fotostudio");
        primaryStage.setScene(new Scene((Parent) mfWrapper.getLoadedObject()));
        primaryStage.show();

*/

        //TODO: 1) creating camera table
        //      2) number of frames to open = number of existing camera in DB

        /* Creating shotFrame */
        int anz = 1;
        int x = 200;
        for(int i=0; i<anz; i++) { // Anzahl der Kameras...
            Stage stage = new Stage();
            stage.setTitle("Shot Frame "+(i+1));
            stage.setScene(new Scene((Parent) springFXMLLoader.load("/fxml/shotFrame.fxml"),400,400));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setFullScreen(false);
            stage.initOwner(primaryStage);
            stage.setX(x);
            stage.show();

            x += 200;
        }

        //Creating Profile-Frame
        SpringFXMLLoader.FXMLWrapper<Object, ProfileFrameController> profileWrapper =
                springFXMLLoader.loadAndWrap("/fxml/profileFrame.fxml", ProfileFrameController.class);
        profileStage = new Stage();
        profileStage.setTitle("Profile Verwaltung");
        profileStage.setScene(new Scene((Parent) profileWrapper.getLoadedObject(),400,400));


        //Creating Login-Frame
        SpringFXMLLoader.FXMLWrapper<Object, LoginFrameController> adminLoginWrapper = springFXMLLoader.loadAndWrap("/fxml/loginFrame.fxml",LoginFrameController.class);
        this.adminLoginStage = new Stage();
        this.adminLoginStage.setScene(new Scene((Parent) adminLoginWrapper.getLoadedObject()));
        this.adminLoginStage.setTitle("Administratoren-Login");
        this.adminLoginStage.initModality(Modality.APPLICATION_MODAL);
        this.adminLoginStage.initOwner(primaryStage);
        adminLoginWrapper.getController().setStageAndMain(adminLoginStage, this);

        //springFXMLLoader.loadAndWrap("/fxml/shotFrame.fxml", ShotFrameController.class).getController().refreshShot();
        try {
            CameraHandlerImpl cameraHandler= new CameraHandlerImpl(springFXMLLoader.loadAndWrap("/fxml/shotFrame.fxml", ShotFrameController.class).getController(),new ImageServiceImpl(), new ShootingServiceImpl());
            cameraHandler.getImages();
        } catch (Exception e) {
            LOGGER.info("Getting camera - "+e);
        }

    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Stopping Application");
        if (this.applicationContext != null && applicationContext.isRunning()) {
            this.applicationContext.close();
        }
        super.stop();
    }

    public void showAdminLogin(){
        adminLoginStage.show();
    }
    public void showShootingAdministration(){
        shootingStage.show();
    }

    //TODO: call this from shootingStage
    public void showProfileStage(){
        profileStage.show();
    }
}