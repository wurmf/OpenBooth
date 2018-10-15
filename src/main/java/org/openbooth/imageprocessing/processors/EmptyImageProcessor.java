package org.openbooth.imageprocessing.processors;

import java.awt.image.BufferedImage;
import java.util.List;

public class EmptyImageProcessor implements ImageProcessor {

    @Override
    public BufferedImage process(BufferedImage image) {
        return image;
    }
}
