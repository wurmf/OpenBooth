package org.openbooth.operating.camera;

import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.exceptions.CameraException;
import org.openbooth.operating.operations.Operation;
import org.openbooth.operating.exception.OperationException;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.exceptions.ImageHandlingException;

import java.awt.image.BufferedImage;
import java.util.List;

public class MakePreviewOperation implements Operation {

    private CameraHandler cameraHandler;
    private String tempStoragePath;
    private ImageHandler imageHandler;

    MakePreviewOperation(CameraHandler cameraHandler, ImageHandler imageHandler, String tempStoragePath){
        this.cameraHandler = cameraHandler;
        this.imageHandler = imageHandler;
        this.tempStoragePath = tempStoragePath;
    }

    @Override
    public void execute(List<BufferedImage> images) throws OperationException {
        try{
            String imagePath = cameraHandler.captureAndTransferPreviewFromCamera(tempStoragePath);
            images.add(imageHandler.openImage(imagePath));
        } catch (ImageHandlingException | CameraException e) {
            throw new OperationException(e);
        }
    }
}
