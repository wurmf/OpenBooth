package org.openbooth.imageprocessing.processors;

import org.openbooth.imageprocessing.exception.ProcessingException;

/**
 * A class implementing this interface returns an ImageProcessor based on it's current status
 */
public interface ImageProcessorFactory {

    ImageProcessor getProcessor() throws ProcessingException;
}
