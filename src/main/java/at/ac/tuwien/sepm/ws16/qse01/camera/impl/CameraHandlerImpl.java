package at.ac.tuwien.sepm.ws16.qse01.camera.impl;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.application.ShotFrameManager;
import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;
import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;

import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraGphoto;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraList;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraUtils;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class CameraHandlerImpl implements CameraHandler {

    Logger LOGGER = LoggerFactory.getLogger(CameraHandlerImpl.class);
    ShotFrameManager shotFrameManager;
    private SpringFXMLLoader springFXMLLoader;
    ImageService imageService;
    ShootingService shootingService;
    List<CameraGphoto> cameraGphotoList=new ArrayList<CameraGphoto>();

    @Autowired
    public CameraHandlerImpl(ShotFrameManager shotFrameManager, ImageService imageService, ShootingService shootingService)
    {
        this.shotFrameManager=shotFrameManager;
        this.imageService= imageService;
        this.shootingService= shootingService;
    }

    /**
     * Saves image in images folder and in database.
     * Also tells the Shot monitor to refresh the image.
     *
     * */
    @Override
    public void getImages() throws CameraException {

        for(int i=0;i<cameraGphotoList.size();i++)
        {

            CameraHandlerThread cameraHandlerThread = new CameraHandlerThread();
            cameraHandlerThread.setImageService(imageService);
            cameraHandlerThread.setShotFrameManager(shotFrameManager);
            cameraHandlerThread.setShootingService(shootingService);
            cameraHandlerThread.setCameraGphoto(cameraGphotoList.get(i));
            cameraHandlerThread.start();
        }
    }

    @Override
    public List<Camera> getCameras() throws CameraException {
        int count=0;
        final CameraList cl = new CameraList();
        try {
            LOGGER.debug("Cameras: " + cl);

            count=cl.getCount();

        } finally {
            CameraUtils.closeQuietly(cl);
        }
        //final CameraGphoto c = new CameraGphoto();
        for(int i=0;i<count;i++)
        {
            cameraGphotoList.add(new CameraGphoto());
            cameraGphotoList.get(i).initialize();
            cameraGphotoList.get(i).ref();
        }
        return null;
    }
}
