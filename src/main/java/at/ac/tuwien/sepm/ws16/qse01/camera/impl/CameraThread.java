package at.ac.tuwien.sepm.ws16.qse01.camera.impl;

import at.ac.tuwien.sepm.ws16.qse01.service.FilterService;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraThread extends Thread{

    private Logger LOGGER = LoggerFactory.getLogger(CameraThread.class);
    private ImageService imageService;
    private ShootingService shootingService;
    private CameraGphoto cameraGphoto;
    private Camera camera;
    private ImageProcessor imageProcessor;
    private ShotFrameController shotFrameController;

    private boolean shouldStop = false;
    private boolean takeImage = false;
    private boolean serieShot = false;
    private boolean countdown = false;

    @Override
    public void run()
    {
        while(true)
        {
            if(takeImage)
            {
                if(countdown)
                {
                    //TODO: shotFrameController.showCountdown(10)
                }
                captureImage();
                if(serieShot)
                {
                    for (int i = 0; i < 5; i++)
                    {
                        captureImage();
                    }
                }
                takeImage=false;
            }
            else
            {
                capturePreview();
            }
            if(shouldStop)
            {
                LOGGER.debug("Stopped CameraThread for Camera {}", camera);
                return;
            }
        }
    }

    private void captureImage()
    {
        Image image;
        try
        {
            final CameraFile cf = cameraGphoto.captureImage();
            if (cf != null) {
                Shooting activeShooting = shootingService.searchIsActive();
                if(activeShooting != null)
                {
                    int imageID = imageService.getNextImageID();

                    String directoryPath = activeShooting.getStorageDir() + "/";

                    DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
                    Date date = new Date();
                    String imagePath = directoryPath + "K"+camera.getId()+ "_" + dateFormat.format(date) + ".jpg";
                    image = new Image(imageID, imagePath, activeShooting.getId(), new Date());
                    image = imageService.create(image);
                    cf.save(new File(imagePath).getAbsolutePath());

                    imageProcessor.processShot(image);

                    LOGGER.debug(imageService.getLastImgPath(activeShooting.getId()));
                }
                else
                {
                    LOGGER.error("no active shooting during capture");
                }
                cf.close();
            }
        }
        catch (CameraException ex)
        {
            LOGGER.debug("waitForImage failed", ex);
            setStop(true);
        }
        catch (ServiceException ex)
        {
            LOGGER.debug("Exception in service: {}", ex);
        }
    }

    private void capturePreview()
    {
        try
        {

            final CameraFile cf = cameraGphoto.capturePreview();
            if (cf != null)
            {
                Shooting activeShooting = shootingService.searchIsActive();
                if(activeShooting != null)
                {
                    String imagePath = activeShooting.getStorageDir() + "/tmp/K" + +camera.getId()+".jpg";
                    cf.save(new File(imagePath).getAbsolutePath());
                    imageProcessor.processPreview(imagePath);

                    LOGGER.debug(imageService.getLastImgPath(activeShooting.getId()));
                }
                else
                {
                    LOGGER.error("no active shooting during capture");
                }
                cf.close();

            }
        }
        catch (CameraException ex)
        {
            LOGGER.debug("capturePreview failed" + ex);
            setStop(true);
        }
        catch (ServiceException ex)
        {
            LOGGER.debug("Exception in service: {}", ex);
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

    public void setCountdown(boolean countdown) {
        this.countdown = countdown;
    }

    public void setShootingService(ShootingService shootingService){
        this.shootingService = shootingService;
    }

    public void setShotFrameController(ShotFrameController shotFrameController){
        this.shotFrameController = shotFrameController;
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
}
