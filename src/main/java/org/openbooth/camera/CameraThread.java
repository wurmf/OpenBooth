package org.openbooth.camera;

import org.openbooth.entities.Camera;
import org.openbooth.entities.Image;
import org.openbooth.entities.Shooting;
import org.openbooth.gui.ShotFrameController;
import org.openbooth.service.ImageService;
import org.openbooth.service.ShootingService;
import org.openbooth.service.exceptions.ServiceException;
import org.openbooth.service.imageprocessing.ImageProcessor;
import org.openbooth.util.TempStorageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class CameraThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(CameraThread.class);

    private ShotFrameController shotFrameController;
    private ImageProcessor imageProcessor;

    private static final int SHOW_SHOT_TIME_IN_MILLISECONDS = 2000;

    protected boolean shouldStop = false;
    private boolean takeImage = false;
    private boolean serieShot = false;
    private Camera camera;
    private int countdown = 0;

    private ImageService imageService;
    private ShootingService shootingService;
    private TempStorageHandler tempStorageHandler;

    protected CameraThread(ImageService imageService, ShootingService shootingService, TempStorageHandler tempStorageHandler){
        this.imageService = imageService;
        this.shootingService = shootingService;
        this.tempStorageHandler = tempStorageHandler;
    }

    @Override
    public void run()
    {
        checkInitialized();
        checkImplInitialized();

        while(!shouldStop)
        {
            if(takeImage)
            {
                checkAndShowCountdown();
                captureImage();
                takeImage=false;
            }
            else
            {
                capturePreview();
            }
        }
        LOGGER.debug("Stopped CameraThread for Camera {}", camera);
    }

    protected void captureImage()
    {

        Shooting activeShooting;
        try
        {
            activeShooting = shootingService.searchIsActive();
        }
        catch (ServiceException e)
        {
            LOGGER.error("captureImage - error in shootingservice -", e);
            return;
        }

        if (activeShooting == null)
        {
            LOGGER.error("captureImage - no active shooting during capture");
            return;
        }
        Image image;
        int anz = 1;
        List<Image> imageList = new ArrayList<>();
        if (serieShot)
        {
            anz = 5;
        }

        for (int i = 0; i < anz; i++)
        {

            String directoryPath = activeShooting.getStorageDir();

            if(directoryPath == null || directoryPath.isEmpty())
            {
                LOGGER.error("captureImage - shooting directory path is null or empty");
                shouldStop = true;
                return;
            }

            DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
            Date date = new Date();
            String imagePath = new File(directoryPath + "/K" + camera.getId() + "_" + dateFormat.format(date) + ".jpg").getAbsolutePath();

            captureAndTransferImageFromCamera(imagePath);

            try
            {
                image = new Image(imagePath, activeShooting.getId());
                image = imageService.create(image);
                imageList.add(image);
            }
            catch (ServiceException e)
            {
                LOGGER.error("captureImage - exception in service", e);
            }

        }

        for (Image shot : imageList)
        {
            try
            {
                imageProcessor.processShot(shot);
                sleep(SHOW_SHOT_TIME_IN_MILLISECONDS);
            }
            catch (ServiceException e)
            {
                LOGGER.error("captureImage - exception in service", e);
            }
            catch (InterruptedException e)
            {
                LOGGER.error("captureImage - exception during showing of shot", e);
                return;
            }
        }
    }

    private void capturePreview()
    {

        String imagePath = new File(tempStorageHandler.getTempStoragePath() + "/K" + +camera.getId()+".jpg").getAbsolutePath();

        captureAndTransferPreviewFromCamera(imagePath);

        try {
            imageProcessor.processPreview(imagePath);
        } catch (ServiceException | NullPointerException e) {
            //Ignore Exception if thread should be stopped
            if(!shouldStop){
                LOGGER.error("capturePreview - exception in service", e);
            }
        }
    }

    public void setTakeImage(boolean takeImage){
        this.takeImage = takeImage;
    }

    public void setSerieShot(boolean serieShot){
        this.serieShot = serieShot;
        this.countdown = 0;
    }

    public void setCountdown(int countdown){
        this.countdown = countdown;
        this.serieShot = false;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera(){
        return this.camera;
    }

    public void setStop(boolean shouldStop){
        this.shouldStop = shouldStop;
    }

    public void setShotFrameController(ShotFrameController shotFrameController){
        this.shotFrameController = shotFrameController;
    }

    public void setImageProcessor(ImageProcessor imageProcessor){
        this.imageProcessor = imageProcessor;
    }

    protected abstract void captureAndTransferImageFromCamera(String imagePath);

    protected abstract void captureAndTransferPreviewFromCamera(String imagePath);

    private void checkInitialized(){
        if(imageService == null || shootingService == null || tempStorageHandler == null || camera == null || shotFrameController == null || imageProcessor == null){
            LOGGER.error("checkInitialized - A dependency of Camerathread is null, maybe the constructor or a setter was not called?");
            shouldStop = true;
        }
    }

    private void checkAndShowCountdown(){
        if(countdown != 0){
            shotFrameController.showCountdown(countdown);
            while (!shotFrameController.isExpired())
            {
                capturePreview();
            }
        }
    }

    protected abstract void checkImplInitialized();



}
