package at.ac.tuwien.sepm.ws16.qse01.camera;

import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;

import java.util.List;


public interface CameraHandler {

    /**
     * Saves image in images folder and in database.
     * Also tells the Shot monitor to refresh the image.
     *
     * */
    public void getImages() throws CameraException;

    public List<Camera> getCameras() throws CameraException;
}
