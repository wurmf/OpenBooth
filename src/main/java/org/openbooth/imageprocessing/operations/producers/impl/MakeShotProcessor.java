package org.openbooth.imageprocessing.operations.producers.impl;

import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.exceptions.CameraException;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.operations.producers.ImageProducer;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.exceptions.ImageHandlingException;

import java.awt.image.BufferedImage;

public class MakeShotProcessor implements ImageProducer {

    private CameraHandler cameraHandler;
    private ImageHandler imageHandler;
    private String storagePath;
    private String imageName;

    MakeShotProcessor(CameraHandler cameraHandler, ImageHandler imageHandler, String storagePath, String imageName){
        this.cameraHandler = cameraHandler;
        this.imageHandler = imageHandler;
        this.storagePath = storagePath;
        this.imageName = imageName;
    }

    @Override
    public BufferedImage produce () throws ProcessingException {
        try {
            String currentImagePath = cameraHandler.captureAndTransferImageFromCamera(storagePath, imageName);
            return imageHandler.openImage(currentImagePath);
        } catch (CameraException | ImageHandlingException e) {
            throw new ProcessingException(e);
        }

    }
}
