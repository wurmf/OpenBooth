package org.openbooth.imageprocessing.consumer;

import org.openbooth.imageprocessing.exception.ProcessingException;

public interface ImageConsumerFactory {

    ImageConsumer getConsumer() throws ProcessingException;
}
