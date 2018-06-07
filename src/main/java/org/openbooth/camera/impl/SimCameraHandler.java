package org.openbooth.camera.impl;

import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.CameraThread;
import org.openbooth.camera.exeptions.CameraException;
import org.openbooth.entities.Camera;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class SimCameraHandler implements CameraHandler {

    private List<Camera> simulatedCameraList;

    public SimCameraHandler(){
        simulatedCameraList = new ArrayList<>();
        simulatedCameraList.add(new Camera(-1, "simulated_label", "simulated_port", "simulated_model", "simulated_serialnumber"));

    }

    @Override
    public List<CameraThread> createThreads(List<Camera> cameraList) throws CameraException {
        return null;
    }

    @Override
    public List<Camera> getCameras() throws CameraException {
        return null;
    }

    @Override
    public void removeCameraFromList(Camera camera) {

    }

    @Override
    public void captureImage(Camera camera) {

    }

    @Override
    public void setSerieShot(Camera camera, boolean serieShot) {

    }

    @Override
    public void setCountdown(Camera camera, int countdown) {

    }

    @Override
    public void closeCameras() {

    }
}
