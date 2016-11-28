package at.ac.tuwien.sepm.ws16.qse01.camera;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import at.ac.tuwien.sepm.ws16.qse01.gui.MainFrameController;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.impl.ImageServiceImpl;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import at.ac.tuwien.sepm.ws16.qse01.camera.CameraFile.Path;
import at.ac.tuwien.sepm.ws16.qse01.camera.jna.GPhoto2Native;
import at.ac.tuwien.sepm.ws16.qse01.camera.jna.GPhoto2Native.CameraFilePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Closeable;
import java.io.File;
import java.util.Date;


public class CameraHandler {

    Logger LOGGER = LoggerFactory.getLogger(CameraHandler.class);
    ShotFrameController shotFrameController;
    private SpringFXMLLoader springFXMLLoader;
    ImageService imageService;
    //SessionService sessionService;
    int id=0;

    @Autowired
    public CameraHandler(ShotFrameController shotFrameController, ImageService imageService/*, SessionService sessionService*/)
    {
        this.shotFrameController=shotFrameController;
        this.imageService= imageService;
        //this.sessionService= sessionService;
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
    public void getImages() {

        final CameraList cl = new CameraList();
        try {
            LOGGER.info("Cameras: " + cl);
        } finally {
            CameraUtils.closeQuietly(cl);
        }
        final Camera c = new Camera();
        try {
            while(true) {

                c.initialize();
                final CameraFile cf = c.wait_for_image();
                int sessionID=1;
                String imagePath= "/images/image" + id++ + ".jpg";
                Image image= new Image(0,  imagePath, sessionID, new Date());
                Image image2=imageService.create(image);
                cf.save(new File("/images/image" + image2.getImageID() + ".jpg").getAbsolutePath());       //TODO: get imageID aus der Datenbank

                //shotFrameController.refreshShot();                                          //TODO: get shotFrameController
            }
        } finally {
            CameraUtils.closeQuietly(c);
        }
    }
}
