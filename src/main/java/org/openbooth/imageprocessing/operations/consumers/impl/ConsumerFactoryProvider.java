package org.openbooth.imageprocessing.operations.consumers.impl;

import org.openbooth.config.key.ConfigIntegerKeys;
import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.exception.OperationCreationException;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.operations.OperationFactory;
import org.openbooth.imageprocessing.operations.OperationFactoryProvider;
import org.openbooth.imageprocessing.operations.consumers.ImageConsumingOperation;
import org.openbooth.imageprocessing.operations.consumers.ImageConsumer;
import org.openbooth.storage.ReadOnlyConfigStore;
import org.openbooth.storage.exception.ConfigStoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsumerFactoryProvider implements OperationFactoryProvider<ImageConsumer, ImageConsumingOperation> {


    private ShotFrameController shotFrameController;
    private ReadOnlyConfigStore configStore;

    @Autowired
    private ConsumerFactoryProvider(ShotFrameController shotFrameController, ReadOnlyConfigStore configStore){
        this.shotFrameController = shotFrameController;
        this.configStore = configStore;
    }

    public OperationFactory<ImageConsumer> getOperationFactory(ImageConsumingOperation operation) throws OperationCreationException{
        switch (operation) {
            case SHOW_SHOT:
                return createShowShotFactory();
            case SHOW_PREVIEW:
                return createShowPreviewFactory();

            default:
                throw new OperationCreationException("There is not factory for ImageConsumingOperation " + operation);
        }
    }

    private OperationFactory<ImageConsumer> createShowPreviewFactory(){
        return () -> new ShowPreviewProcessor(shotFrameController);
    }

    private OperationFactory<ImageConsumer> createShowShotFactory(){
        return () -> {
            try {
                int showShotTime = configStore.getInt(ConfigIntegerKeys.SHOW_SHOT_TIME.key);
                return new ShowShotProcessor(shotFrameController, showShotTime);
            } catch (ConfigStoreException e) {
                throw new ProcessingException(e);
            }
        };
    }

}
