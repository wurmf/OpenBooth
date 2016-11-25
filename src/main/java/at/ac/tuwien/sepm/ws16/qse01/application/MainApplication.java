package at.ac.tuwien.sepm.ws16.qse01.application;

import at.ac.tuwien.sepm.ws16.qse01.gui.MainFrameController;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
 * @author Dominik Moser
 */
@Configuration
@ComponentScan("at.ac.tuwien.sepm")
public class MainApplication extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    private AnnotationConfigApplicationContext applicationContext = null;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        LOGGER.info("Starting Application");
        applicationContext = new AnnotationConfigApplicationContext(MainApplication.class);
        SpringFXMLLoader springFXMLLoader = applicationContext.getBean(SpringFXMLLoader.class);
        SpringFXMLLoader.FXMLWrapper<Object, MainFrameController> mfWrapper =
                springFXMLLoader.loadAndWrap("/fxml/mainFrame.fxml", MainFrameController.class);
        mfWrapper.getController().setPrimaryStage(primaryStage);
        primaryStage.setTitle("SEPM - WS16 - Spring/Maven/FXML Sample");
        primaryStage.setScene(new Scene((Parent) mfWrapper.getLoadedObject(), 800, 200));
        primaryStage.show();

        /* Creating shotFrame */
        SpringFXMLLoader.FXMLWrapper<Object, ShotFrameController> shotWrapper =
                springFXMLLoader.loadAndWrap("/fxml/shotFrame.fxml", ShotFrameController.class);
        Stage shotStage = new Stage();
        shotStage.setScene(new Scene((Parent) shotWrapper.getLoadedObject()));
        shotStage.setFullScreen(true);
        shotStage.show();


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
