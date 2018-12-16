package org.openbooth.camera.impl;

import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.exceptions.CameraException;
import org.openbooth.camera.libgphoto2java.CameraFile;
import org.openbooth.camera.libgphoto2java.CameraGphoto;
import org.openbooth.camera.libgphoto2java.CameraList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!simulated_camera")
public class GPhotoCameraHandler implements CameraHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GPhotoCameraHandler.class);

    private boolean isInitialized = false;


    private CameraGphoto camera;

    @Override
    public void initialize() throws CameraException{
        if(isInitialized) return;

        try(CameraList cameraList = new CameraList()) {

            if (cameraList.getCount() == 0) throw new CameraException("No camera found");
            if (cameraList.getCount() > 1)
                throw new CameraException(cameraList.getCount() + " cameras found - there must be only one camera");

            camera = new CameraGphoto();
            camera.initialize();

            isInitialized = true;
            LOGGER.info("Camera successfully initialized");
        }
    }

    @Override
    public String captureAndTransferImageFromCamera(String storagePath, String imageName) throws CameraException {
        if(!isInitialized) initialize();

        try(CameraFile cameraFile = camera.captureImage()){

        if (cameraFile == null) throw new CameraException("camerafile is null");

        String imagePath = storagePath + "/" + imageName + ".jpg";
        cameraFile.save(imagePath);
        return imagePath;
    }
}

    @Override
    public String captureAndTransferPreviewFromCamera(String storagePath, String imageName) throws CameraException {
        if(!isInitialized) initialize();

        try(CameraFile cameraFile = camera.capturePreview()){

            if(cameraFile == null) throw new CameraException("camerafile is null");

            String imagePath = storagePath + "/" + imageName + ".jpg";
            cameraFile.save(imagePath);
            return imagePath;
        }
    }
}
