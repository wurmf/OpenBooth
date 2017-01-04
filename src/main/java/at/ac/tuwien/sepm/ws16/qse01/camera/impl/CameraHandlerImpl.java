package at.ac.tuwien.sepm.ws16.qse01.camera.impl;

import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import at.ac.tuwien.sepm.ws16.qse01.application.ShotFrameManager;
import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;
import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;

import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraGphoto;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraList;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraUtils;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.gui.RefreshManager;
import at.ac.tuwien.sepm.ws16.qse01.gui.ShotFrameController;
import at.ac.tuwien.sepm.ws16.qse01.service.CameraService;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;


@Component
public class CameraHandlerImpl implements CameraHandler {

    private Logger LOGGER = LoggerFactory.getLogger(CameraHandlerImpl.class);
    private ShotFrameManager shotFrameManager;
    private SpringFXMLLoader springFXMLLoader;
    private ImageService imageService;
    private ShootingService shootingService;
    private CameraService cameraService;
    private RefreshManager refreshManager;
    private List<CameraGphoto> cameraGphotoList=new ArrayList<CameraGphoto>();
    private List<String> cameraModelList = new ArrayList<>();
    private List<String> cameraPortList = new ArrayList<>();
    private List<Camera> cameraList = new ArrayList<>();

    @Autowired
    public CameraHandlerImpl(ShotFrameManager shotFrameManager, ImageService imageService, ShootingService shootingService, CameraService cameraService, RefreshManager refreshManager)
    {
        this.shotFrameManager=shotFrameManager;
        this.imageService= imageService;
        this.shootingService= shootingService;
        this.cameraService=cameraService;
        this.refreshManager=refreshManager;
    }

    /**
     * Saves image in images folder and in database.
     * Also tells the Shot monitor to refresh the image.
     *
     * */
    @Override
    public void getImages() throws CameraException {

        Camera camera;
        for(int i=0;i<cameraGphotoList.size();i++) {
            camera=cameraList.get(i);
            CameraHandlerThread cameraHandlerThread = new CameraHandlerThread();
            cameraHandlerThread.setImageService(imageService);
            cameraHandlerThread.setShotFrameManager(shotFrameManager);
            cameraHandlerThread.setShootingService(shootingService);
            cameraHandlerThread.setCameraGphoto(cameraGphotoList.get(i));
            cameraHandlerThread.setCamera(camera);
            cameraHandlerThread.setRefreshManager(refreshManager);
            cameraHandlerThread.start();
        }
    }

    @Override
    public List<Camera> getCameras() throws CameraException {
        try {
            cameraService.setAllCamerasInactive();
        } catch (ServiceException e) {
            LOGGER.debug("could not set cameras inactive");
        }
        int count=0;
        final CameraList cl = new CameraList();
        try {
            LOGGER.debug("Cameras: " + cl);

            count=cl.getCount();
            Camera camera;
            for(int i=0;i<count;i++)
            {
                cameraGphotoList.add(new CameraGphoto());
                Pointer pInfo=cl.getPortInfo(i);
                cameraPortList.add(cl.getPort(i));
                cameraModelList.add(cl.getModel(i));
                LOGGER.debug(pInfo.toString());

                cameraGphotoList.get(i).setPortInfo(cl.getPortPath(cl.getPort(i,true)));

                cameraGphotoList.get(i).initialize();
                cameraGphotoList.get(i).ref();
                try{
                    camera = new Camera(-1, "Kamera " + i, cameraPortList.get(i), cameraModelList.get(i), "Seriennummer: "+i);
                    camera=cameraService.cameraExists(camera);
                    if(camera==null)
                    {
                        camera = new Camera(-1, "Kamera " + i, cameraPortList.get(i), cameraModelList.get(i), "Seriennummer: "+i);
                        cameraService.createCamera(camera);
                    }
                    cameraService.setCameraActive(camera.getId());
                    cameraList.add(camera);
                }
                catch (ServiceException ex)
                {
                    LOGGER.error("Could not create camera entity");
                    throw new CameraException(ex.getMessage(), -1);
                }
            }

        } finally {
            CameraUtils.closeQuietly(cl);
        }
        return null;
    }
}
