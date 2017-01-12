package at.ac.tuwien.sepm.util.imageprocessing.impl;

import at.ac.tuwien.sepm.util.imageprocessing.ImageProcessor;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.service.FilterService;
import at.ac.tuwien.sepm.ws16.qse01.service.LogoWatermarkService;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;

/**
 * Created by fabian on 12.01.17.
 */
public class ImageProcessorImpl implements ImageProcessor {

    private ShootingService shootingService;
    private ProfileService profileService;
    private LogoWatermarkService logoWatermarkService;
    private FilterService filterService;
    //private GreenscreenService greenscreenService;

    private Camera camera;


    public ImageProcessorImpl(ShootingService shootingService, ProfileService profileService, LogoWatermarkService logoWatermarkService, FilterService filterService, Camera camera){
        this.shootingService = shootingService;
        this.profileService = profileService;
        this.logoWatermarkService = logoWatermarkService;
        this.filterService = filterService;
        this.camera = camera;
    }

    @Override
    public void processPreview(String imgPath) {


    }

    @Override
    public void processShot(String imgPath) {

    }
}
