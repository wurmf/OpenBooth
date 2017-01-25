package at.ac.tuwien.sepm.ws16.qse01.camera.impl;

import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraUtils;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import at.ac.tuwien.sepm.ws16.qse01.service.ProfileService;
import at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing.ImageProcessor;
import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;

import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraFile;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraGphoto;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.ws16.qse01.entities.Shooting;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CameraThread extends Thread{

    private Logger LOGGER = LoggerFactory.getLogger(CameraThread.class);
    private ImageService imageService;
    private ShootingService shootingService;
    private CameraGphoto cameraGphoto;
    private Camera camera;
    private ImageProcessor imageProcessor;
    private ShotFrameController shotFrameController;
    private ProfileService profileService;

    private boolean shouldStop = false;
    private boolean takeImage = false;
    private boolean serieShot = false;
    private int countdown = 0;

    private String tempStorage;

    @Override
    public void run()
    {
        createTempDir();
        while(!shouldStop)
        {
            if(takeImage)
            {
                if(countdown!=0)
                {
                    shotFrameController.showCountdown(countdown);
                    while (!shotFrameController.isExpired()){
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
            if(shouldStop)
            {
                try
                {
                    cameraGphoto.close();
                }
                catch (IOException e)
                {
                    LOGGER.error("cameraThread run, could not close Camera", e);
                }
                LOGGER.debug("Stopped CameraThread for Camera {}", camera);
            }
        }
    }


    private void captureImage() {

        Shooting activeShooting;
        try {
            activeShooting = shootingService.searchIsActive();
        } catch (ServiceException e) {
            LOGGER.error("captureImage - error in shootingservice -", e);
            return;
        }

        if (activeShooting == null) {
            LOGGER.error("captureImage - no active shooting during capture");
            return;
        }

        Image image;
        int anz = 1;
        List<Image> imageList = new ArrayList<>();
        if (serieShot) {
            anz = 5;
        }

        for (int i = 0; i < anz; i++) {

            CameraFile cf;
            try {
                cf = cameraGphoto.captureImage();
            } catch (CameraException ex) {
                LOGGER.error("captureImage - capture failed", ex);
                setStop(true);
                return;
            }

            if (cf == null) {
                LOGGER.error("captureImage - camerafile is null");
                return;
            }


            String directoryPath = activeShooting.getStorageDir();

            DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
            Date date = new Date();
            String imagePath = directoryPath + "/K" + camera.getId() + "_" + dateFormat.format(date) + ".jpg";

            try {
                cf.save(new File(imagePath).getAbsolutePath());
            } catch (CameraException e) {
                LOGGER.error("captureImage - save image failed, should be saved to {}", imagePath, e);
                setStop(true);
                return;
            } finally {
                CameraUtils.closeQuietly(cf);
            }


            try {
                image = new Image(imagePath, activeShooting.getId());
                image = imageService.create(image);
                imageList.add(image);
            } catch (ServiceException e) {
                LOGGER.error("captureImage - exception in service", e);
            }

        }

        for (Image shot : imageList) {
            try {
                imageProcessor.processShot(shot);
                sleep(5000);
            } catch (ServiceException e) {
                LOGGER.error("captureImage - exception in service", e);
            } catch (InterruptedException e){
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

        String imagePath = tempStorage + "/K" + +camera.getId()+".jpg";

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
        } catch (ServiceException e) {
            //Ignore Exception if thread should be stopped
            if(!shouldStop){
                LOGGER.error("capturePreview - exception in service", e);
            }
        }
    }

    /*private void waitForEvent()
    {
        LOGGER.info("Camera Thread for Camera {} started", camera);
        int i = 1;
        boolean imageSaved = false;

        while (i == 1)
        {
            Image image=null;
            try {

                final CameraFile cf = cameraGphoto.waitForImage();
                if (cf != null) {
                    Shooting activeShooting = shootingService.searchIsActive();
                    if(activeShooting != null){
                        int imageID = imageService.getNextImageID();

                        String directoryPath = activeShooting.getStorageDir() + "/";

                        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
                        Date date = new Date();
                        String imagePath = directoryPath + "K"+camera.getId()+ "_" + dateFormat.format(date) + ".jpg";
                        image = new Image(imageID, imagePath, activeShooting.getId(), new Date());
                        image = imageService.create(image);
                        cf.save(new File(imagePath).getAbsolutePath());

                        imageSaved=true;

                        LOGGER.debug(imageService.getLastImgPath(activeShooting.getId()));
                    }else{
                        LOGGER.error("no active shooting during capture");
                    }
                    cf.close();

                }


            } catch (CameraException ex) {
                LOGGER.debug("waitForImage failed" + ex);
                return;
            } catch (ServiceException ex) {
                LOGGER.debug("Exception in service: {}", ex);
            }

            if(shouldStop){
                LOGGER.debug("Stopped CameraThread for Camera {}", camera);
                return;
            }

            if(imageSaved)
            {
                try {
                    imageProcessor.processShot(image);
                } catch (ServiceException e) {
                    LOGGER.error("An Error during the image processing occured", e);
                }
                imageSaved=false;
            }
        }
        CameraUtils.closeQuietly(cameraGphoto);
    }*/

    public static void main(String[] args) {
        (new Thread(new CameraThread())).start();
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public void setTakeImage(boolean takeImage) {
        this.takeImage = takeImage;
    }

    public void setSerieShot(boolean serieShot) {
        this.serieShot = serieShot;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public void setShootingService(ShootingService shootingService){
        this.shootingService = shootingService;
    }

    public void setShotFrameController(ShotFrameController shotFrameController){
        this.shotFrameController = shotFrameController;
    }

    public void setProfileService(ProfileService profileService) {
        this.profileService = profileService;
    }

    public void setCameraGphoto(CameraGphoto cameraGphoto) {
        this.cameraGphoto = cameraGphoto;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera(){
        return this.camera;
    }

    public void setImageProcessor(ImageProcessor imageProcessor){
        this.imageProcessor = imageProcessor;
    }

    public void setStop(boolean shouldStop){
        this.shouldStop = shouldStop;
    }

    private void createTempDir(){

        tempStorage = System.getProperty("user.home") + "/.fotostudio/tmp";

        Path tempStoragePath = Paths.get(tempStorage);
        if(tempStoragePath != null){

            try {
                Files.createDirectories(tempStoragePath);
            } catch (FileAlreadyExistsException e) {
                LOGGER.debug("createTempDir - temp directory already exists");
            } catch (IOException e) {
                LOGGER.error("createTempDir - Error creating temp directory", e);
            }
        }




    }
}
