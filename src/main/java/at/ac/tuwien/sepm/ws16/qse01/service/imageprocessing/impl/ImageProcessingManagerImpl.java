package at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing.impl;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.util.OpenCVLoader;
import at.ac.tuwien.sepm.util.exceptions.LibraryLoadingException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing.ImageProcessingManager;
import at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing.ImageProcessor;
import at.ac.tuwien.sepm.ws16.qse01.application.ShotFrameManager;
import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;
import at.ac.tuwien.sepm.ws16.qse01.camera.impl.CameraThread;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.gui.RefreshManager;
import at.ac.tuwien.sepm.ws16.qse01.service.*;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.FilterServiceImpl;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.GreenscreenServiceImpl;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.LogoWatermarkServiceImpl;
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

    private List<CameraThread> cameraThreadList;

    private RemoteService remoteService;

    @Autowired
    public ImageProcessingManagerImpl(CameraHandler cameraHandler, ShotFrameManager shotFrameManager, RefreshManager refreshManager, ShootingService shootingService, ProfileService profileService, ImageService imageService, RemoteService remoteService, OpenCVLoader openCVLoader){

        this.cameraHandler = cameraHandler;
        this.shotFrameManager = shotFrameManager;
        this.refreshManager = refreshManager;
        this.shootingService = shootingService;
        this.profileService = profileService;
        this.imageService = imageService;
        this.remoteService = remoteService;
        this.openCVLoader = openCVLoader;
    }

    @Override
    public void initImageProcessing() throws ServiceException{
        LOGGER.debug("initImageProcessing - entering initImageProcessing method ");
        List<Camera> cameraList = cameraHandler.getCameras();

        Map<Position, ShotFrameController> positionShotFrameMap = initShotFrameManager(cameraList);
        //If no camerathread has to be initialized then return
        if(positionShotFrameMap == null)
            return;

        cameraThreadList = cameraHandler.createThreads();

        initCameraThreads(positionShotFrameMap);

        for(CameraThread cameraThread : cameraThreadList){
            cameraThread.start();
        }
        remoteService.start();
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
        if(!remoteService.isRunning()){
            LOGGER.debug("stopImageProcessing - RemoteService not running");
        } else {
            remoteService.stop();
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

    private Map<Position, ShotFrameController> initShotFrameManager(List<Camera> cameraList) throws ServiceException{
        List<Position> positionList = new ArrayList<>();

        for(Camera c : cameraList){
            Position p = profileService.getPositionOfCameraOfProfile(c);
            if(p!=null){
                LOGGER.debug("initShotFrameManager - camera added");
                positionList.add(p);
            } else{
                LOGGER.info("initShotFrameManager - no position for this camera: "+c.getId());
                cameraHandler.removeCameraFromList(c);
            }
        }
        int positionNumber = profileService.getAllPairCameraPositionOfProfile().size();

        //If you want to test without cameras attached, comment out
        //TODO: make sure to remove the comments before deploying


        if(positionList.size()!=positionNumber){
            LOGGER.info("initShotFrameManager - attached cameras not compatible with profile positionList.size: "+positionList.size() + " | number of Positions for profile: "+positionNumber);
            throw new ServiceException("Selected Profile not compatible with attached cameras");
        }


        LOGGER.info("initShotFrameManager - attached cameras compatible with profile. Number of assigned positions to connected cameras: "+positionList.size() + " | number of positions for profile: "+positionNumber);

        if(positionList.isEmpty()){
            LOGGER.debug("initShotFrameManager - No cameras specfied in active profile - nothing to initialize");
            return null;
        }

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
            FilterService filterService = new FilterServiceImpl(shootingService, openCVLoader, imageHandler);
            GreenscreenService greenscreenService = new GreenscreenServiceImpl(openCVLoader, imageHandler);

            ImageProcessor imageProcessor = new ImageProcessorImpl(shotFrameController, shootingService, profileService, imageService, logoWatermarkService, filterService, greenscreenService, position, imageHandler, refreshManager);

            cameraThread.setImageService(imageService);
            cameraThread.setShootingService(shootingService);
            cameraThread.setShotFrameController(shotFrameController);
            cameraThread.setImageProcessor(imageProcessor);
        }
    }
}
