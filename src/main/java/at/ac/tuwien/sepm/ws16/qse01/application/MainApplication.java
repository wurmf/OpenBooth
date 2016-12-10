package at.ac.tuwien.sepm.ws16.qse01.application;

import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;
import at.ac.tuwien.sepm.ws16.qse01.camera.impl.CameraHandlerImpl;
import at.ac.tuwien.sepm.ws16.qse01.gui.*;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
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
    private Stage miniaturStage;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    private AnnotationConfigApplicationContext applicationContext;
    private WindowManager windowManager;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        LOGGER.info("Starting Application");


        applicationContext = new AnnotationConfigApplicationContext(MainApplication.class);





        windowManager = applicationContext.getBean(WindowManager.class);
        windowManager.prepare(primaryStage, applicationContext);
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Stopping Application");
        if (this.applicationContext != null && applicationContext.isRunning()) {
            this.applicationContext.close();
        }
        super.stop();
    }
    public void showMiniaturStage(){ miniaturStage.show();}
}
