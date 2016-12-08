package at.ac.tuwien.sepm.ws16.qse01.application;

import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;
import at.ac.tuwien.sepm.ws16.qse01.camera.impl.CameraHandlerImpl;
import at.ac.tuwien.sepm.ws16.qse01.gui.*;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ImageServiceImpl;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ShootingServiceImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Stage mainStage;
    private Stage miniaturStage;

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
                springFXMLLoader.loadAndWrap("/fxml/shootingFrame.fxml", ShootingAdminController.class);
        shootingStage = new Stage();
        shootingStage.setScene(new Scene((Parent) shootingWrapper.getLoadedObject()));
        shootingStage.setFullScreen(true);
        shootingWrapper.getController().setStageAndMain(shootingStage, this);

        SpringFXMLLoader.FXMLWrapper<Object, MainFrameController> mfWrapper = springFXMLLoader.loadAndWrap("/fxml/mainFrame.fxml", MainFrameController.class);
        mfWrapper.getController().setStageAndMain(primaryStage, this);
        primaryStage.setTitle("Fotostudio");
        primaryStage.setScene(new Scene((Parent) mfWrapper.getLoadedObject()));
        primaryStage.setFullScreen(true);
        mainStage=primaryStage;
        primaryStage.show();




        //Creating Miniatur-Frame
        SpringFXMLLoader.FXMLWrapper<Object, MiniaturFrameController> miniWrapper =
                springFXMLLoader.loadAndWrap("/fxml/miniaturFrame.fxml", MiniaturFrameController.class);
        miniaturStage = new Stage();
        miniaturStage.setTitle("Fotoübersicht");
        miniaturStage.setScene(new Scene((Parent) miniWrapper.getLoadedObject(),800,500));
        try {
            miniWrapper.getController().init(shootingStage,miniaturStage);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        miniaturStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        miniaturStage.setHeight(Screen.getPrimary().getVisualBounds()
                .getHeight());

        //  miniaturStage.setFullScreen(true);
       miniaturStage.show();

        //Creating Profile-Frame
        SpringFXMLLoader.FXMLWrapper<Object, ProfileFrameController> profileWrapper =
                springFXMLLoader.loadAndWrap("/fxml/profileFrame.fxml", ProfileFrameController.class);
        profileStage = new Stage();
        profileStage.setTitle("Profilverwaltung");
        profileStage.initOwner(shootingStage);
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
            CameraHandler cameraHandler = applicationContext.getBean(CameraHandlerImpl.class);
            //CameraHandlerImpl cameraHandler= new CameraHandlerImpl(springFXMLLoader.loadAndWrap("/fxml/shotFrame.fxml", ShotFrameController.class).getController(),new ImageServiceImpl(), new ShootingServiceImpl());
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
    public void showMainFrame(){mainStage.show();}

    //TODO: call this from shootingStage
    public void showProfileStage(){
        profileStage.show();
    }
    public void showMiniaturStage(){ miniaturStage.show();}
}
