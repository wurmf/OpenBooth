package org.openbooth.camera.impl;

import org.openbooth.camera.CameraHandler;
import org.openbooth.camera.CameraThread;
import org.openbooth.camera.exeptions.CameraException;

import org.openbooth.camera.libgphoto2java.CameraGphoto;
import org.openbooth.camera.libgphoto2java.CameraList;
import org.openbooth.camera.libgphoto2java.CameraUtils;
import org.openbooth.entities.Camera;
import org.openbooth.service.CameraService;
import org.openbooth.service.exceptions.ServiceException;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Component
public class CameraHandlerImpl implements CameraHandler
{

    private Logger LOGGER = LoggerFactory.getLogger(CameraHandlerImpl.class);

    private ApplicationContext applicationContext;

    private CameraService cameraService;
    private List<CameraGphoto> cameraGphotoList=new ArrayList<>();
    private List<String> cameraModelList = new ArrayList<>();
    private List<String> cameraPortList = new ArrayList<>();
    private List<Camera> cameraList = new ArrayList<>();
    private List<CameraThreadImpl> internalThreadList;

    private boolean isInitialized = false;

    @Autowired
    public CameraHandlerImpl(CameraService cameraService, ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
        this.cameraService=cameraService;
    }

    /**
     * Saves image in images folder and in database.
     * Also tells the Shot monitor to refresh the image.
     *
     * */
    @Override
    public List<CameraThread> createThreads(List<Camera> cameraList) throws CameraException
    {


        internalThreadList = new ArrayList<>();

        List<CameraThread> threadList = new ArrayList<>();
        for(Camera camera : cameraList)
        {
            int index = cameraList.indexOf(camera);
            CameraThreadImpl cameraThread = applicationContext.getBean(CameraThreadImpl.class);

            cameraThread.setCameraGphoto(cameraGphotoList.get(index));
            cameraThread.setCamera(camera);
            internalThreadList.add(cameraThread);
            threadList.add(cameraThread);
        }

        return threadList;
    }

    @PostConstruct
    @Override
    public List<Camera> getCameras() throws CameraException
    {
        if(isInitialized)
        {
            for (CameraGphoto camera: cameraGphotoList)
            {
                if(!camera.isInitialized())
                {
                    camera.initialize();
                }
            }
            return cameraList;
        }
        try
        {
            cameraService.setAllCamerasInactive();
        }
        catch (ServiceException e)
        {
            LOGGER.debug("getCameras - could not set cameras inactive", e);
        }
        cameraGphotoList=new ArrayList<>();
        cameraModelList=new ArrayList<>();
        cameraPortList=new ArrayList<>();
        cameraList=new ArrayList<>();
        int count=0;
        final CameraList cl = new CameraList();
        try
        {
            LOGGER.debug("getCameras - Cameras: " + cl);

            count=cl.getCount();
            Camera camera;
            for(int i=0;i<count;i++)
            {
                cameraGphotoList.add(new CameraGphoto());
                Pointer pInfo=cl.getPortInfo(i);
                cameraPortList.add(cl.getPort(i));
                cameraModelList.add(cl.getModel(i));
                LOGGER.debug("getCamera - PortInfo: "+pInfo.toString());

                cameraGphotoList.get(i).setPortInfo(cl.getPortPath(cl.getPort(i,true)));

                cameraGphotoList.get(i).initialize();
                cameraGphotoList.get(i).ref();
                try
                {
                    camera = new Camera(-1, "Kamera " + i, cameraPortList.get(i), cameraModelList.get(i), "Seriennummer: "+i);
                    camera=cameraService.cameraExists(camera);
                    if(camera==null)
                    {
                        camera = new Camera(-1, "Kamera " + i, cameraPortList.get(i), cameraModelList.get(i), "Seriennummer: "+i);
                        cameraService.createCamera(camera);
                    }
                    cameraService.setCameraActive(camera.getId());
                    cameraList.add(camera);
                    LOGGER.info("getCameras - camera {} detected", camera);
                }
                catch (ServiceException ex)
                {
                    LOGGER.error("getCameras - Could not create camera entity - ", ex);
                    throw new CameraException(ex.getMessage(), -1);
                }

            }

        }
        finally
        {
            CameraUtils.closeQuietly(cl);
        }
        isInitialized = true;
        return cameraList;
    }

    @Override
    public void captureImage(Camera camera)
    {
        for (CameraThreadImpl thread: internalThreadList)
        {
            if(thread.getCamera().getId()==camera.getId())
            {
                thread.setTakeImage(true);
            }
        }
    }

    @Override
    public void setSerieShot(Camera camera, boolean serieShot)
    {
        for (CameraThreadImpl thread: internalThreadList)
        {
            if(thread.getCamera().getId()==camera.getId())
            {
                thread.setSerieShot(serieShot);
            }
        }
    }

    @Override
    public void setCountdown(Camera camera, int countdown)
    {
        for (CameraThreadImpl thread: internalThreadList)
        {
            if(thread.getCamera().getId()==camera.getId())
            {
                thread.setCountdown(countdown);
            }
        }
    }

    @Override
    public void removeCameraFromList(Camera camera)
    {
        if(cameraList.isEmpty()){
            return;
        }
        /*
        int index=cameraList.indexOf(camera);
        cameraList.remove(index);
        CameraUtils.closeQuietly(cameraGphotoList.get(index));
        cameraGphotoList.remove(index);
        cameraModelList.remove(index);
        cameraPortList.remove(index);
        */

    }

    @Override
    public void closeCameras()
    {
        for(CameraGphoto cameraGphoto : cameraGphotoList)
        {
            CameraUtils.closeQuietly(cameraGphoto);
        }
    }
}
