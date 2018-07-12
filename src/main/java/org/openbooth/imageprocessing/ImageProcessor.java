package org.openbooth.imageprocessing;

import org.openbooth.entities.Image;
import org.openbooth.entities.Position;
import org.openbooth.gui.ShotFrameController;
import org.openbooth.service.exceptions.ServiceException;

/**
 * This class provides methods for processing previews and shots
 */
public interface ImageProcessor {

    /**
     * Processes a preview image, the image will not be saved and directly
     * displayed in the ShotFrame after the processing is done
     * The ShotFrame is set by the implementation
     * @param imgPath must specify a valid image
     */
    void processPreview(String imgPath) throws ServiceException;

    /**
     * Processes a "real" shot/picture.
     * The image will be saved in the current shooting storage directory and persisted
     * by using the ImageService. MiniaturFrame and FullScreenFrame will be refreshed and the
     * image will be passed to the correct ShotFrame
     * The ShotFrame is set by the implementation
     *
     * If an image is filtered the both the filtered version and the non filtered version
     * are saved and persisted in the database
     * @param image must specify a valid image
     */
    void processShot(Image image) throws ServiceException;

    /**
     * Sets the Shotframecontroller, to which the processed image should be forwarded to display the images.
     * @param shotFrameController the ShotFrameController to which the images will be forwarded
     */
    void setShotFrameController(ShotFrameController shotFrameController);

    /**
     * Sets the position, to which this imageprocessor and shotframe belong to. This determines which filter or greenscreen should be applied.
     * @param position the position to which this imageprocessor belongs.
     */
    void setPosition(Position position);
}
