package org.openbooth.operating.camera;

import org.openbooth.camera.CameraHandler;
import org.openbooth.operating.OperationFactory;
import org.openbooth.operating.exception.OperationException;
import org.openbooth.operating.operations.Operation;
import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.exception.PersistenceException;
import org.openbooth.util.ImageHandler;
import org.springframework.stereotype.Component;

@Component
public class MakeShotsOperationFactory implements OperationFactory {

    private CameraHandler cameraHandler;
    private ImageHandler imageHandler;
    private KeyValueStore keyValueStore;

    public MakeShotsOperationFactory(CameraHandler cameraHandler, ImageHandler imageHandler, KeyValueStore keyValueStore){
        this.cameraHandler = cameraHandler;
        this.imageHandler = imageHandler;
        this.keyValueStore = keyValueStore;
    }

    @Override
    public Operation getOperation() throws OperationException {
        try {
            int numberOfShots = keyValueStore.getInt("number_of_shots");
            String storagePath = keyValueStore.getString("image_storage_path");
            return new MakeShotsOperation(cameraHandler, imageHandler, numberOfShots, storagePath);
        } catch (PersistenceException e) {
            throw new OperationException(e);
        }
    }
}
