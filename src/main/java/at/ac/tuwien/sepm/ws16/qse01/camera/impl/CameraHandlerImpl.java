package at.ac.tuwien.sepm.ws16.qse01.camera.impl;

import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;
import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;

import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraGphoto;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraList;
import at.ac.tuwien.sepm.ws16.qse01.camera.libgphoto2java.CameraUtils;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;
import at.ac.tuwien.sepm.ws16.qse01.service.CameraService;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class CameraHandlerImpl implements CameraHandler {

    private Logger LOGGER = LoggerFactory.getLogger(CameraHandlerImpl.class);

    private CameraService cameraService;
    private List<CameraGphoto> cameraGphotoList=new ArrayList<CameraGphoto>();
    private List<String> cameraModelList = new ArrayList<>();
    private List<String> cameraPortList = new ArrayList<>();
    private List<Camera> cameraList = new ArrayList<>();

    @Autowired
    public CameraHandlerImpl(CameraService cameraService)
    {
        this.cameraService=cameraService;
    }

    /**
     * Saves image in images folder and in database.
     * Also tells the Shot monitor to refresh the image.
     *
     * */
    @Override
    public List<CameraThread> createThreads() throws CameraException {

        Camera camera;
        List<CameraThread> threadList = new ArrayList<>();
        for(int i=0;i<cameraGphotoList.size();i++) {
            camera=cameraList.get(i);
            CameraThread cameraThread = new CameraThread();

            cameraThread.setCameraGphoto(cameraGphotoList.get(i));
            cameraThread.setCamera(camera);
            threadList.add(cameraThread);
        }
        return threadList;
    }

    @Override
    public List<Camera> getCameras() throws CameraException {
        try {
            cameraService.setAllCamerasInactive();
        } catch (ServiceException e) {
            LOGGER.debug("could not set cameras inactive", e);
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
                    LOGGER.error("Could not create camera entity", ex);
                    throw new CameraException(ex.getMessage(), -1);
                }
            }

        } finally {
            CameraUtils.closeQuietly(cl);
        }
        return cameraList;
    }
}
