package org.openbooth.application;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;


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
    public void start(Stage primaryStage) {
        LOGGER.info("Starting Application");

        applicationContext = new AnnotationConfigApplicationContext(MainApplication.class);
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Stopping Application");


        if ( applicationContext.isRunning() ) {
            this.applicationContext.close();
        }

        super.stop();
    }
}
