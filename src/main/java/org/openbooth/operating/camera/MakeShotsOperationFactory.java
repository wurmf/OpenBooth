package org.openbooth.operating.camera;

import org.openbooth.camera.CameraHandler;
import org.openbooth.operating.OperationFactory;
import org.openbooth.operating.operations.Operation;
import org.openbooth.util.ImageHandler;
import org.springframework.stereotype.Component;

@Component
public class MakeShotsOperationFactory implements OperationFactory {

    private static final String TEST_STORAGE = "/home/fabian";
    private static final int TEST_NUMBER_OF_SHOTS = 1;

    private CameraHandler cameraHandler;
    private ImageHandler imageHandler;

    public MakeShotsOperationFactory(CameraHandler cameraHandler, ImageHandler imageHandler){
        this.cameraHandler = cameraHandler;
        this.imageHandler = imageHandler;
    }

    @Override
    public Operation getOperation() {
        return new MakeShotsOperation(cameraHandler, imageHandler, TEST_NUMBER_OF_SHOTS, TEST_STORAGE);
    }
}
