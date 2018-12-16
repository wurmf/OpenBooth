package org.openbooth.imageprocessing.producer;

import org.openbooth.imageprocessing.exception.ProcessingException;

/**
 * A class based on this interface returns an ImageProducer based on the current state of the program
 * factories should not be singletons (use @Scope(BeanDefinition.SCOPE_PROTOTYPE))
 */
public interface ImageProducerFactory {

    ImageProducer getProducer() throws ProcessingException;

}
