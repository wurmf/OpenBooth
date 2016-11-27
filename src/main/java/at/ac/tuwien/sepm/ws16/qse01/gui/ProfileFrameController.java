package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * The controller for the profileFrame.
 */
@Component
public class ProfileFrameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileFrameController.class);

    private SpringFXMLLoader springFXMLLoader;
    private Stage profileStage;

    @Autowired
    public ProfileFrameController(SpringFXMLLoader springFXMLLoader) {
        this.springFXMLLoader = springFXMLLoader;
    }

    @FXML
    private void helloWorldAction() throws IOException {
        LOGGER.debug("Hello World pressed");

       /* Stage stage = new Stage();
        stage.setTitle("SEPM - WS16 - Spring/Maven/FXML Sample");

        stage.setScene(new Scene((Parent) this.springFXMLLoader.load("/fxml/questionAnswerFrame.fxml"), 600, 150));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.primaryStage);
        stage.showAndWait();*/
    }

    public void setProfileStage(Stage profileStage) {
        this.profileStage = profileStage;
    }

}
