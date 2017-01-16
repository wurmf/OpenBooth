package at.ac.tuwien.sepm.ws16.qse01.application;

import at.ac.tuwien.sepm.ws16.qse01.camera.CameraHandler;
import at.ac.tuwien.sepm.ws16.qse01.camera.impl.CameraThread;
import at.ac.tuwien.sepm.ws16.qse01.service.ImageService;
import at.ac.tuwien.sepm.ws16.qse01.service.ShootingService;
import at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing.ImageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ThreadCommunicator {

    private CameraHandler cameraHandler;
    private ImageService imageService;
    private ImageProcessor imageProcessor;
    private ShootingService shootingService;

    private List<CameraThread> cameraThreads= new ArrayList<>();

    @Autowired
    public ThreadCommunicator(CameraHandler cameraHandler, ImageService imageService, ShootingService shootingService) {
        this.cameraHandler = cameraHandler;
        this.imageService = imageService;
        this.shootingService = shootingService;
    }

    public void run()
    {
        init();
        for (CameraThread thread: cameraThreads)
        {
            thread.start();
        }
    }

    private void init()
    {
        cameraThreads=cameraHandler.createThreads();
        for (CameraThread thread: cameraThreads)
        {
            thread.setImageService(imageService);
            thread.setImageProcessor(imageProcessor);
            thread.setShootingService(shootingService);
        }
    }

    public void captureImage(int cameraID)
    {
        for (CameraThread thread : cameraThreads)
        {
            if(thread.getCamera().getId()==cameraID)
            {
                thread.setTakeImage(true);
            }
        }
    }

    public void setCameraHandler(CameraHandler cameraHandler) {
        this.cameraHandler = cameraHandler;
    }

    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    public void setImageProcessor(ImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }

    public void setShootingService(ShootingService shootingService) {
        this.shootingService = shootingService;
    }
}
