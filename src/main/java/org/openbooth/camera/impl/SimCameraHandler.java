package org.openbooth.camera.impl;

import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.CameraThread;
import org.openbooth.camera.exeptions.CameraException;
import org.openbooth.entities.Camera;

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
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Camera> getCameras() throws CameraException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeCameraFromList(Camera camera) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void captureImage(Camera camera) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSerieShot(Camera camera, boolean serieShot) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCountdown(Camera camera, int countdown) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void closeCameras() {
        throw new UnsupportedOperationException();
    }
}
