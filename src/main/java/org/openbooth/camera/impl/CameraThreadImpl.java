package org.openbooth.camera.impl;

import org.openbooth.camera.CameraThread;
import org.openbooth.camera.libgphoto2java.CameraUtils;
import org.openbooth.camera.exeptions.CameraException;

import org.openbooth.camera.libgphoto2java.CameraFile;
import org.openbooth.camera.libgphoto2java.CameraGphoto;
import org.openbooth.service.ImageService;
import org.openbooth.service.ShootingService;
import org.openbooth.util.TempStorageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CameraThreadImpl extends CameraThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(CameraThreadImpl.class);


    private CameraGphoto cameraGphoto;

    @Autowired
    public CameraThreadImpl(ImageService imageService, ShootingService shootingService, TempStorageHandler tempStorageHandler){
        super(imageService,shootingService, tempStorageHandler);
    }



    @Override
    protected void captureAndTransferImageFromCamera(String destinationImagePath){
        CameraFile cf;
        try
        {
            cf = cameraGphoto.captureImage();
        }
        catch (CameraException ex)
        {
            LOGGER.error("captureImage - capture failed", ex);
            setStop(true);
            return;
        }

        if (cf == null)
        {
            LOGGER.error("captureImage - camerafile is null");
            return;
        }


        try
        {
            cf.save(destinationImagePath);
        }
        catch (CameraException e)
        {
            LOGGER.error("captureImage - save image failed, should be saved to {}", destinationImagePath, e);
            setStop(true);
        }
        finally
        {
            CameraUtils.closeQuietly(cf);
        }
    }

    @Override
    protected void captureAndTransferPreviewFromCamera(String destinationImagePath){
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



        try {
            cf.save(destinationImagePath);
        } catch (CameraException e) {
            LOGGER.error("capturePreview - save image failed, should be saved to {}", destinationImagePath, e);
            setStop(true);
        } finally {
            CameraUtils.closeQuietly(cf);
        }
    }







    void setCameraGphoto(CameraGphoto cameraGphoto) {
        this.cameraGphoto = cameraGphoto;
    }


    @Override
    protected void checkImplInitialized(){
        boolean initialized =  cameraGphoto != null;

        if(!initialized) {
            LOGGER.error("checkImplInitialized - cameraGphoto not properly initialized");
            shouldStop = true;
        }
    }
}
