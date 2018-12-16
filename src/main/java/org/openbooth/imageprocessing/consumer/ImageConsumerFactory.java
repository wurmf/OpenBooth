package org.openbooth.imageprocessing.consumer;

import org.openbooth.imageprocessing.exception.ProcessingException;

/**
 *  A class based on this interface returns an ImageConsumer based on the current state of the program
 *
 * factories should not be singletons (use @Scope(BeanDefinition.SCOPE_PROTOTYPE))
 */
public interface ImageConsumerFactory {

    ImageConsumer getConsumer() throws ProcessingException;
}
