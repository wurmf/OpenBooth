package org.openbooth.imageprocessing.processors.camera;

import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.exceptions.CameraException;
import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.exceptions.ImageHandlingException;

import java.awt.image.BufferedImage;
import java.util.List;

public class MakePreviewProcessor implements ImageProcessor {

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
    public void process(List<BufferedImage> images) throws ProcessingException {
        try{
            String imagePath = cameraHandler.captureAndTransferPreviewFromCamera(tempStoragePath, imageName);
            images.add(imageHandler.openImage(imagePath));
        } catch (ImageHandlingException | CameraException e) {
            throw new ProcessingException(e);
        }
    }
}
