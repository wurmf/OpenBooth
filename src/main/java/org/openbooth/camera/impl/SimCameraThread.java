package org.openbooth.camera.impl;

import org.openbooth.camera.CameraThread;
import org.openbooth.service.ImageService;
import org.openbooth.service.ShootingService;
import org.openbooth.util.TempStorageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimCameraThread extends CameraThread {

    private static final Logger LOGGER  = LoggerFactory.getLogger(SimCameraHandler.class);
    private static final String SIM_PREVIEW_IMAGE_PATH = "";
    private static final String SIM_SHOT_IMAGE_PATH = "";


    public SimCameraThread(ImageService imageService, ShootingService shootingService, TempStorageHandler tempStorageHandler){
        super(imageService, shootingService, tempStorageHandler);
    }

    @Override
    protected void captureAndTransferImageFromCamera(String imagePath) {

    }

    @Override
    protected void captureAndTransferPreviewFromCamera(String imagePath) {

    }

    protected void checkImplInitialized(){
        //Nothing to check for simulated camera
    }
}
