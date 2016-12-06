package at.ac.tuwien.sepm.ws16.qse01.camera.impl;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;
import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;

import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CameraHandlerImpl implements CameraHandler {

    Logger LOGGER = LoggerFactory.getLogger(CameraHandlerImpl.class);
    ShotFrameController shotFrameController;
    private SpringFXMLLoader springFXMLLoader;
    ImageService imageService;
    ShootingService shootingService;

    @Autowired
    public CameraHandlerImpl(ShotFrameController shotFrameController, ImageService imageService, ShootingService shootingService)
    {
        this.shotFrameController=shotFrameController;
        this.imageService= imageService;
        this.shootingService= shootingService;
    }

    /**
     * Saves image in images folder and in database.
     * Also tells the Shot monitor to refresh the image.
     *
     * TODO: get Imageservice and Sessionservice
     *
     * @return
     *
     * */
    public void getImages() throws CameraException {

       /* final CameraList cl = new CameraList();
        try {
            LOGGER.debug("Cameras: " + cl);
        } finally {
            CameraUtils.closeQuietly(cl);
        }
        final CameraGphoto c = new CameraGphoto();
        int i = 1;
        boolean imageSaved = false;
        while (i == 1)
        {
            try {
                c.initialize();
                final CameraFile cf = c.waitForImage();
                if (cf != null) {
                    int shootngID = 1;
                    String imagePath = "/images/shooting1/img" + id++ + ".jpg";
                    Image image = new Image(0, imagePath, shootngID, new Date());
                    Image image2 = imageService.create(image);

                    cf.save(new File(System.getProperty("user.dir") + "/src/main/resources/images/shooting1/img" + image2.getImageID() + ".jpg").getAbsolutePath());       //TODO: get imageID aus der Datenbank
                    CameraUtils.closeQuietly(cf);
                    imageSaved=true;
                    LOGGER.debug(image2.getImageID() + "");
                    LOGGER.debug(imageService.getLastImgPath(shootngID));
                }

            } catch (CameraException ex) {
                LOGGER.debug("waitForImage Timeout");
                CameraUtils.closeQuietly(c);
                //throw new CameraException(ex.getMessage(), ex.getResult());
            }
            if(imageSaved)
            {
                shotFrameController.refreshShot();
            }
            imageSaved=false;
        }
        CameraUtils.closeQuietly(c);*/


        CameraHandlerThread cameraHandlerThread = new CameraHandlerThread();
        cameraHandlerThread.setImageService(imageService);
        cameraHandlerThread.setShotFrameController(shotFrameController);
        cameraHandlerThread.setShootingService(shootingService);
        cameraHandlerThread.start();
    }

}
