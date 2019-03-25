package org.openbooth.imageprocessing.operations.processors.impl;

import org.openbooth.config.key.ConfigStringKeys;
import org.openbooth.imageprocessing.exception.OperationCreationException;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.operations.OperationFactory;
import org.openbooth.imageprocessing.operations.OperationFactoryProvider;
import org.openbooth.imageprocessing.operations.processors.ImageProcessingOperation;
import org.openbooth.imageprocessing.operations.processors.ImageProcessor;
import org.openbooth.storage.ReadOnlyConfigStore;
import org.openbooth.storage.exception.ConfigStoreException;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.ImageNameHandler;
import org.openbooth.util.exceptions.ImageNameHandlingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessorFactoryProvider implements OperationFactoryProvider<ImageProcessor, ImageProcessingOperation> {

    private ReadOnlyConfigStore configStore;
    private ImageNameHandler imageNameHandler;
    private ImageHandler imageHandler;

    @Autowired
    public ProcessorFactoryProvider(ReadOnlyConfigStore configStore, ImageNameHandler imageNameHandler, ImageHandler imageHandler) {
        this.configStore = configStore;
        this.imageNameHandler = imageNameHandler;
        this.imageHandler = imageHandler;
    }

    @Override
    public OperationFactory<ImageProcessor> getOperationFactory(ImageProcessingOperation operation) throws OperationCreationException {
        switch (operation){
            case MIRROR_IMAGE:
                return createMirrorImageFactory();

            case SAVE_IMAGE_TO_SHOOTING_FOLDER:
                return createSaveImageToShootingFolderFactory();

            default:
                throw new OperationCreationException("There is not factory for ImageProcessingOperation " + operation);
        }
    }


    private OperationFactory<ImageProcessor> createMirrorImageFactory(){
        return MirrorImageProcessor::new;
    }

    private OperationFactory<ImageProcessor> createSaveImageToShootingFolderFactory(){
        return () -> {
            try {
                String storagePath = configStore.getString(ConfigStringKeys.IMAGE_FOLDER.key);
                String imagePath = storagePath + "/" + imageNameHandler.getNewImageName() + ".jpg";
                return new SaveImageProcessor(imageHandler, imagePath);
            } catch (ConfigStoreException | ImageNameHandlingException e) {
                throw new ProcessingException(e);
            }
        };
    }
}
