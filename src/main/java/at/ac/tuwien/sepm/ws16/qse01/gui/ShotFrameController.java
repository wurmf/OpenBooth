package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * The controller for the shotFrame.
 *
 */

@Component
//@Scope("prototype")  //Multithreading for multiple shotframes
public class ShotFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrameController.class);

    @Resource
    private ImageService imageService;

    @Resource
    private ShootingService shootingService;

    @FXML
    private ImageView shotView;

    @Autowired
    public ShotFrameController(ImageService imageService,ShootingService shootingService) throws Exception {

        this.imageService = imageService;
        this.shootingService = shootingService;


    }

    /*
     Precondition: shootingID must be defined
     Postcondition: the last image will be showed
     */
    /** showing the last image taken
     *
     */
    @FXML
    public void refreshShot() { //TODO: int camera
        String imgPath = null;
        try {
            imgPath = imageService.getLastImgPath(shootingService.searchIsActive().getId());
        } catch (ServiceException e) {
            LOGGER.debug("Fehler: "+e.getMessage());
        }

        LOGGER.debug("refreshing Shot with imagepath = "+imgPath);
        try {
            shotView.setImage(new Image(imgPath));
        }catch (Exception e){
            LOGGER.debug("Fehler: "+e.getMessage());
        }

    }
}
