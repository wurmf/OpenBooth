package org.openbooth.imageprocessing.processors.storage;

import org.openbooth.config.keys.StringKey;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.exception.KeyValueStoreException;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.ImageNameHandler;
import org.openbooth.util.exceptions.ImageNameHandlingException;
import org.openbooth.util.imageupload.LycheeUploadThread;

public class SaveAndUploadProcFactory implements ImageProcessorFactory {

    private KeyValueStore keyValueStore;
    private ImageHandler imageHandler;
    private ImageNameHandler imageNameHandler;
    private LycheeUploadThread lupThread;

    public SaveAndUploadProcFactory(
            KeyValueStore keyValueStore,
            ImageHandler imageHandler,
            ImageNameHandler imageNameHandler,
            LycheeUploadThread lupThread
    ){
        this.keyValueStore = keyValueStore;
        this.imageHandler = imageHandler;
        this.imageNameHandler = imageNameHandler;
        this.lupThread = lupThread;
    }

    @Override
    public ImageProcessor getProcessor() throws ProcessingException {

        try {
            String storagePath = keyValueStore.getString(StringKey.IMAGE_FOLDER.key);
            String imagePath = storagePath + "/" + imageNameHandler.getNewImageName() + ".jpg";

            SaveImagesProcessor saveProcessor = new SaveImagesProcessor(imageHandler, imagePath);

            return new SaveAndUploadProcessor(saveProcessor, imagePath, this.lupThread);
        } catch (KeyValueStoreException | ImageNameHandlingException e) {
            throw new ProcessingException(e);
        }
    }

}
