package at.ac.tuwien.sepm.ws16.qse01.service.imageprocessing;

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
}
