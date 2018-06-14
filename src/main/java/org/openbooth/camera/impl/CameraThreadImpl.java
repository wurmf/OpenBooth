package org.openbooth.camera.impl;

import org.openbooth.camera.CameraThread;
import org.openbooth.camera.libgphoto2java.CameraUtils;
import org.openbooth.camera.exeptions.CameraException;

import org.openbooth.camera.libgphoto2java.CameraFile;
import org.openbooth.camera.libgphoto2java.CameraGphoto;
import org.openbooth.entities.Image;
import org.openbooth.entities.Shooting;
import org.openbooth.service.ImageService;
import org.openbooth.service.ShootingService;
import org.openbooth.service.exceptions.ServiceException;
import org.openbooth.util.TempStorageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Scope("prototype")
public class CameraThreadImpl extends CameraThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(CameraThreadImpl.class);

    private ImageService imageService;
    private ShootingService shootingService;
    private TempStorageHandler tempStorageHandler;

    private CameraGphoto cameraGphoto;

    @Autowired
    public CameraThreadImpl(ImageService imageService, ShootingService shootingService, TempStorageHandler tempStorageHandler){
        this.imageService = imageService;
        this.shootingService = shootingService;
        this.tempStorageHandler = tempStorageHandler;
    }

    @Override
    public void run()
    {
        if(!checkInitialized())
        {
            LOGGER.error("CameraThread not properly initialized");
            shouldStop = true;
        }
        while(!shouldStop)
        {
            if(takeImage)
            {
                if(countdown!=0)
                {
                    shotFrameController.showCountdown(countdown);
                    while (!shotFrameController.isExpired())
                    {
                        capturePreview();
                    }
                }
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


    private void captureImage()
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

            CameraFile cf;
            try
            {
                cf = cameraGphoto.captureImage();
            }
            catch (CameraException ex)
            {
                LOGGER.error("captureImage - capture failed", ex);
                setStop(true);
                return;
            }

            if (cf == null)
            {
                LOGGER.error("captureImage - camerafile is null");
                return;
            }
            String directoryPath = activeShooting.getStorageDir();

            if(directoryPath == null || directoryPath.isEmpty())
            {
                LOGGER.error("captureImage - shooting directory path is null or empty");
                shouldStop = true;
                return;
            }

            DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
            Date date = new Date();
            String imagePath = directoryPath + "/K" + camera.getId() + "_" + dateFormat.format(date) + ".jpg";

            try
            {
                cf.save(new File(imagePath).getAbsolutePath());
            }
            catch (CameraException e)
            {
                LOGGER.error("captureImage - save image failed, should be saved to {}", imagePath, e);
                setStop(true);
                return;
            }
            finally
            {
                CameraUtils.closeQuietly(cf);
            }
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
                sleep(2000);
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
        final CameraFile cf;
        try {
            cf = cameraGphoto.capturePreview();
        } catch (CameraException e){
            LOGGER.error("capturePreview - capture failed", e);
            setStop(true);
            return;
        }

        if (cf == null) {
            LOGGER.error("capturePreview - camerafile is null");
            return;
        }

        String imagePath = tempStorageHandler.getTempStoragePath() + "/K" + +camera.getId()+".jpg";

        try {
            cf.save(new File(imagePath).getAbsolutePath());
        } catch (CameraException e) {
            LOGGER.error("capturePreview - save image failed, should be saved to {}", imagePath, e);
            setStop(true);
        } finally {
            CameraUtils.closeQuietly(cf);
        }

        try {
            imageProcessor.processPreview(imagePath);
        } catch (ServiceException | NullPointerException e) {
            //Ignore Exception if thread should be stopped
            if(!shouldStop){
                LOGGER.error("capturePreview - exception in service", e);
            }
        }
    }




    void setCameraGphoto(CameraGphoto cameraGphoto) {
        this.cameraGphoto = cameraGphoto;
    }



    private boolean checkInitialized(){
        return cameraGphoto != null && camera != null && imageProcessor != null && shotFrameController != null;

    }
}
