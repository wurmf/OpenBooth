package at.ac.tuwien.sepm.ws16.qse01.camera.impl;

import at.ac.tuwien.sepm.ws16.qse01.application.ShotFrameManager;
import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;

import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraFile;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraGphoto;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraList;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraUtils;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraHandlerThread  extends Thread{

    Logger LOGGER = LoggerFactory.getLogger(CameraHandlerThread.class);
    ImageService imageService;
    ShootingService shootingService;
    ShotFrameManager shotFrameManager;
    CameraGphoto cameraGphoto;
    Camera camera;


    public void run()
    {
        int i = 1;
        boolean imageSaved = false;
        // Anmerkung: Manueller Test
      /*  shotFrameManager.refreshShot(1,"/images/shooting1/img1.jpg");
        shotFrameManager.refreshShot(2,"/images/shooting1/img2.jpg");
        System.out.println("ende refreshing..");*/

        while (i == 1)
        {
            Image image=null;
            try {

                final CameraFile cf = cameraGphoto.waitForImage();
                if (cf != null) {
                    Shooting activeShooting = shootingService.searchIsActive();
                    if(activeShooting != null){
                        int imageID = imageService.getNextImageID();

                        String directoryPath = activeShooting.getStorageDir() + "/shooting" + activeShooting.getId() + "/";
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
                        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
                        Date date = new Date();
                        String imagePath = directoryPath + "K"+camera.getId()+ "_" + dateFormat.format(date) + ".jpg";
                        image = new Image(imageID, imagePath, activeShooting.getId(), new Date());
                        image = imageService.create(image);

                        cf.save(new File(imagePath).getAbsolutePath());

                        imageSaved=true;


                        LOGGER.debug(image.getImageID() + "");
                        LOGGER.debug(imageService.getLastImgPath(activeShooting.getId()));
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
                shotFrameManager.refreshShot(camera.getId(), image.getImagepath());
            }
            imageSaved=false;
        }
        CameraUtils.closeQuietly(cameraGphoto);
    }

    public static void main(String[] args) {
        (new Thread(new CameraHandlerThread())).start();
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public void setShotFrameManager(ShotFrameManager shotFrameManager) {
        this.shotFrameManager = shotFrameManager;
    }

    public void setShootingService(ShootingService shootingService){
        this.shootingService = shootingService;
    }

    public void setCameraGphoto(CameraGphoto cameraGphoto) {
        this.cameraGphoto = cameraGphoto;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
