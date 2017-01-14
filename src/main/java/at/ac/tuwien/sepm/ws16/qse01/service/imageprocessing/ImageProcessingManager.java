package at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.util.OpenCVLoader;
import at.ac.tuwien.sepm.util.exceptions.LibraryLoadingException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing.impl.ImageProcessorImpl;
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
 * Created by fabian on 13.01.17.
 */
@Component
public class ImageProcessingManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessingManager.class);

    private CameraHandler cameraHandler;
    private ShotFrameManager shotFrameManager;
    private RefreshManager refreshManager;

    private ShootingService shootingService;
    private ProfileService profileService;
    private ImageService imageService;

    private OpenCVLoader openCVLoader;

    private List<CameraThread> cameraThreadList;

    public ImageProcessingManager(CameraHandler cameraHandler, ShotFrameManager shotFrameManager, RefreshManager refreshManager, ShootingService shootingService, ProfileService profileService, ImageService imageService, OpenCVLoader openCVLoader){

        this.cameraHandler = cameraHandler;
        this.shotFrameManager = shotFrameManager;
        this.refreshManager = refreshManager;
        this.shootingService = shootingService;
        this.profileService = profileService;
        this.imageService = imageService;
        this.openCVLoader = openCVLoader;
    }

    public void initImageProcessing() throws ServiceException{
        LOGGER.debug("entering initImageProcessing method ");
        List<Camera> cameraList = cameraHandler.getCameras();
        List<Position> positionList = new ArrayList<>();
        for(Camera c : cameraList){
            Position p = profileService.getPositionOfCameraOfProfile(c);
            positionList.add(p);
        }

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
                throw new ServiceException("Could not load opencv library", e);
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
    }

    public void stopImageProcessing(){
        if(cameraThreadList == null) {
            LOGGER.debug("No CameraThreads running - nothing to stop");
        }else {
            for(CameraThread cameraThread : cameraThreadList){
                cameraThread.setStop(true);
            }
        }
    }
}
