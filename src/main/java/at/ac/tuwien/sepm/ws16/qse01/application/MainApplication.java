package at.ac.tuwien.sepm.ws16.qse01.application;

import at.ac.tuwien.sepm.util.printer.ImagePrinter;
import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.awt.print.PrinterException;
import java.io.IOException;

/**
 * The starting point of the sample application.
 *
 */
@Configuration
@ComponentScan("at.ac.tuwien.sepm")
public class MainApplication extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    private AnnotationConfigApplicationContext applicationContext;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        LOGGER.info("Starting Application");
        //TODO: revert to original state
        Image img=new Image();
        img.setImagepath("/home/martin/Bilder/PTBWE2_M_C0696_20161029.jpg");
        try {
            (new ImagePrinter()).print(img);
        } catch (PrinterException e) {
            LOGGER.error("could not print - "+e);
        }
        //applicationContext = new AnnotationConfigApplicationContext(MainApplication.class);
        //WindowManager windowManager = applicationContext.getBean(WindowManager.class);
        //windowManager.start(primaryStage, applicationContext);
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Stopping Application");
        if (this.applicationContext != null && applicationContext.isRunning()) {
            this.applicationContext.close();
        }
        super.stop();
    }
}
