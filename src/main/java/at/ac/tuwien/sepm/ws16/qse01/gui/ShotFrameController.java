package at.ac.tuwien.sepm.ws16.qse01.gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

/**
 * Controller for the shotFrame.
 *
 */
/*@Controller*/

public class ShotFrameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrameController.class);

    @FXML
    private ImageView shotView;

    private int frameID;


    public void initShotFrame(int cameraID)  {
        this.frameID  = cameraID;
    }

    /*
     Precondition: shootingID must be defined
     Postcondition: the last image will be showed
     */
    /** showing the last image taken
     *
     */
    @FXML
    public void refreshShot(String imgPath) {

        LOGGER.debug("refreshing Shot with imagepath = "+imgPath);
        try {
            shotView.setImage(new Image(imgPath));
        }catch (Exception e){
            LOGGER.debug("refreshShot(String) - Fehler - ",e);
        }
    }

    public void refreshShot(BufferedImage img) {

        LOGGER.debug("refreshing Shot with Buffered img = "+img);
        try {
            shotView.setImage(SwingFXUtils.toFXImage(img,null));
        }catch (Exception e){
            LOGGER.debug("refreshShot(BufferedImage) - Fehler - ",e);
        }
    }

    public int getFrameID(){
        return frameID;
    }
}
