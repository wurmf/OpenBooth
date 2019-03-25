package org.openbooth.imageprocessing.operations.processors.impl;

import org.openbooth.imageprocessing.operations.processors.ImageProcessor;

import java.awt.image.BufferedImage;

public class MirrorImageProcessor implements ImageProcessor {

    MirrorImageProcessor(){}

    @Override
    public BufferedImage process(BufferedImage image)  {
        BufferedImage mirroredImage = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        mirroredImage.createGraphics().drawImage(image, image.getWidth(),0,- image.getWidth(),image.getHeight(),null);
        return mirroredImage;
    }
}
