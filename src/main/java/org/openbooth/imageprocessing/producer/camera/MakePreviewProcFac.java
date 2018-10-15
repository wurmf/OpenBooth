package org.openbooth.imageprocessing.producer.camera;

import org.openbooth.camera.CameraHandler;
import org.openbooth.imageprocessing.processors.ImageProcessor;
import org.openbooth.imageprocessing.processors.ImageProcessorFactory;
import org.openbooth.imageprocessing.producer.ImageProducer;
import org.openbooth.imageprocessing.producer.ImageProducerFactory;
import org.openbooth.storage.StorageHandler;
import org.openbooth.storage.exception.StorageHandlingException;
import org.openbooth.util.ImageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MakePreviewProcFac implements ImageProducerFactory {

    private CameraHandler cameraHandler;
    private ImageHandler imageHandler;
    private String tempStoragePath;

    @Autowired
    public MakePreviewProcFac(CameraHandler cameraHandler, ImageHandler imageHandler, StorageHandler storageHandler) throws StorageHandlingException {
        this.cameraHandler = cameraHandler;
        this.imageHandler = imageHandler;
        tempStoragePath = storageHandler.getNewTemporaryFolderPath();
    }

    @Override
    public ImageProducer getProducer() {
        return new MakePreviewProcessor(cameraHandler, imageHandler, tempStoragePath, "preview_image");
    }
}
