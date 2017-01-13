package at.ac.tuwien.sepm.ws16.qse01.camera;

import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;
import at.ac.tuwien.sepm.ws16.qse01.camera.impl.CameraThread;
import at.ac.tuwien.sepm.ws16.qse01.entities.Camera;

import java.util.List;


public interface CameraHandler {

    /**
     * Detects all connected cameras and saves for them in the Database or sets them active if they are already saved.
     *
     *
     * @throws CameraException if something goes wrong during the start of the Threads
     * */
    List<CameraThread> createThreads() throws CameraException;


    /**
     * Saves image in images folder and in database.
     * Also tells the Shot monitor to refresh the image.
     *
     * @throws CameraException if something goes wrong during the detecting of the cameras
     * */
    List<Camera> getCameras() throws CameraException;
}
