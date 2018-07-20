package org.openbooth.operating.camera;

import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.exceptions.CameraException;
import org.openbooth.operating.exception.OperationException;
import org.openbooth.operating.operations.Operation;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.exceptions.ImageHandlingException;

import java.awt.image.BufferedImage;
import java.util.List;

public class MakeShotsOperation implements Operation {

    private CameraHandler cameraHandler;
    private ImageHandler imageHandler;
    private int numberOfShots;
    private String storagePath;
    private String imageName;

    MakeShotsOperation(CameraHandler cameraHandler, ImageHandler imageHandler, int numberOfShots, String storagePath, String imageName){
        this.cameraHandler = cameraHandler;
        this.imageHandler = imageHandler;
        this.numberOfShots = numberOfShots;
        this.storagePath = storagePath;
        this.imageName = imageName;
    }

    @Override
    public void execute(List<BufferedImage> images) throws OperationException {
        for(int i = 0; i < numberOfShots; i++){
            try {
                String currentImagePath = cameraHandler.captureAndTransferImageFromCamera(storagePath, imageName);
                images.add(imageHandler.openImage(currentImagePath));
            } catch (CameraException | ImageHandlingException e) {
                throw new OperationException(e);
            }
        }
    }
}
