package org.openbooth.service;

import org.openbooth.service.exceptions.ServiceException;

import java.awt.image.BufferedImage;

/**
 * This class provides methods for using greenscreen techniques
 */
public interface GreenscreenService {

    /**
     * Applies the given background to the given image using a greenscreen algorithm
     * The given image will not be modified
     * @param srcImg the given image
     * @param backgroundPath the path to the given background image
     * @return a new image where the green background is replaced with the image specified by the given background object
     * @throws ServiceException if an error during the image processing occurs
     */
    BufferedImage applyGreenscreen(BufferedImage srcImg, String backgroundPath) throws ServiceException;
}
