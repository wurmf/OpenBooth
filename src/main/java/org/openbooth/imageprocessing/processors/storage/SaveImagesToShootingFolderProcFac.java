package org.openbooth.imageprocessing.processors.storage;

import org.openbooth.application.ApplicationContextProvider;
import org.openbooth.config.key.ConfigStringKeys;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.storage.ReadOnlyConfigStore;
import org.openbooth.storage.exception.ConfigStoreException;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.ImageNameHandler;
import org.openbooth.util.exceptions.ImageNameHandlingException;
import org.springframework.context.ApplicationContext;


public class SaveImagesToShootingFolderProcFac implements ImageProcessorFactory {

    private ReadOnlyConfigStore configStore;
    private ImageHandler imageHandler;
    private ImageNameHandler imageNameHandler;

    public SaveImagesToShootingFolderProcFac(){
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        this.configStore = applicationContext.getBean(ReadOnlyConfigStore.class);
        this.imageHandler = applicationContext.getBean(ImageHandler.class);
        this.imageNameHandler = applicationContext.getBean(ImageNameHandler.class);
    }

    public SaveImagesToShootingFolderProcFac(
            ReadOnlyConfigStore configStore,
            ImageHandler imageHandler,
            ImageNameHandler imageNameHandler
    ){
        this.configStore = configStore;
        this.imageHandler = imageHandler;
        this.imageNameHandler = imageNameHandler;
    }


    @Override
    public ImageProcessor getProcessor() throws ProcessingException {
        try {
            String storagePath = configStore.getString(ConfigStringKeys.IMAGE_FOLDER.key);
            String imagePath = storagePath + "/" + imageNameHandler.getNewImageName() + ".jpg";
            return new SaveImagesProcessor(imageHandler, imagePath);
        } catch (ConfigStoreException | ImageNameHandlingException e) {
            throw new ProcessingException(e);
        }
    }
}
