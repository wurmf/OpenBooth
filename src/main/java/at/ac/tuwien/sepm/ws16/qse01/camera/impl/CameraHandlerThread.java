package at.ac.tuwien.sepm.ws16.qse01.camera.impl;

import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraFile;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraGphoto;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraList;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraUtils;
import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Created by osboxes on 03.12.16.
 */
public class CameraHandlerThread  extends Thread{

    Logger LOGGER = LoggerFactory.getLogger(CameraHandlerThread.class);
    ImageService imageService;
    ShootingService shootingService;
    ShotFrameController shotFrameController;


    public void run()
    {
        int i = 1;
        boolean imageSaved = false;
        final CameraList cl = new CameraList();
        try {
            LOGGER.debug("Cameras: " + cl);
        } finally {
            CameraUtils.closeQuietly(cl);
        }
        final CameraGphoto c = new CameraGphoto();
        c.initialize();
        while (i == 1)
        {
            try {

                final CameraFile cf = c.waitForImage();
                LOGGER.debug("event");
                if (cf != null) {
                    Shooting activeShooting = shootingService.search_isactive();
                    if(activeShooting != null){
                        int imageID = imageService.getNextImageID();

                        String directoryPath = activeShooting.getStorageFile() + "/shooting" + activeShooting.getPropertyId() + "/";
                        Path storageDir = Paths.get(directoryPath);
                        try {
                            Files.createDirectory(storageDir);
                            LOGGER.info("directory created \n {} \n", storageDir);
                        } catch (FileAlreadyExistsException e) {
                            LOGGER.info("Directory " + e.getMessage() + " already exists \n");
                        } catch (IOException e){
                            LOGGER.error("error creating directory " + e.getMessage() + "\n");
                            throw new ServiceException("error creating directory " + e.getMessage() + "\n");
                        }

                        String imagePath = directoryPath + "img" + imageID;
                        Image image = new Image(imageID, imagePath, activeShooting.getPropertyId(), new Date());
                        image = imageService.create(image);

                        cf.save(new File(imagePath).getAbsolutePath());

                        imageSaved=true;


                        LOGGER.debug(image.getImageID() + "");
                        LOGGER.debug(imageService.getLastImgPath(activeShooting.getPropertyId()));
                    }else{
                        LOGGER.debug("no active shooting during capture");
                    }
                    cf.close();

                }

            } catch (CameraException ex) {
                LOGGER.debug("waitForImage Timeout");
                //throw new CameraException(ex.getMessage(), ex.getResult());
            } catch (ServiceException ex) {
                LOGGER.debug("Exception in service: {}", ex.getMessage());
            }
            if(imageSaved)
            {
                shotFrameController.refreshShot();
            }
            imageSaved=false;
        }
        CameraUtils.closeQuietly(c);
    }

    public static void main(String[] args) {
        (new Thread(new CameraHandlerThread())).start();
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public void setShotFrameController(ShotFrameController shotFrameController) {
        this.shotFrameController = shotFrameController;
    }

    public void setShootingService(ShootingService shootingService){
        this.shootingService = shootingService;
    }

}
