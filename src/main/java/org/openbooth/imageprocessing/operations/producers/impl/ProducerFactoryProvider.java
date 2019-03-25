package org.openbooth.imageprocessing.operations.producers.impl;

import org.openbooth.camera.CameraHandler;
import org.openbooth.imageprocessing.exception.OperationCreationException;
import org.openbooth.imageprocessing.operations.OperationFactory;
import org.openbooth.imageprocessing.operations.OperationFactoryProvider;
import org.openbooth.imageprocessing.operations.producers.ImageProducer;
import org.openbooth.imageprocessing.operations.producers.ImageProducingOperation;
import org.openbooth.storage.StorageHandler;
import org.openbooth.storage.exception.StorageException;
import org.openbooth.util.ImageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProducerFactoryProvider implements OperationFactoryProvider<ImageProducer, ImageProducingOperation> {

    private CameraHandler cameraHandler;
    private ImageHandler imageHandler;
    private StorageHandler storageHandler;

    @Autowired
    private ProducerFactoryProvider(CameraHandler cameraHandler, ImageHandler imageHandler, StorageHandler storageHandler) {
        this.cameraHandler = cameraHandler;
        this.imageHandler = imageHandler;
        this.storageHandler = storageHandler;
    }

    @Override
    public OperationFactory<ImageProducer> getOperationFactory(ImageProducingOperation operation) throws OperationCreationException {
        switch (operation) {
            case MAKE_SHOT:
                return createMakeShotFactory();

            case MAKE_PREVIEW:
                return createMakePreviewFactory();

            default:
                throw new OperationCreationException("There is not factory for ImageProducingOperation " + operation);
        }
    }

    private OperationFactory<ImageProducer> createMakeShotFactory() throws OperationCreationException {
        try {
            String tempStoragePath = storageHandler.getNewTemporaryFolderPath();
            return () -> new MakeShotProcessor(cameraHandler, imageHandler, tempStoragePath, "shot_image");
        } catch (StorageException e) {
            throw new OperationCreationException(e);
        }
    }

    private OperationFactory<ImageProducer> createMakePreviewFactory() throws OperationCreationException{
        try {
            String tempStoragePath = storageHandler.getNewTemporaryFolderPath();
            return () -> new MakePreviewProcessor(cameraHandler, imageHandler, tempStoragePath, "preview_image");
        } catch (StorageException e) {
            throw new OperationCreationException(e);
        }
    }


}
