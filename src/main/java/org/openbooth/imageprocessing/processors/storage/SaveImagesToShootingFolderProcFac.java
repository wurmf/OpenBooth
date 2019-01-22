package org.openbooth.imageprocessing.processors.storage;

import org.openbooth.config.key.ConfigStringKeys;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.storage.ConfigStore;
import org.openbooth.storage.exception.KeyValueStoreException;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.ImageNameHandler;
import org.openbooth.util.exceptions.ImageNameHandlingException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SaveImagesToShootingFolderProcFac implements ImageProcessorFactory {

    private ConfigStore configStore;
    private ImageHandler imageHandler;
    private ImageNameHandler imageNameHandler;

    public SaveImagesToShootingFolderProcFac(
            ConfigStore configStore,
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
        } catch (KeyValueStoreException | ImageNameHandlingException e) {
            throw new ProcessingException(e);
        }
    }
}
