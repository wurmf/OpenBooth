package org.openbooth.imageprocessing.producer;

import org.openbooth.imageprocessing.exception.ProcessingException;

import java.awt.image.BufferedImage;

public interface ImageProducer {

    BufferedImage produce() throws ProcessingException;

}
