package org.openbooth.camera.impl;

import org.openbooth.camera.CameraThread;
import org.openbooth.service.ImageService;
import org.openbooth.service.ShootingService;
import org.openbooth.util.FileTransfer;
import org.openbooth.util.TempStorageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SimCameraThread extends CameraThread {

    private static final Logger LOGGER  = LoggerFactory.getLogger(SimCameraHandler.class);
    private static final String SIM_PREVIEW_IMAGE_PATH = SimCameraThread.class.getResource("/images/simulated.camera/sim_preview_image.png").getPath();
    private static final String SIM_SHOT_IMAGE_PATH = SimCameraThread.class.getResource("/images/simulated.camera/sim_shot_image.png").getPath();

    private FileTransfer fileTransfer;


    public SimCameraThread(ImageService imageService, ShootingService shootingService, TempStorageHandler tempStorageHandler, FileTransfer fileTransfer){
        super(imageService, shootingService, tempStorageHandler);
        this.fileTransfer = fileTransfer;
    }

    @Override
    protected void captureAndTransferImageFromCamera(String imagePath) {
        try {
            fileTransfer.transfer(SIM_SHOT_IMAGE_PATH, imagePath);
        } catch (IOException e) {
            LOGGER.error("captureAndTransferImageFromCamera - Error while copying simulated shot image - stopping camerathread", e);
            shouldStop = true;
        }
    }

    @Override
    protected void captureAndTransferPreviewFromCamera(String imagePath) {
        try {
            fileTransfer.transfer(SIM_PREVIEW_IMAGE_PATH, imagePath);
        } catch (IOException e) {
            LOGGER.error("captureAndTransferPreviewFromCamera - Error while copying simulated preview image - stopping camerathread", e);
            shouldStop = true;
        }
    }

    protected void checkImplInitialized(){
        //Nothing to check for simulated camera
    }
}
