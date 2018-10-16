package org.openbooth.storage;

import org.openbooth.storage.exception.StorageHandlingException;

/**
 * A class implementing this interface provides methods for managing folders and files in the openooth home directory
 * as well as methods for creating temporary folders inside the openbooth home directory.
 *
 */
public interface StorageHandler {

    /**
     * This method can be used to create temporary folders
     * @return an absolute path to a new and empty temporary folder
     * @throws StorageHandlingException If the new temporary folder could not be created
     */
    String getNewTemporaryFolderPath() throws StorageHandlingException;

    /**
     * Returns the path for the folder with given name and creates it if it does not exist
     * @param folderName the given folder name
     * @return the absolute path to the folder
     */
    String getPathForFolder(String folderName) throws StorageHandlingException;

    /**
     * This method is used to check if a given file exists in a given folder in the storage
     * @param folderName the given folder name
     * @param fileName the given file name
     * @return true if the file exists, false otherwise
     */
    boolean checkIfFileExistsInFolder(String folderName, String fileName);
}
