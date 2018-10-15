package org.openbooth.imageprocessing.processors;

import org.openbooth.imageprocessing.exception.ProcessingException;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * This interface is used for any operation dealing with images,
 * the images in the list might replaced by new images when executed
 * or any other operation using the images is performed
 */
public interface ImageProcessor {

    void process(List<BufferedImage> images) throws ProcessingException;

}
