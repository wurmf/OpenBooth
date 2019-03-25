package org.openbooth.imageprocessing.operations.producers;

import org.openbooth.imageprocessing.exception.ProcessingException;

import java.awt.image.BufferedImage;

public interface ImageProducer {

    BufferedImage produce() throws ProcessingException;

}
