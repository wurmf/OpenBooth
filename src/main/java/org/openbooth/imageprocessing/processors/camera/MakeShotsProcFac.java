package org.openbooth.imageprocessing.processors.camera;

import org.openbooth.camera.CameraHandler;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.StorageHandler;
import org.openbooth.storage.exception.PersistenceException;
import org.openbooth.storage.exception.StorageHandlingException;
import org.openbooth.util.ImageHandler;
import org.springframework.stereotype.Component;

@Component
public class MakeShotsProcFac implements ImageProcessorFactory {

    private CameraHandler cameraHandler;
    private ImageHandler imageHandler;
    private KeyValueStore keyValueStore;
    private String tempStoragePath;

    public MakeShotsProcFac(CameraHandler cameraHandler, ImageHandler imageHandler, KeyValueStore keyValueStore, StorageHandler storageHandler) throws StorageHandlingException {
        this.cameraHandler = cameraHandler;
        this.imageHandler = imageHandler;
        this.keyValueStore = keyValueStore;

        tempStoragePath = storageHandler.getNewTemporaryFolderPath();
    }

    @Override
    public ImageProcessor getProcessor() throws ProcessingException {
        try {
            int numberOfShots = keyValueStore.getInt("number_of_shots");
            return new MakeShotsProcessor(cameraHandler, imageHandler, numberOfShots, tempStoragePath, "shot_image");
        } catch (PersistenceException e) {
            throw new ProcessingException(e);
        }
    }
}
