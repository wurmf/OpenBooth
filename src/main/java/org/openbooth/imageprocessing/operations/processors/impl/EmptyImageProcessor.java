package org.openbooth.imageprocessing.operations.processors.impl;

import org.openbooth.imageprocessing.operations.processors.ImageProcessor;

import java.awt.image.BufferedImage;

public class EmptyImageProcessor implements ImageProcessor {

    EmptyImageProcessor(){}

    @Override
    public BufferedImage process(BufferedImage image) {
        return image;
    }
}
