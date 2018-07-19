package org.openbooth.camera.impl;

import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.exceptions.CameraException;
import org.openbooth.util.FileTransfer;
import org.openbooth.util.ImageNameHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SimCameraHandler implements CameraHandler {

    private static final String SIM_PREVIEW_IMAGE_PATH = "/images/simcam/sim_preview_image.png";
    private static final String SIM_SHOT_IMAGE_PATH = "/images/simcam/sim_shot_image.png";

    private FileTransfer fileTransfer;

    @Autowired
    public SimCameraHandler(FileTransfer fileTransfer){
        this.fileTransfer = fileTransfer;
    }

    @Override
    public String captureAndTransferImageFromCamera(String storagePath) throws CameraException{
        try {
            String imagePath = storagePath + "/" + ImageNameHandler.getNewImageName() + ".png";
            fileTransfer.transfer(SIM_SHOT_IMAGE_PATH, imagePath);
            return imagePath;
        } catch (IOException e) {
            throw new CameraException(e);
        }
    }

    @Override
    public void captureAndTransferPreviewFromCamera(String imagePath) throws CameraException {
        try {
            fileTransfer.transfer(SIM_PREVIEW_IMAGE_PATH, imagePath);
        } catch (IOException e) {
            throw new CameraException(e);
        }
    }
}
