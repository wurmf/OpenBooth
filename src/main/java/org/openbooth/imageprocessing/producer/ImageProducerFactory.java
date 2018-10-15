package org.openbooth.imageprocessing.producer;

import org.openbooth.imageprocessing.exception.ProcessingException;

public interface ImageProducerFactory {

    ImageProducer getProducer() throws ProcessingException;

}
