package org.openbooth.imageprocessing.processors.storage;

import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.exceptions.ImageHandlingException;
import java.awt.image.BufferedImage;

public class SaveImagesProcessor implements ImageProcessor {

    private ImageHandler imageHandler;
    private String imagePath;

    SaveImagesProcessor(ImageHandler imageHandler, String imagePath){
        this.imageHandler = imageHandler;
        this.imagePath = imagePath;
    }

    @Override
    public BufferedImage process(BufferedImage image) throws ProcessingException {

        try {
            imageHandler.saveImage(image, imagePath);
        } catch (ImageHandlingException e) {
            throw new ProcessingException(e);
        }

        return image;

    }
}
