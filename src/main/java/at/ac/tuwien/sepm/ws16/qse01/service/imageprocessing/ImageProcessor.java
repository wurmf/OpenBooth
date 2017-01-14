package at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing;

import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

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
}
