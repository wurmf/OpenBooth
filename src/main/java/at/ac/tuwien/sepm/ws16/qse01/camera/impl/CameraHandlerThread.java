package at.ac.tuwien.sepm.ws16.qse01.camera.impl;

import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraFile;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraGphoto;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraList;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraUtils;
import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;

/**
 * Created by osboxes on 03.12.16.
 */
public class CameraHandlerThread  extends Thread{

    Logger LOGGER = LoggerFactory.getLogger(CameraHandlerThread.class);
    ImageService imageService;
    ShotFrameController shotFrameController;
    int id;

    public void run()
    {
        final CameraList cl = new CameraList();
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
        CameraUtils.closeQuietly(c);
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public void setShotFrameController(ShotFrameController shotFrameController) {
        this.shotFrameController = shotFrameController;
    }

    public void setId(int id) {
        this.id = id;
    }
}
