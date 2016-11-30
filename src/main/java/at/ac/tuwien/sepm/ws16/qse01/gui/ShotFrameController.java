package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ImageServiceImpl;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The controller for the shotFrame.
 *
 */

@Component
public class ShotFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrameController.class);

    private SpringFXMLLoader springFXMLLoader;
    private Stage primaryStage;

    private ImageService imageService;

    @FXML
    private ImageView shotView;

    @FXML
    private BorderPane pane;

    @Autowired
    public ShotFrameController(SpringFXMLLoader springFXMLLoader) throws Exception {
        this.springFXMLLoader = springFXMLLoader;

        imageService = new ImageServiceImpl();
        if(shotView != null)
            shotView.setImage(new Image("/images/noimage.png"));

        if(pane != null)
            pane.setBackground(new Background(new BackgroundFill(Color.web("#000000"), CornerRadii.EMPTY, Insets.EMPTY)));

    }

    /*
     Precondition: shootingID must be defined
     Postcondition: the last image will be showed
     */
    /** showing the last image taken
     *
     */
    @FXML
    public void refreshShot(){ //TODO: int camera

        String imgPath = imageService.getLastImgPath(1); //ShootingID = 1; //TODO: create a variable for actual shooting;

        LOGGER.debug("refreshing Shot with imagepath = "+imgPath);

        shotView.setImage(new Image(imgPath));

    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
