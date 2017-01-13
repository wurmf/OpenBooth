package at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.util.OpenCVLoader;
import at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing.impl.ImageProcessorImpl;
import at.ac.tuwien.sepm.ws16.qse01.application.ShotFrameManager;
import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;
import at.ac.tuwien.sepm.ws16.qse01.camera.impl.CameraThread;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.gui.RefreshManager;
import at.ac.tuwien.sepm.ws16.qse01.service.*;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.FilterServiceImpl;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.LogoWatermarkServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

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
        shotFrameManager.init(cameraList);


        cameraThreadList = cameraHandler.createThreads();
        for(CameraThread cameraThread : cameraThreadList){
            Camera camera  = cameraThread.getCamera();

            ImageHandler imageHandler = new ImageHandler(openCVLoader);

            LogoWatermarkService logoWatermarkService = new LogoWatermarkServiceImpl(profileService, imageHandler);
            FilterService filterService = new FilterServiceImpl(shootingService, openCVLoader, imageHandler);

            ImageProcessor imageProcessor = new ImageProcessorImpl(shotFrameManager, shootingService, profileService, imageService, logoWatermarkService, filterService, camera, imageHandler, refreshManager);

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
