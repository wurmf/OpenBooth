package org.openbooth.operating.camera;

import org.openbooth.camera.CameraHandler;
import org.openbooth.operating.operations.Operation;
import org.openbooth.operating.OperationFactory;
import org.openbooth.storage.StorageHandler;
import org.openbooth.storage.exception.StorageHandlingException;
import org.openbooth.util.ImageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MakePreviewOperationFactory implements OperationFactory {

    private CameraHandler cameraHandler;
    private ImageHandler imageHandler;
    private String tempStoragePath;

    @Autowired
    public MakePreviewOperationFactory(CameraHandler cameraHandler, ImageHandler imageHandler, StorageHandler storageHandler) throws StorageHandlingException {
        this.cameraHandler = cameraHandler;
        this.imageHandler = imageHandler;
        tempStoragePath = storageHandler.getNewTemporaryFolderPath();
    }

    @Override
    public Operation getOperation() {
        return new MakePreviewOperation(cameraHandler, imageHandler, tempStoragePath);
    }
}
