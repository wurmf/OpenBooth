package org.openbooth.operating.storage;

import org.openbooth.operating.exception.OperationException;
import org.openbooth.operating.operations.Operation;
import org.openbooth.storage.exception.PersistenceException;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.ImageNameHandler;
import org.openbooth.util.exceptions.ImageHandlingException;
import org.openbooth.util.exceptions.ImageNameHandlingException;

import java.awt.image.BufferedImage;
import java.util.List;

public class SaveImagesOperation implements Operation {

    private ImageHandler imageHandler;
    private ImageNameHandler imageNameHandler;
    private String destinationFolderPath;
    private String fileExtension;

    public SaveImagesOperation(ImageHandler imageHandler, ImageNameHandler imageNameHandler, String destinationFolderPath, String fileExtension){
        this.imageHandler = imageHandler;
        this.imageNameHandler = imageNameHandler;
        this.destinationFolderPath = destinationFolderPath;
        this.fileExtension = fileExtension;
    }

    @Override
    public void execute(List<BufferedImage> images) throws OperationException{
        for(BufferedImage image : images){
            try {
                String imagePath = destinationFolderPath + "/" + imageNameHandler.getNewImageName() + "." + fileExtension;
                imageHandler.saveImage(image, imagePath);
            } catch (ImageHandlingException | ImageNameHandlingException e) {
                throw new OperationException(e);
            }
        }
    }
}
