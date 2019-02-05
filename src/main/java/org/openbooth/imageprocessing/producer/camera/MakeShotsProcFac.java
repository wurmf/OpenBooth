package org.openbooth.imageprocessing.producer.camera;

import org.openbooth.application.ApplicationContextProvider;
import org.openbooth.camera.CameraHandler;
import org.openbooth.imageprocessing.producer.ImageProducer;
import org.openbooth.imageprocessing.producer.ImageProducerFactory;
import org.openbooth.storage.StorageHandler;
import org.openbooth.storage.exception.StorageException;
import org.openbooth.util.ImageHandler;
import org.springframework.context.ApplicationContext;


public class MakeShotsProcFac implements ImageProducerFactory {

    private CameraHandler cameraHandler;
    private ImageHandler imageHandler;
    private String tempStoragePath;

    public MakeShotsProcFac() throws StorageException{
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        this.cameraHandler = applicationContext.getBean(CameraHandler.class);
        this.imageHandler = applicationContext.getBean(ImageHandler.class);

        tempStoragePath = applicationContext.getBean(StorageHandler.class).getNewTemporaryFolderPath();
    }

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
