package org.openbooth.camera.impl;

import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.exceptions.CameraException;
import org.openbooth.util.FileTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("simulated_camera")
public class SimCameraHandler implements CameraHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimCameraHandler.class);

    private static final String SIM_PREVIEW_IMAGE_PATH = "/images/simcam/sim_preview_image.png";
    private static final String SIM_SHOT_IMAGE_PATH = "/images/simcam/sim_shot_image.png";

    private SimCameraHandler(){
        LOGGER.info("Simulated camera active!");
    }

    @Override
    public String captureAndTransferImageFromCamera(String storagePath, String imageName) throws CameraException{
        return transferImageToLocation(SIM_SHOT_IMAGE_PATH, storagePath, imageName);
    }

    @Override
    public String captureAndTransferPreviewFromCamera(String storagePath, String imageName) throws CameraException {
        return transferImageToLocation(SIM_PREVIEW_IMAGE_PATH, storagePath, imageName);
    }

    @Override
    public void initialize(){
        //This is supposed to be empty, the SimCameraHandler doesn't need initialization
    }

    private String transferImageToLocation(String originalImagePath, String storagePath, String imageName) throws CameraException{
        try {
            String imagePath = storagePath + "/" + imageName + ".png";
            FileTransfer.transfer(originalImagePath, imagePath);
            return imagePath;
        } catch (IOException e) {
            throw new CameraException(e);
        }
    }
}
