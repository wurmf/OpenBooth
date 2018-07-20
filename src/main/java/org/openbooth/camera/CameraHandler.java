package org.openbooth.camera;

import org.openbooth.camera.exceptions.CameraException;

/**
 * This interface is the interface to the connected camera
 */
public interface CameraHandler {

    /**
     * Triggers a shot and transfers it the image to specified location
     * @param storagePath the specified location
     * @throws CameraException if an error during capture occurs
     * @return the full name of the transferred image
     */
    String captureAndTransferImageFromCamera(String storagePath) throws CameraException;

    /**
     * Makes a preview image and stores it at the specified location
     * @param storagePath the specified location
     * @throws CameraException if an error during capturing the preview occurs
     * @return the full name of the transferred preview image
     */
    String captureAndTransferPreviewFromCamera(String storagePath) throws CameraException;
}
