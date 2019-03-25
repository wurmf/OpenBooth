package org.openbooth.imageprocessing.operations.consumers;

import org.openbooth.imageprocessing.exception.ProcessingException;

import java.awt.image.BufferedImage;

public interface ImageConsumer {

    void consume(BufferedImage image) throws ProcessingException;

}
