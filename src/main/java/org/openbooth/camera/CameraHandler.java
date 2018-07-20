package org.openbooth.camera;

import org.openbooth.camera.exceptions.CameraException;

/**
 * This interface is the interface to the connected camera
 */
public interface CameraHandler {

    /**
     * Triggers a shot and stores the image at the specified location with the given name.
     * The file extension is determined inside the method.
     * @param storagePath the path to the directory where the image will be stored
     * @param imageName the name of the image without file extension
     * @throws CameraException if an error during capture occurs
     * @return the full path of the stored image
     */
    String captureAndTransferImageFromCamera(String storagePath, String imageName) throws CameraException;

    /**
     * Makes a preview image and stores the image at the specified location with the given name.
     * The file extension is determined inside the method.
     * @param storagePath the path to the directory where the preview image will be stored
     * @param imageName the name of the preview image without file extension
     * @throws CameraException if an error during capture occurs
     * @return the full path of the stored preview image
     */
    String captureAndTransferPreviewFromCamera(String storagePath, String imageName) throws CameraException;
}
