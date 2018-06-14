package org.openbooth.camera;

import org.openbooth.entities.Camera;
import org.openbooth.gui.ShotFrameController;
import org.openbooth.service.imageprocessing.ImageProcessor;

public abstract class CameraThread extends Thread {

    protected ShotFrameController shotFrameController;
    protected ImageProcessor imageProcessor;

    protected boolean shouldStop = false;
    protected boolean takeImage = false;
    protected boolean serieShot = false;
    protected Camera camera;
    protected int countdown = 0;


    @Override
    public abstract void run();

    public void setTakeImage(boolean takeImage){
        this.takeImage = takeImage;
    }

    public void setSerieShot(boolean serieShot){
        this.serieShot = serieShot;
        this.countdown = 0;
    }

    public void setCountdown(int countdown){
        this.countdown = countdown;
        this.serieShot = false;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera(){
        return this.camera;
    }

    public void setStop(boolean shouldStop){
        this.shouldStop = shouldStop;
    }

    public void setShotFrameController(ShotFrameController shotFrameController){
        this.shotFrameController = shotFrameController;
    }

    public void setImageProcessor(ImageProcessor imageProcessor){
        this.imageProcessor = imageProcessor;
    }



}
