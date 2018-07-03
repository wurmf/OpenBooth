package org.openbooth.camera.impl;

import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.CameraThread;
import org.openbooth.entities.Camera;
import org.openbooth.service.CameraService;
import org.openbooth.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Profile("simulated_camera")
public class SimCameraHandler implements CameraHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimCameraHandler.class);

    private List<Camera> simulatedCameraList = new ArrayList<>();
    private List<SimCameraThread> simulatedCameraThreadList = new ArrayList<>();


    private ApplicationContext applicationContext;

    public SimCameraHandler(ApplicationContext applicationContext, CameraService cameraService) throws ServiceException{
        this.applicationContext = applicationContext;

        Camera newCamera = new Camera(-1, "simulated_label", "simulated_port", "simulated_model", "simulated_serialnumber");
        Camera storedCamera = cameraService.cameraExists(newCamera);

        if(storedCamera == null)
            storedCamera = cameraService.createCamera(newCamera);

        cameraService.setCameraActive(storedCamera.getId());
        simulatedCameraList.add(storedCamera);

        LOGGER.info("using simulated camera instead of real camera");
    }

    @Override
    public List<CameraThread> createThreads(List<Camera> cameraList){

        List<CameraThread> cameraThreadList = new ArrayList<>();

        for(Camera camera : simulatedCameraList){
            SimCameraThread cameraThread = applicationContext.getBean(SimCameraThread.class);
            cameraThread.setCamera(camera);
            cameraThreadList.add(cameraThread);
            simulatedCameraThreadList.add(cameraThread);

        }

        return cameraThreadList;
    }

    @Override
    public List<Camera> getCameras() {


        return simulatedCameraList;
    }

    @Override
    public void removeCameraFromList(Camera camera) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void captureImage(Camera camera) {
        for(SimCameraThread cameraThread : simulatedCameraThreadList){
            if(cameraThread.getCamera().getId() == camera.getId()) {
                cameraThread.setTakeImage(true);
            }
        }
    }

    @Override
    public void setSerieShot(Camera camera, boolean serieShot) {
        for(SimCameraThread cameraThread : simulatedCameraThreadList){
            if(cameraThread.getCamera().getId() == camera.getId()){
                cameraThread.setSerieShot(true);
            }
        }
    }

    @Override
    public void setCountdown(Camera camera, int countdown) {
        for(SimCameraThread cameraThread : simulatedCameraThreadList){
            if(cameraThread.getCamera().getId() == camera.getId()){
                cameraThread.setCountdown(countdown);
            }
        }
    }

    @Override
    public void closeCameras() {
        //This method is empty, because the simulated cameras don't have to be closed
    }
}
