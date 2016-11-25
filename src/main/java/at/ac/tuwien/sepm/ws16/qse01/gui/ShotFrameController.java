package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.service.FotoService;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.FotoServiceImpl;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
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
 * Created by macdnz on 25.11.16.
 */

@Component
public class ShotFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrameController.class);

    private SpringFXMLLoader springFXMLLoader;
    private Stage primaryStage;

    private FotoService fotoService;

    @FXML
    private ImageView shotView;

    @FXML
    private BorderPane pane;

    @Autowired
    public ShotFrameController(SpringFXMLLoader springFXMLLoader) throws Exception {
        this.springFXMLLoader = springFXMLLoader;

        fotoService = new FotoServiceImpl();
        if(shotView != null)
            shotView.setImage(new Image("/images/noimage.png"));

        if(pane != null)
            pane.setBackground(new Background(new BackgroundFill(Color.web("#000000"), CornerRadii.EMPTY, Insets.EMPTY)));

    }

    /** showing the last image taken
     * @param camera - camera id of taken image
     *
     */
    @FXML
    public void refreshShot(){ //TODO int camera
        String imgPath = fotoService.getLastImgPath(1); //ShootingID = 1; TODO create a variable for actual shooting;

        LOGGER.debug("refreshing Shot with imagepath = "+imgPath);

        shotView.setImage(new Image(imgPath));

    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
