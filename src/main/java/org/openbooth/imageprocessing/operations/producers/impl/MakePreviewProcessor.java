package org.openbooth.imageprocessing.operations.producers.impl;

import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.exceptions.CameraException;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.operations.producers.ImageProducer;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.exceptions.ImageHandlingException;

import java.awt.image.BufferedImage;

public class MakePreviewProcessor implements ImageProducer {

    private CameraHandler cameraHandler;
    private String tempStoragePath;
    private String imageName;
    private ImageHandler imageHandler;

    MakePreviewProcessor(CameraHandler cameraHandler, ImageHandler imageHandler, String tempStoragePath, String imageName){
        this.cameraHandler = cameraHandler;
        this.imageHandler = imageHandler;
        this.tempStoragePath = tempStoragePath;
        this.imageName = imageName;
    }

    @Override
    public BufferedImage produce() throws ProcessingException {
        try{
            String imagePath = cameraHandler.captureAndTransferPreviewFromCamera(tempStoragePath, imageName);
            return imageHandler.openImage(imagePath);
        } catch (ImageHandlingException | CameraException e) {
            throw new ProcessingException(e);
        }
    }
}
