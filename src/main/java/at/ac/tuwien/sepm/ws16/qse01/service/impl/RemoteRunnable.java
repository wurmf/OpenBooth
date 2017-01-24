package at.ac.tuwien.sepm.ws16.qse01.service.impl;

import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;

import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * RemoteRunnable
 * used to run in a thread to listen to keystrockes numbers
 */
public class RemoteRunnable implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteRunnable.class);
    private volatile boolean running = true;
    private CameraHandler cameraHandler = null;
    private ProfileService profileService = null;
    private Profile activeProfile = null;
    private List<Camera> cameras = new ArrayList<>();
    private int numberOfCameras = 0;
    private int numberOfPositions = 0;
    private Profile.PairCameraPosition pairCameraPosition = null;

    public RemoteRunnable(CameraHandler cameraHandler, ProfileService profileService){
        LOGGER.debug("constructor with parameters cameraHandler{} and profileService {}",cameraHandler,profileService);
        this.cameraHandler = cameraHandler;
        this.profileService = profileService;
        try {
            if (profileService != null) {activeProfile = profileService.getActiveProfile();
            numberOfPositions = activeProfile.getPairCameraPositions().size();}
        } catch (ServiceException e) {
            activeProfile = null;
            LOGGER.debug("Active Profile couldn't be determined, thus null value will be assumed");
        }
        try {
            if (cameraHandler != null) {cameras = cameraHandler.getCameras();numberOfCameras = cameras.size();}
        } catch (CameraException e) {
            cameras = new ArrayList<>();
            LOGGER.debug("Cameras couldn't be determined, thus an empty List will be assumed");
        }

    }

    @Override
    public void run() {
        LOGGER.debug("run - listens for trigger sequences");
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        String keyboardInput = "";

        try {
            while (this.running) {
                try {
                    while (!keyboard.ready()) { Thread.sleep(200);}
                    keyboardInput = keyboard.readLine();
                    this.triggerCall(keyboardInput);
                    }
                catch (InterruptedException e) {
                    LOGGER.debug("listener has been asked to terminate listening");
                    return;
                    }
            }
        }
        catch (IOException e) {
            LOGGER.debug("There seems to be an problem with the keyboard");
        }
    }

    public void stopExecuting(){
        LOGGER.debug("stopExecuting");
        this.running = false;
    }

    public void triggerCall(String triggerSequence){
        LOGGER.debug("triggerCall with triggerSequence {}",triggerSequence);
        int index = -1;
        String messageString = "";
        switch (triggerSequence){
            case "1" : index = 0;break;
            case "2" : index = 1;break;
            case "3" : index = 2;break;
            case "4" : index = 3;break;
            case "5" : index = 4;break;
            case "6" : index = 5;break;
            case "7" : index = 6;break;
            case "8" : index = 7;break;
            case "9" : index = 8;break;
            default: index = -1;break;
        }
        if(index >= 0)
            {messageString = "triggerCall - Attempting to trigger camera object at paitcameraposition list index " + index + " because of valid trigger sequence{}";}
        else
            {messageString = "triggerCall - No action is attempted to be triggered associated to trigger sequence{}";}
        LOGGER.debug(messageString,triggerSequence);

        if( numberOfPositions > index && index >= 0 ){
            messageString = "triggerCall - Camera is at this index present and an image capture is triggered";
            //cameraHandler.captureImage(cameras.get(cameraIndex));
            Profile.PairCameraPosition pairCameraPosition = activeProfile.getPairCameraPositions().get(index);
            int shotType = pairCameraPosition.getShotType();
            Camera camera = pairCameraPosition.getCamera();
            if (shotType == pairCameraPosition.SHOT_TYPE_MULTIPLE){cameraHandler.setSerieShot(camera,true);}
            else if (shotType == pairCameraPosition.SHOT_TYPE_TIMED) {cameraHandler.setCountdown(camera,5);}
            else {}
            cameraHandler.captureImage(camera);
        }
        else if(index >= 0){
            messageString = "triggerCall - No camera at this index found, so no action will be triggered";
        }
        else {
            messageString = "triggerCall - Trigger sequence is invalid";
        }
        LOGGER.debug(messageString);
    }
}