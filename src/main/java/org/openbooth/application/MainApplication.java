package org.openbooth.application;

import javafx.application.Application;
import javafx.stage.Stage;
import org.openbooth.gui.GUIManager;
import org.openbooth.imageprocessing.ImageProcessingManager;
import org.openbooth.storage.StorageHandler;
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

        applicationContext.getBean(GUIManager.class).setUpGUI(primaryStage);

        applicationContext.getBean(ImageProcessingManager.class).start();
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Stopping Application");
        ImageProcessingManager imageProcessingManager = applicationContext.getBean(ImageProcessingManager.class);
        imageProcessingManager.stopProcessing();

        LOGGER.debug("Waiting for image processing to stop");
        while(imageProcessingManager.isProcessing()){
            Thread.sleep(500);
        }

        applicationContext.getBean(StorageHandler.class).clearTemporaryStorage();

        if ( applicationContext.isRunning() ) {
            this.applicationContext.close();
        }

        super.stop();
    }
}
