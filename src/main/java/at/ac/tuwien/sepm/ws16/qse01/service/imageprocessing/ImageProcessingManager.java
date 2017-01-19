package at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing;

import at.ac.tuwien.sepm.ws16.qse01.entities.Profile;
import at.ac.tuwien.sepm.ws16.qse01.service.exceptions.ServiceException;

/**
 * This class provides methods for initializing and stopping the image processing
 */
public interface ImageProcessingManager {

    /**
     * Starts the image processing
     * Initializes the CamerHandler and starts the CameraThreads
     * Initializes the ShotFrames
     * Instantiates ImageProcessor objects for each CameraThread
     * @throws ServiceException if the opencv library cannot be loaded, no CameraThreads or ShotFrames will be initialised
     */
     void initImageProcessing() throws ServiceException;

    /**
     * Stops all Threads related to image processing including the CameraThreads
     */
     void stopImageProcessing();

    /**
     * Checks wether the give profile is compatible with the connected cameras.
     * If all cameras included in the give profile are connected, true will be returned
     * If a connected camera has no positon in the give profile it will be ignored
     * If any camera included in the give profile is not connected false will be returned
     * @param profile the given profile
     * @return wether the give profile is compatible with the connected cameras
     * @throws ServiceException if an error during the checking occurs
     */
    boolean checkImageProcessing(Profile profile) throws ServiceException;
}
