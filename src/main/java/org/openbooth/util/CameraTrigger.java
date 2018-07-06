package org.openbooth.util;

import javafx.scene.input.KeyEvent;
import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.exeptions.CameraException;
import org.openbooth.entities.Camera;
import org.openbooth.entities.Profile;
import org.openbooth.gui.CustomerFrameController;
import org.openbooth.service.ProfileService;
import org.openbooth.service.exceptions.ServiceException;
import org.openbooth.util.exceptions.TriggerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CameraTrigger {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerFrameController.class);

    private CameraHandler cameraHandler;
    private ProfileService profileService;

    @Autowired
    public CameraTrigger(CameraHandler cameraHandler, ProfileService profileService){
        this.cameraHandler = cameraHandler;
        this.profileService = profileService;
    }

    public void triggerShotIfCorrectKey(KeyEvent keyEvent) throws TriggerException {


        int cameraIndex = KeyHandler.getIndexForKeyEvent(keyEvent);
        if(cameraIndex < 0){
            // Return if key is not assigned to trigger a simcam.
            LOGGER.debug("triggerShotIfCorrectKey - key not assigned to trigger cameras");
            return;
        }


        LOGGER.debug( "triggerShotIfCorrectKey - Attempting to trigger simcam at index {} on keypress of {}.", cameraIndex, keyEvent.getCharacter());


        //get active profile or throw exception if no profile is found
        int numberOfPositions;
        Profile activeProfile;
        List<Profile.PairCameraPosition> pairCameraPositionList;

        try {
            activeProfile = profileService.getActiveProfile();
            pairCameraPositionList = activeProfile.getPairCameraPositions();
            numberOfPositions = pairCameraPositionList.size();
        } catch (ServiceException e) {
            // Throw exception because without activeProfile execution is not possible
            throw new TriggerException("Unable to get profile from profileservice.",e);
        }


        // get list of cameras from cameraHandler
        List<Camera> cameras = new ArrayList<>();
        try {
            if (cameraHandler != null ) {
                cameras = cameraHandler.getCameras();
            }
        } catch (CameraException | ServiceException e) {
            throw new TriggerException("Unable to get cameras.",e);
        }


        // Set shotType and capture image only if cameraIndex is in range of cameras that are enlisted in this profile
        if( numberOfPositions > cameraIndex ){

            Profile.PairCameraPosition pairCameraPosition = pairCameraPositionList.get(cameraIndex);

            int shotType = pairCameraPosition.getShotType();
            Camera camera = pairCameraPosition.getCamera();

            if(!cameras.contains(camera)){
                LOGGER.debug("triggerShotIfCorrectKey - Selected simcam is not connected to the computer.");
            }

            setShotType(camera, shotType);


            cameraHandler.captureImage(camera);
            LOGGER.debug("triggerShotIfCorrectKey - Image captured");

        }
        else {
            LOGGER.debug("triggerShotIfCorrectKey - KeyHandler returned an index no simcam is assigned to");
        }
    }



    private void setShotType(Camera camera, int shotType){

        boolean seriesShot = false;
        int countDownTime = 0;

        if(shotType == Profile.PairCameraPosition.SHOT_TYPE_MULTIPLE){
            seriesShot = true;
        }

        if(shotType == Profile.PairCameraPosition.SHOT_TYPE_TIMED){
            countDownTime = 8;
        }

        cameraHandler.setSerieShot(camera, seriesShot);
        cameraHandler.setCountdown(camera, countDownTime);

        LOGGER.debug("setShotType - serieShot set to {}, countDownTime set to {}.",seriesShot,countDownTime);

    }
}
