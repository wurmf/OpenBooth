package at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing.impl;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.util.OpenCVLoader;
import at.ac.tuwien.sepm.util.exceptions.LibraryLoadingException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
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

    public ImageProcessingManagerImpl(CameraHandler cameraHandler, ShotFrameManager shotFrameManager, RefreshManager refreshManager, ShootingService shootingService, ProfileService profileService, ImageService imageService, OpenCVLoader openCVLoader){

        this.cameraHandler = cameraHandler;
        this.shotFrameManager = shotFrameManager;
        this.refreshManager = refreshManager;
        this.shootingService = shootingService;
        this.profileService = profileService;
        this.imageService = imageService;
        this.openCVLoader = openCVLoader;
    }

    @Override
    public boolean initImageProcessing() throws ServiceException{
        LOGGER.debug("initImageProcessing - entering initImageProcessing method ");
        List<Camera> cameraList = cameraHandler.getCameras();
        List<Position> positionList = new ArrayList<>();

        for(Camera c : cameraList){
            Position p = profileService.getPositionOfCameraOfProfile(c);
            if(p!=null){
                LOGGER.debug("initImageProcessing - camera added");
                positionList.add(p);
            } else{
                LOGGER.info("initImageProcessing - no position for this camera: "+c.getId());
                cameraHandler.removeCameraFromList(c);
            }
        }
        int positionNumber=profileService.getActiveProfile().getPairCameraPositions().size();

        //If you want to test without cameras attached, comment out
        //TODO: make sure to remove the comments before deploying
        /*
        if(positionList.size()!=positionNumber){
            LOGGER.info("initImageProcessing - attached cameras not compatible with profile positionList.size: "+positionList.size() + " | number of Positions for profile: "+positionNumber);
            return false;
        }
        */
        LOGGER.info("initImageProcessing - attached cameras compatible with profile. Number of assigned positions to connected cameras: "+positionList.size() + " | number of positions for profile: "+positionNumber);


        Map<Position, ShotFrameController> positionShotFrameMap = shotFrameManager.init(positionList);


        cameraThreadList = cameraHandler.createThreads();
        for(CameraThread cameraThread : cameraThreadList){
            Position position  = profileService.getPositionOfCameraOfProfile(cameraThread.getCamera());
            ShotFrameController shotFrameController = positionShotFrameMap.get(position);

            ImageHandler imageHandler;
            try {
                imageHandler = new ImageHandler(openCVLoader);
            } catch (LibraryLoadingException e) {
                LOGGER.error("initImageProcessing - Could not load opencv library", e);
                shotFrameManager.closeFrames();
                throw new ServiceException("Could not load opencv library");
            }

            LogoWatermarkService logoWatermarkService = new LogoWatermarkServiceImpl(profileService, imageHandler);
            FilterService filterService = new FilterServiceImpl(shootingService, openCVLoader, imageHandler);
            GreenscreenService greenscreenService = new GreenscreenServiceImpl(openCVLoader, imageHandler);

            ImageProcessor imageProcessor = new ImageProcessorImpl(shotFrameController, shootingService, profileService, imageService, logoWatermarkService, filterService, greenscreenService, position, imageHandler, refreshManager);

            cameraThread.setImageService(imageService);
            cameraThread.setShootingService(shootingService);
            cameraThread.setImageProcessor(imageProcessor);
        }

        for(CameraThread cameraThread : cameraThreadList){
            cameraThread.start();
        }
        LOGGER.debug("image processing initialised");
        return true;
    }

    @Override
    public void stopImageProcessing(){
        if(cameraThreadList == null) {
            LOGGER.debug("stopImageProcessing - No CameraThreads running - nothing to stop");
        }else {
            for(CameraThread cameraThread : cameraThreadList){
                cameraThread.setStop(true);
            }
        }
    }
}
