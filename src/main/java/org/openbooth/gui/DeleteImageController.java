package org.openbooth.gui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Delete Controller
 */
@Component
public class DeleteImageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteImageController.class);
    @FXML
    private ImageView deleteImage;

    private boolean scene;

    @Autowired
    private WindowManager windowManager;

    public DeleteImageController(WindowManager windowManager) {
        this.windowManager = windowManager;
        scene = false;
    }

    /**
     * No== not deleting the image
     * deciding in witch controller to delete
     */
    public void onNoPressed() {
        if(scene){
            windowManager.showFullscreenImage(false);
        }else {
            windowManager.showMiniaturscene(false);
        }
    }

    /**
     * Yes == deleting the image
     *  deciding in witch controller to delete
     */
    public void onYesPressed() {
        if(scene){
            windowManager.showFullscreenImage(true);
            LOGGER.info("deleting image in controller fullscreen");
        }else {
            windowManager.showMiniaturscene(true);
            LOGGER.info("deleting image in controller miniatur");
        }
    }

    /**
     * set image that should be deleted to be visible in the delete screen
     * sets in witch controller to delete in
     * @param scene if true == full screen controller, if false == miniaturframe controller
     * @param imageView image view of the to be deleted image
     */
    public void setdeleteImage(boolean scene,Image imageView){
        deleteImage.setImage(imageView);
        this.scene = scene;
        deleteImage.setVisible(true);
    }
}
