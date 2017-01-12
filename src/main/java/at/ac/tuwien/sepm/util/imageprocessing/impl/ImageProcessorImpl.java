package at.ac.tuwien.sepm.util.imageprocessing.impl;

import at.ac.tuwien.sepm.util.ImageHandler;
import at.ac.tuwien.sepm.util.imageprocessing.ImageProcessor;
import at.ac.tuwien.sepm.ws16.qse01.application.ShotFrameManager;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.ws16.qse01.entities.Position;
import at.ac.tuwien.sepm.ws16.qse01.gui.RefreshManager;
import at.ac.tuwien.sepm.ws16.qse01.service.FilterService;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

import java.awt.image.BufferedImage;

/**
 * Created by fabian on 12.01.17.
 */
public class ImageProcessorImpl implements ImageProcessor {

    private ShotFrameManager shotFrameManager;
    private ShootingService shootingService;
    private ProfileService profileService;
    private LogoWatermarkService logoWatermarkService;
    private FilterService filterService;
    //private GreenscreenService greenscreenService;

    private Camera camera;
    private Position position = null;

    private ImageHandler imageHandler;
    private RefreshManager refreshManager;


    public ImageProcessorImpl(ShotFrameManager shotFrameManager, ShootingService shootingService, ProfileService profileService, LogoWatermarkService logoWatermarkService, FilterService filterService, Camera camera, ImageHandler imageHandler, RefreshManager refreshManager){
        this.shotFrameManager = shotFrameManager;
        this.shootingService = shootingService;
        this.profileService = profileService;
        this.logoWatermarkService = logoWatermarkService;
        this.filterService = filterService;

        this.camera = camera;

        this.imageHandler = imageHandler;
        this.refreshManager = refreshManager;
    }

    @Override
    public void processPreview(String imgPath) throws ServiceException{
        if(position == null){
            position = profileService.getPositionOfCameraOfProfile(camera);
        }

        BufferedImage preview;


        boolean isGreenscreen = true;
        boolean isFilter = true;

        if(isGreenscreen){
            String backGroundPath = "";
            preview = null;
        } else if (isFilter){
            String filterName = "grayscale"; //TODO: get from profile
            preview = filterService.filter(filterName, imgPath);
        }else {
            preview = imageHandler.openImage(imgPath);
        }

        shotFrameManager.refreshShot(camera.getId(), preview);
    }

    @Override
    public void processShot(Image image) throws ServiceException{
        String imgPath = image.getImagepath();
        if(position == null){
            position = profileService.getPositionOfCameraOfProfile(camera);
        }

        BufferedImage shot;

        boolean isGreenscreen = true;
        boolean isFilter = true;

        if(isGreenscreen){
            String backGroundPath = "test";
            shot = null;
        } else if (isFilter){
            shot = imageHandler.openImage(imgPath);
            logoWatermarkService.addLogosToImage(shot);
            imageHandler.saveImage(shot, imgPath);  //TODO: Change if cameraHandlerThread stores img in temp folder

            String filterName = "grayscale"; //TODO: get from profile
            shot = filterService.filter(filterName, imgPath);
        }else {
            shot = imageHandler.openImage(imgPath);
        }

        logoWatermarkService.addLogosToImage(shot);
        shotFrameManager.refreshShot(camera.getId(), shot);
        imageHandler.saveImage(shot, "newnaem");    //TODO: add real name
        //TODO: persist image in db

        refreshManager.refreshFrames(image);
        refreshManager.refreshFrames(image); //TODO: change if image has been persisted
    }
}
