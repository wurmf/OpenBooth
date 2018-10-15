package org.openbooth.imageprocessing.processors.storage;

import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.ImageNameHandler;
import org.openbooth.util.exceptions.ImageHandlingException;
import org.openbooth.util.exceptions.ImageNameHandlingException;

import java.awt.image.BufferedImage;
import java.util.List;

public class SaveImagesProcessor implements ImageProcessor {

    private ImageHandler imageHandler;
    private ImageNameHandler imageNameHandler;
    private String destinationFolderPath;
    private String fileExtension;

    public SaveImagesProcessor(ImageHandler imageHandler, ImageNameHandler imageNameHandler, String destinationFolderPath, String fileExtension){
        this.imageHandler = imageHandler;
        this.imageNameHandler = imageNameHandler;
        this.destinationFolderPath = destinationFolderPath;
        this.fileExtension = fileExtension;
    }

    @Override
    public BufferedImage process(BufferedImage image) throws ProcessingException {

        try {
            String imagePath = destinationFolderPath + "/" + imageNameHandler.getNewImageName() + "." + fileExtension;
            imageHandler.saveImage(image, imagePath);
        } catch (ImageHandlingException | ImageNameHandlingException e) {
            throw new ProcessingException(e);
        }

        return image;

    }
}
