package org.openbooth.imageprocessing.processors;

import org.openbooth.imageprocessing.exception.ProcessingException;

import java.awt.image.BufferedImage;

/**
 * This interface is used for any operation dealing with images,
 * the given image is processed and the resulting image is returned.
 */
public interface ImageProcessor {

    BufferedImage process(BufferedImage image) throws ProcessingException;

}
