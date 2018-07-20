package org.openbooth.operating.storage;

import org.openbooth.operating.OperationFactory;
import org.openbooth.operating.exception.OperationException;
import org.openbooth.operating.operations.Operation;
import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.exception.PersistenceException;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.ImageNameHandler;
import org.springframework.stereotype.Component;

@Component
public class SaveImagesToShootingFolderOperationFactory implements OperationFactory {

    private KeyValueStore keyValueStore;
    private ImageHandler imageHandler;
    private ImageNameHandler imageNameHandler;

    public SaveImagesToShootingFolderOperationFactory(
            KeyValueStore keyValueStore,
            ImageHandler imageHandler,
            ImageNameHandler imageNameHandler
    ){
        this.keyValueStore = keyValueStore;
        this.imageHandler = imageHandler;
        this.imageNameHandler = imageNameHandler;
    }


    @Override
    public Operation getOperation() throws OperationException{
        try {
            String storagePath = keyValueStore.getString("image_storage_path");
            return new SaveImagesOperation(imageHandler, imageNameHandler, storagePath, "jpg");
        } catch (PersistenceException e) {
            throw new OperationException(e);
        }
    }
}
