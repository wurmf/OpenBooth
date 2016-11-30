package at.ac.tuwien.sepm.ws16.qse01.camera.impl;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Date;


public class CameraHandlerImpl implements CameraHandler {

    Logger LOGGER = LoggerFactory.getLogger(CameraHandlerImpl.class);
    ShotFrameController shotFrameController;
    private SpringFXMLLoader springFXMLLoader;
    ImageService imageService;
    //SessionService sessionService;
    int id=0;

    @Autowired
    public CameraHandlerImpl(ShotFrameController shotFrameController, ImageService imageService/*, SessionService sessionService*/)
    {
        this.shotFrameController=shotFrameController;
        this.imageService= imageService;
        //this.sessionService= sessionService;
    }
    public CameraHandlerImpl()
    {
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
    public void getImages() throws CameraException{

        final CameraList cl = new CameraList();
        try {
            LOGGER.debug("Cameras: " + cl);
        } finally {
            CameraUtils.closeQuietly(cl);
        }
        final CameraGphoto c = new CameraGphoto();
        int i=1;
        try {
            while(i==1) {

                c.initialize();
                final CameraFile cf = c.waitForImage();
                if(cf!=null)
                {
                    int sessionID = 1;
                    String imagePath = "/images/image" + id++ + ".jpg";
                    Image image = new Image(0, imagePath, sessionID, new Date());
                    //Image image2=imageService.create(image);
                    cf.save(new File("image" + 1 + ".jpg").getAbsolutePath());       //TODO: get imageID aus der Datenbank
                    //LOGGER.debug(image2.getImageID()+ "");
                    //shotFrameController.refreshShot();
                }
            }
        }
        catch(CameraException ex)
        {
            LOGGER.debug("waitForImage Timeout");
            //throw new CameraException(ex.getMessage(), ex.getResult());
        }
        finally {
            CameraUtils.closeQuietly(c);
        }
    }
}
