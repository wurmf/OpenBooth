package at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing.impl;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing.ImageProcessor;
import at.ac.tuwien.sepm.ws16.qse01.application.ShotFrameManager;
import at.ac.tuwien.sepm.ws16.qse01.entities.*;
import at.ac.tuwien.sepm.ws16.qse01.gui.RefreshManager;
import at.ac.tuwien.sepm.ws16.qse01.service.*;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

/**
 * This class implements an image processor
 */
public class ImageProcessorImpl implements ImageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessorImpl.class);

    private ShotFrameManager shotFrameManager;
    private ShootingService shootingService;
    private ProfileService profileService;
    private ImageService imageService;

    private LogoWatermarkService logoWatermarkService;
    private FilterService filterService;
    //private GreenscreenService greenscreenService;
    //TODO: adapt for greenscreenService

    private Camera camera;
    private Profile.PairCameraPosition pairCameraPosition;

    private ImageHandler imageHandler;
    private RefreshManager refreshManager;


    public ImageProcessorImpl(ShotFrameManager shotFrameManager, ShootingService shootingService, ProfileService profileService, ImageService imageService, LogoWatermarkService logoWatermarkService, FilterService filterService, Camera camera, ImageHandler imageHandler, RefreshManager refreshManager){
        this.shotFrameManager = shotFrameManager;
        this.shootingService = shootingService;
        this.profileService = profileService;
        this.imageService = imageService;
        this.logoWatermarkService = logoWatermarkService;
        this.filterService = filterService;


        this.camera = camera;

        this.imageHandler = imageHandler;
        this.refreshManager = refreshManager;
    }

    @Override
    public void processPreview(String imgPath) throws ServiceException{
        if(pairCameraPosition == null){
            pairCameraPosition = profileService.getPairCameraPosition(camera);
        }

        Position position = profileService.getPositionOfCameraOfProfile(camera);

        LOGGER.debug("entering processPreview method for position {}", position);

        BufferedImage preview;


        boolean isGreenscreen = pairCameraPosition.isGreenScreenReady();
        boolean isFilter = "".equals(pairCameraPosition.getFilterName());

        if(isGreenscreen){
            String backGroundPath = pairCameraPosition.getBackground().getPath();
            preview = null;
        } else if (isFilter){

            String filterName = pairCameraPosition.getFilterName();
            preview = filterService.filter(filterName, imgPath);
            LOGGER.debug("processPreview - Filter {} for position {} added to preview image", filterName, position);
        }else {
            preview = imageHandler.openImage(imgPath);
            LOGGER.debug("processPreview - No filter or greenscreen detected for position {}", position);
        }

        shotFrameManager.refreshShot(camera.getId(), preview);
    }

    @Override
    public void processShot(Image image) throws ServiceException{
        String imgPath = image.getImagepath();
        if(pairCameraPosition == null){
            pairCameraPosition = profileService.getPairCameraPosition(camera);
        }

        Position position = profileService.getPositionOfCameraOfProfile(camera);

        LOGGER.debug("entering processShot method for position {}", position);

        BufferedImage shot;

        boolean isGreenscreen = pairCameraPosition.isGreenScreenReady();
        boolean isFilter = "".equals(pairCameraPosition.getFilterName());
        boolean logosEnabled = profileService.getAllPairLogoRelativeRectangle().isEmpty();

        if(isGreenscreen){
            String backGroundPath = pairCameraPosition.getBackground().getPath();
            shot = null;
        } else if (isFilter){
            shot = imageHandler.openImage(imgPath);

            if(logosEnabled){
                logoWatermarkService.addLogosToImage(shot);
                imageHandler.saveImage(shot, imgPath);  //TODO: Change if cameraHandlerThread stores img in temp folder
                LOGGER.debug("processShot - {} overwritten with logo image before filtering");
            }

            String filterName = pairCameraPosition.getFilterName();
            shot = filterService.filter(filterName, imgPath);
            LOGGER.debug("processShot - Filter {} for position {} added to shot", filterName, position);

        }else {
            shot = imageHandler.openImage(imgPath);
        }

        logoWatermarkService.addLogosToImage(shot);
        shotFrameManager.refreshShot(camera.getId(), shot);



        Image filteredImage = null;
        if(isFilter){
            //Save filtered image in same path as original path
            String fileEnding = imgPath.substring(imgPath.lastIndexOf('.') + 1);
            String directoryAndName = imgPath.substring(0, imgPath.lastIndexOf('.'));
            String newImgPath = directoryAndName + "_" + pairCameraPosition.getFilterName() + fileEnding;
            imageHandler.saveImage(shot, newImgPath);
            LOGGER.debug("processShot - filtered image saved");

            //Persist filtered image in database
            Shooting activeShooting = shootingService.searchIsActive();
            if(activeShooting == null){
                LOGGER.error("processShot - No active shooting during saving of filtered image");
                throw new ServiceException("No active Shooting");
            }
            filteredImage = new Image(newImgPath, activeShooting.getId());
            imageService.create(filteredImage);
            LOGGER.debug("processShot - filteredImage {} persistet in database", filteredImage);

        } else {
            imageHandler.saveImage(shot, imgPath);
            LOGGER.debug("processShot - processed image saved");
        }

        //Refresh with processed image
        refreshManager.refreshFrames(image);

        //refresh with filtered image
        if(isFilter){
            refreshManager.refreshFrames(filteredImage);
        }

    }
}
