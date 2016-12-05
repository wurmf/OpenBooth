package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The controller for the shotFrame.
 *
 */

@Component
//@Scope("prototype")  //Multithreading for multiple shotframes
public class ShotFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShotFrameController.class);

    private ImageService imageService;

    @FXML
    private ImageView shotView;

    @Autowired
    public ShotFrameController(ImageService imageService) throws Exception {

        this.imageService = imageService;
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

        shotView.setImage(new Image("file:" + imgPath));

    }
}
