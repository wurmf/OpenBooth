package at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing.impl;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.util.exceptions.ImageHandlingException;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing.ImageProcessor;
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

    private ShotFrameController shotFrameController;

    private ShootingService shootingService;
    private ProfileService profileService;
    private ImageService imageService;

    private LogoWatermarkService logoWatermarkService;
    private FilterService filterService;
    private GreenscreenService greenscreenService;

    private Position position;
    private Profile.PairCameraPosition pairCameraPosition;

    private ImageHandler imageHandler;
    private RefreshManager refreshManager;


    public ImageProcessorImpl(ShotFrameController shotFrameController, ShootingService shootingService, ProfileService profileService, ImageService imageService, LogoWatermarkService logoWatermarkService, FilterService filterService, GreenscreenService greenscreenService, Position position, ImageHandler imageHandler, RefreshManager refreshManager){
        this.shotFrameController = shotFrameController;

        this.shootingService = shootingService;
        this.profileService = profileService;
        this.imageService = imageService;

        this.logoWatermarkService = logoWatermarkService;
        this.filterService = filterService;
        this.greenscreenService = greenscreenService;

        this.position = position;

        this.imageHandler = imageHandler;
        this.refreshManager = refreshManager;
    }

    @Override
    public void processPreview(String imgPath) throws ServiceException{
        Camera camera = profileService.getCameraOfPositionOfProfile(position);
        pairCameraPosition = profileService.getPairCameraPosition(camera);



        LOGGER.debug("entering processPreview method for position {}", position);

        BufferedImage preview;

        String filterName = pairCameraPosition.getFilterName();

        boolean isGreenscreen = pairCameraPosition.isGreenScreenReady();
        boolean isFilter = !"".equals(filterName) && !"original".equals(filterName);

        if(isGreenscreen){
            Background background = pairCameraPosition.getBackground();

            preview =openImageThrowException(imgPath);

            if(background == null){
                LOGGER.debug("processPreview - greenscreen activated for position {} but no background set", position);
            }else {
                preview = greenscreenService.applyGreenscreen(preview, background);
                LOGGER.debug("processShot - Background {} applied to shot from position {}", background, position);
            }
        } else if (isFilter){

            preview = filterService.filter(filterName, imgPath);
            LOGGER.debug("processPreview - Filter {} for position {} added to preview image", filterName, position);
        }else {
            preview = openImageThrowException(imgPath);
            LOGGER.debug("processPreview - No filter or greenscreen detected for position {}", position);
        }

        shotFrameController.refreshShot(preview);
    }

    @Override
    public void processShot(Image image) throws ServiceException{
        String imgPath = image.getImagepath();
        Camera camera = profileService.getCameraOfPositionOfProfile(position);
        pairCameraPosition = profileService.getPairCameraPosition(camera);


        LOGGER.debug("entering processShot method for position {}", position);

        BufferedImage shot;

        String filterName = pairCameraPosition.getFilterName();

        boolean isGreenscreen = pairCameraPosition.isGreenScreenReady();
        boolean isFilter = !"".equals(filterName) && !"original".equals(filterName);

        if(isGreenscreen){
            Background background = pairCameraPosition.getBackground();

            shot = openImageThrowException(imgPath);

            if(background == null){
                LOGGER.debug("processShot - greenscreen activated for position {} but no background set", position);
            }else {
                shot = greenscreenService.applyGreenscreen(shot, background);
                LOGGER.debug("processShot - Background {} applied to shot from position {}", background, position);
            }
        } else if (isFilter){

            shot = saveUnfilterdImageAndApplyFilter(imgPath);

        }else {

            shot = openImageThrowException(imgPath);
        }

        logoWatermarkService.addLogosToImage(shot);
        shotFrameController.refreshShot(shot);


        Image filteredImage = null;
        if(isFilter){

            filteredImage = persistFilteredImage(imgPath, shot);

        } else {

            saveImageThrowException(shot, imgPath);
            LOGGER.debug("processShot - processed image saved");
        }

        //Refresh with processed image
        refreshManager.refreshFrames(image);

        //refresh with filtered image
        if(isFilter){
            refreshManager.refreshFrames(filteredImage);
        }

        LOGGER.info("processShot - shot {} processed", image);
    }

    private Image persistFilteredImage(String originalImgPath, BufferedImage shot) throws ServiceException{
        //Save filtered image in same path as original path
        String fileEnding = originalImgPath.substring(originalImgPath.lastIndexOf('.'));
        String directoryAndName = originalImgPath.substring(0, originalImgPath.lastIndexOf('.'));
        String newImgPath = directoryAndName + "_" + pairCameraPosition.getFilterName() + fileEnding;
        try {
            imageHandler.saveImage(shot, newImgPath);
        } catch (ImageHandlingException e) {
            throw new ServiceException(e);
        }
        LOGGER.debug("processShot - filtered image saved");

        //Persist filtered image in database
        Shooting activeShooting = shootingService.searchIsActive();
        if(activeShooting == null){
            LOGGER.error("processShot - No active shooting during saving of filtered image");
            throw new ServiceException("No active Shooting");
        }
        Image filteredImage = new Image(newImgPath, activeShooting.getId());
        imageService.create(filteredImage);
        LOGGER.debug("processShot - filteredImage {} persistet in database", filteredImage);
        return filteredImage;
    }

    private BufferedImage saveUnfilterdImageAndApplyFilter(String originalImgPath) throws ServiceException{
        BufferedImage shot;
        boolean logosEnabled = profileService.getAllPairLogoRelativeRectangle().isEmpty();

        shot = openImageThrowException(originalImgPath);

        if(logosEnabled){
            logoWatermarkService.addLogosToImage(shot);

            saveImageThrowException(shot, originalImgPath);     //Change if cameraHandlerThread stores img in temp folder
            LOGGER.debug("processShot - {} overwritten with logo image before filtering");
        }

        String filterName = pairCameraPosition.getFilterName();
        shot = filterService.filter(filterName, originalImgPath);
        LOGGER.debug("processShot - Filter {} for position {} added to shot", filterName, position);

        return shot;
    }

    private BufferedImage openImageThrowException(String imgPath) throws ServiceException{
        try {
            return imageHandler.openImage(imgPath);
        } catch (ImageHandlingException e) {
            throw new ServiceException(e);
        }
    }

    private void saveImageThrowException(BufferedImage image, String imgPath) throws ServiceException{
        try {
            imageHandler.saveImage(image, imgPath);
        } catch (ImageHandlingException e) {
            throw new ServiceException(e);
        }
    }
}
