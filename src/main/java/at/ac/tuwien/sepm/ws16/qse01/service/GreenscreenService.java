package at.ac.tuwien.sepm.ws16.qse01.service;

import at.ac.tuwien.sepm.ws16.qse01.entities.Background;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

import java.awt.image.BufferedImage;

/**
 * This class provides methods for using greenscreen techniques
 */
public interface GreenscreenService {

    /**
     * Applies the given background to the given image using an greenscreen algorithm
     * The image specified by background must have the same dimensions as the image given by srcImg
     * The given image will not be modified
     * @param srcImg the given image
     * @param background the given background object
     * @return a new image where the green background is replaced with the image specified by the given background object
     * @throws ServiceException if an error during the image processing occurs
     */
    BufferedImage applyGreenscreen(BufferedImage srcImg, Background background) throws ServiceException;
}
