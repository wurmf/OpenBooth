package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The controller for the mainFrame.
 *
 * @author Dominik Moser
 */
@Component
public class MainFrameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrameController.class);

    private SpringFXMLLoader springFXMLLoader;
    private Stage primaryStage;


    @Autowired
    public MainFrameController(SpringFXMLLoader springFXMLLoader) throws Exception {
        this.springFXMLLoader = springFXMLLoader;



    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

}
