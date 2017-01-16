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

    /**
     * Removes a camera that is connected but cannot be assigned to a position from the list.
     * @param camera the camera that shall be removed.
     */
    void removeCameraFromList(Camera camera);

    /**
     * Sets the captureImage Flag in the CameraThread
     * @param camera camera that shall capture a image
     */
    void captureImage(Camera camera);
}
