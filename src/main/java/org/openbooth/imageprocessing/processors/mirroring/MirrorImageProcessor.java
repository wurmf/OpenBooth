package org.openbooth.imageprocessing.processors.mirroring;

import org.openbooth.imageprocessing.processors.ImageProcessor;

import java.awt.image.BufferedImage;

public class MirrorImageProcessor implements ImageProcessor {

    @Override
    public BufferedImage process(BufferedImage image)  {
        BufferedImage mirroredImage = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        mirroredImage.createGraphics().drawImage(image, image.getWidth(),0,- image.getWidth(),image.getHeight(),null);
        return mirroredImage;
    }
}
