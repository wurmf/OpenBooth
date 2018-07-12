package org.openbooth.operating.camera;

import org.openbooth.camera.CameraHandler;
import org.openbooth.operating.operations.Operation;
import org.openbooth.operating.OperationFactory;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.TempStorageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class MakePreviewOperationFactory implements OperationFactory {

    private CameraHandler cameraHandler;
    private ImageHandler imageHandler;
    private String tempStoragePath;

    @Autowired
    public MakePreviewOperationFactory(CameraHandler cameraHandler, ImageHandler imageHandler, TempStorageHandler tempStorageHandler){
        this.cameraHandler = cameraHandler;
        this.imageHandler = imageHandler;
        tempStoragePath = new File(tempStorageHandler.getTempStoragePath() + "/preview_image.jpg").getAbsolutePath();
    }

    @Override
    public Operation getOperation() {
        return new MakePreviewOperation(cameraHandler, imageHandler, tempStoragePath);
    }
}
