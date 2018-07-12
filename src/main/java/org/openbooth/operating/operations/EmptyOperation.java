package org.openbooth.operating.operations;

import java.awt.image.BufferedImage;

public class EmptyOperation implements Operation {

    @Override
    public BufferedImage execute(BufferedImage image) {
        return image;
    }
}
