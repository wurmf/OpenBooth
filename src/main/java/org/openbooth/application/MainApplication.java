package org.openbooth.application;

import org.openbooth.util.dbhandler.DBHandler;
import org.openbooth.camera.CameraHandler;
import org.openbooth.service.imageprocessing.ImageProcessingManager;
import org.openbooth.gui.WindowManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

/**
 * The starting point of the sample application.
 */
@ComponentScan("org.openbooth")
public class MainApplication extends Application {


    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    private AnnotationConfigApplicationContext applicationContext;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        LOGGER.info("Starting Application");

        applicationContext = new AnnotationConfigApplicationContext(MainApplication.class);
        WindowManager windowManager = applicationContext.getBean(WindowManager.class);
        windowManager.start(primaryStage);
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Stopping Application");

        ImageProcessingManager imageProcessingManager = applicationContext.getBean(ImageProcessingManager.class);
        imageProcessingManager.stopImageProcessing();

        CameraHandler cameraHandler = applicationContext.getBean(CameraHandler.class);
        cameraHandler.closeCameras();

        DBHandler dbHandler = applicationContext.getBean(DBHandler.class);
        if(dbHandler!=null) {
            dbHandler.closeConnection();
        }

        if (this.applicationContext != null && applicationContext.isRunning()) {
            this.applicationContext.close();
        }

        super.stop();
    }
}
