package org.openbooth.imageprocessing.producer.camera;

import org.openbooth.camera.CameraHandler;
import org.openbooth.imageprocessing.producer.ImageProducer;
import org.openbooth.imageprocessing.producer.ImageProducerFactory;
import org.openbooth.storage.StorageHandler;
import org.openbooth.storage.exception.StorageException;
import org.openbooth.util.ImageHandler;
import org.springframework.stereotype.Component;

@Component
public class MakeShotsProcFac implements ImageProducerFactory {

    private CameraHandler cameraHandler;
    private ImageHandler imageHandler;
    private String tempStoragePath;

    public MakeShotsProcFac(CameraHandler cameraHandler, ImageHandler imageHandler, StorageHandler storageHandler) throws StorageException {
        this.cameraHandler = cameraHandler;
        this.imageHandler = imageHandler;

        tempStoragePath = storageHandler.getNewTemporaryFolderPath();
    }

    @Override
    public ImageProducer getProducer() {
        return new MakeShotsProcessor(cameraHandler, imageHandler, tempStoragePath, "shot_image");
    }
}
