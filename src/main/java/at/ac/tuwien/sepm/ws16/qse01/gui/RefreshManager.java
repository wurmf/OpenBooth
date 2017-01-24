package at.ac.tuwien.sepm.ws16.qse01.gui;

import at.ac.tuwien.sepm.ws16.qse01.application.ShotFrameManager;
import at.ac.tuwien.sepm.ws16.qse01.entities.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class that is notified if a new photo is shot and notifies the ShotFrameManager and the MiniatureFrameController of the change.
 */
@Component
public class RefreshManager {
    ShotFrameManager shotFrameManager;
    MiniaturFrameController miniaturFrameController;

    @Autowired
    public RefreshManager(ShotFrameManager shotFrameManager, MiniaturFrameController miniaturFrameController){
        this.shotFrameManager=shotFrameManager;
        this.miniaturFrameController=miniaturFrameController;
    }

    /**
     * Notifies the ShotFrameManager and the MiniatureFrameController of a newly shot image.
     * @param image the Image-Object representing the new image.
     */
    public synchronized void refreshFrames(Image image){
        miniaturFrameController.notifyOfNewImage(image,-1);
    }

    public void notifyMiniatureFrameOfDelete(Image image){
        miniaturFrameController.notifyOfDelete(image);
    }
    public void notifyMiniatureFrameOfAdd(Image image,int index){
        miniaturFrameController.notifyOfNewImage(image,index);
    }
}