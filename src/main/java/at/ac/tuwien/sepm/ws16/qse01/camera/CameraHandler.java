package at.ac.tuwien.sepm.ws16.qse01.camera;

import at.ac.tuwien.sepm.ws16.qse01.camera.exeptions.CameraException;

/**
 * Created by osboxes on 28.11.16.
 */
public interface CameraHandler {

    /**
     * Saves image in images folder and in database.
     * Also tells the Shot monitor to refresh the image.
     *
     * TODO: get Imageservice and Sessionservice
     *
     * @return
     *
     * */
    public void getImages() throws CameraException;
}
