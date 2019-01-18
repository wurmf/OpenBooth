package org.openbooth.imageprocessing.processors;

import org.openbooth.imageprocessing.exception.ProcessingException;

/**
 * A class implementing this interface returns an ImageProcessor based on it's current status
 *
 * factories should not be singletons (use @Scope(BeanDefinition.SCOPE_PROTOTYPE))
 *
 */
public interface ImageProcessorFactory {

    ImageProcessor getProcessor() throws ProcessingException;
}
