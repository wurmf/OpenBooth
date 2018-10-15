package org.openbooth.imageprocessing.processors.camera;

import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.exceptions.CameraException;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.exceptions.ImageHandlingException;

import java.awt.image.BufferedImage;
import java.util.List;

public class MakeShotsProcessor implements ImageProcessor {

    private CameraHandler cameraHandler;
    private ImageHandler imageHandler;
    private int numberOfShots;
    private String storagePath;
    private String imageName;

    MakeShotsProcessor(CameraHandler cameraHandler, ImageHandler imageHandler, int numberOfShots, String storagePath, String imageName){
        this.cameraHandler = cameraHandler;
        this.imageHandler = imageHandler;
        this.numberOfShots = numberOfShots;
        this.storagePath = storagePath;
        this.imageName = imageName;
    }

    @Override
    public void process(List<BufferedImage> images) throws ProcessingException {
        for(int i = 0; i < numberOfShots; i++){
            try {
                String currentImagePath = cameraHandler.captureAndTransferImageFromCamera(storagePath, imageName);
                images.add(imageHandler.openImage(currentImagePath));
            } catch (CameraException | ImageHandlingException e) {
                throw new ProcessingException(e);
            }
        }
    }
}
