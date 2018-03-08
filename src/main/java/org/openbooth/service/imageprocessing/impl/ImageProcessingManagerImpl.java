package org.openbooth.service.imageprocessing.impl;

import org.openbooth.util.ImageHandler;
import org.openbooth.util.OpenCVLoader;
import org.openbooth.util.TempStorageHandler;
import org.openbooth.util.exceptions.LibraryLoadingException;
import org.openbooth.entities.Camera;
import org.openbooth.entities.Position;
import org.openbooth.entities.Profile;
import org.openbooth.gui.ShotFrameController;
import org.openbooth.service.*;
import org.openbooth.service.exceptions.ServiceException;
import org.openbooth.service.imageprocessing.ImageProcessingManager;
import org.openbooth.service.imageprocessing.ImageProcessor;
import org.openbooth.gui.ShotFrameManager;
import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.impl.CameraThread;
import org.openbooth.gui.RefreshManager;
import org.openbooth.service.impl.FilterServiceImpl;
import org.openbooth.service.impl.GreenscreenServiceImpl;
import org.openbooth.service.impl.LogoWatermarkServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class implements the ImageProcessingManager interface
 */
@Component
public class ImageProcessingManagerImpl implements ImageProcessingManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessingManagerImpl.class);

    private CameraHandler cameraHandler;
    private ShotFrameManager shotFrameManager;
    private RefreshManager refreshManager;

    private ShootingService shootingService;
    private ProfileService profileService;
    private ImageService imageService;

    private OpenCVLoader openCVLoader;
    private TempStorageHandler tempStorageHandler;

    private List<CameraThread> cameraThreadList;

    @Autowired
    public ImageProcessingManagerImpl(CameraHandler cameraHandler, ShotFrameManager shotFrameManager, RefreshManager refreshManager, ShootingService shootingService, ProfileService profileService, ImageService imageService, OpenCVLoader openCVLoader, TempStorageHandler tempStorageHandler){

        this.cameraHandler = cameraHandler;
        this.shotFrameManager = shotFrameManager;
        this.refreshManager = refreshManager;
        this.shootingService = shootingService;
        this.profileService = profileService;
        this.imageService = imageService;
        this.openCVLoader = openCVLoader;
        this.tempStorageHandler = tempStorageHandler;
    }

    @Override
    public void initImageProcessing() throws ServiceException{
        LOGGER.debug("initImageProcessing - entering initImageProcessing method ");
        List<Camera> cameraList = cameraHandler.getCameras();

        List<Position> positionList = new ArrayList<>();
        List<Camera> activeCameraList = new ArrayList<>();

        for(int i=0; i<cameraList.size(); i++){
            Camera c = cameraList.get(i);
            Position p = profileService.getPositionOfCameraOfProfile(c);
            if(p!=null){
                LOGGER.debug("initImageProcessing - camera added");
                positionList.add(p);
                activeCameraList.add(c);
            } else{
                LOGGER.info("initImageProcessing - no position for this camera: {}", c);
                //cameraHandler.removeCameraFromList(c);
            }
        }
        int positionNumber = profileService.getAllPairCameraPositionOfProfile().size();



        if(positionList.size()!=positionNumber){
            LOGGER.info("initImageProcessing - attached cameras not compatible with profile positionList.size: "+positionList.size() + " | number of Positions for profile: "+positionNumber);
            throw new ServiceException("Selected Profile not compatible with attached cameras");
        }


        LOGGER.info("initImageProcessing- attached cameras compatible with profile. Number of assigned positions to connected cameras: "+positionList.size() + " | number of positions for profile: "+positionNumber);

        if(positionList.isEmpty()){
            LOGGER.debug("initShotFrameManager - No cameras specfied in active profile - nothing to initialize");
            return;
        }

        Map<Position, ShotFrameController> positionShotFrameMap = initShotFrameManager(positionList);

        cameraThreadList = cameraHandler.createThreads(activeCameraList);

        initCameraThreads(positionShotFrameMap);

        for(CameraThread cameraThread : cameraThreadList){
            cameraThread.start();
        }
        LOGGER.info("initImageProcessing - image processing initialised");
    }

    @Override
    public void stopImageProcessing(){
        if(cameraThreadList == null) {
            LOGGER.debug("stopImageProcessing - No CameraThreads running");
        }else {
            for(CameraThread cameraThread : cameraThreadList){
                cameraThread.setStop(true);
            }
        }
    }

    @Override
    public boolean checkImageProcessing(Profile profile) throws ServiceException{
        LOGGER.debug("Entering checkImageProcessing method");
        List<Camera> cameraList = cameraHandler.getCameras();
        int cameraCount = 0;

        for(Camera camera : cameraList){
            Position correspondingPosition = profileService.getPositionOfCameraOfProfile(profile, camera);
            if(correspondingPosition != null){
                LOGGER.debug("checkImageProcessing - included camera detected");
                cameraCount++;
            }else {
                LOGGER.debug("checkImageProcessing - no positon: camera {} ignored", camera);
            }
        }

        int includedCameraCount = profileService.getAllPairCameraPositionOfProfile(profile.getId()).size();

        if(cameraCount == includedCameraCount){
            LOGGER.debug("checkImageProcessing - profile {} compatible with connected cameras", profile);
            return true;
        } else {
            LOGGER.debug("checkImageProcessing - profile {} not compatible with connected cameras", profile);
            return false;
        }

    }

    private Map<Position, ShotFrameController> initShotFrameManager(List<Position> positionList) throws ServiceException {


        Map<Position, ShotFrameController> positionShotFrameMap = shotFrameManager.init(positionList);
        if(positionShotFrameMap == null || positionShotFrameMap.isEmpty()){
            LOGGER.error("initShotFrameManager - initializing ShotFrames failed - positionShotFrameMap is null or empty");
            throw new ServiceException("Initializing ShotFrames failed");
        }

        return positionShotFrameMap;
    }

    private void initCameraThreads(Map<Position, ShotFrameController> positionShotFrameMap) throws ServiceException{

        for(CameraThread cameraThread : cameraThreadList){
            if(cameraThread.getCamera() == null){
                LOGGER.error("initCameraThreads - No camera set for camerathread {}", cameraThread);
            }
            Position position  = profileService.getPositionOfCameraOfProfile(cameraThread.getCamera());
            if(position == null){
                LOGGER.error("initCameraThreads - No position set for camera {}", cameraThread.getCamera());
                throw new ServiceException("invalid camera - no position set");
            }

            ShotFrameController shotFrameController = positionShotFrameMap.get(position);

            if(shotFrameController == null){
                LOGGER.error("initCameraThreads - No ShotFrameController for positon {}", position);
                throw new ServiceException("Missing ShotFrameController for position");
            }

            ImageHandler imageHandler;
            try {
                imageHandler = new ImageHandler(openCVLoader);
            } catch (LibraryLoadingException e) {
                shotFrameManager.closeFrames();
                throw new ServiceException(e);
            }

            LogoWatermarkService logoWatermarkService = new LogoWatermarkServiceImpl(profileService, imageHandler);
            FilterService filterService = new FilterServiceImpl(openCVLoader, imageHandler, tempStorageHandler);
            GreenscreenService greenscreenService = new GreenscreenServiceImpl(openCVLoader, imageHandler);

            ImageProcessor imageProcessor = new ImageProcessorImpl(shotFrameController, shootingService, profileService, imageService, logoWatermarkService, filterService, greenscreenService, position, imageHandler, refreshManager);

            cameraThread.setImageService(imageService);
            cameraThread.setShootingService(shootingService);
            cameraThread.setTempStoragePath(tempStorageHandler.getTempStoragePath());
            cameraThread.setShotFrameController(shotFrameController);
            cameraThread.setImageProcessor(imageProcessor);
        }
    }
}
