package org.openbooth.imageprocessing.processors.storage;

import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.exception.KeyValueStoreException;
import org.openbooth.storage.exception.StorageException;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.ImageNameHandler;
import org.springframework.stereotype.Component;

@Component
public class SaveImagesToShootingFolderProcFac implements ImageProcessorFactory {

    private KeyValueStore keyValueStore;
    private ImageHandler imageHandler;
    private ImageNameHandler imageNameHandler;

    public SaveImagesToShootingFolderProcFac(
            KeyValueStore keyValueStore,
            ImageHandler imageHandler,
            ImageNameHandler imageNameHandler
    ){
        this.keyValueStore = keyValueStore;
        this.imageHandler = imageHandler;
        this.imageNameHandler = imageNameHandler;
    }


    @Override
    public ImageProcessor getProcessor() throws ProcessingException {
        try {
            String storagePath = keyValueStore.getString(KeyValueStore.IMAGE_FOLDER);
            return new SaveImagesProcessor(imageHandler, imageNameHandler, storagePath, "jpg");
        } catch (KeyValueStoreException e) {
            throw new ProcessingException(e);
        }
    }
}
