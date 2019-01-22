package org.openbooth.storage;

import org.openbooth.storage.exception.StorageException;


/**
 * A class implementing this interface provides methods for managing folders and files in the openooth home directory
 * as well as methods for creating temporary folders inside the openbooth home directory.
 *
 */
public interface StorageHandler {

    /**
     * This method can be used to create temporary folders
     * @return an absolute path to a new and empty temporary folder
     * @throws StorageException If the new temporary folder could not be created
     */
    String getNewTemporaryFolderPath() throws StorageException;

    /**
     * Returns the path for the folder with given name and creates it if it does not exist
     * @param folderName the given folder name
     * @return the absolute path to the folder
     * @throws IllegalArgumentException if the name of the folder contains a path delimiter, is empty or null
     * @throws StorageException If a file with the given name already exists, but it is not a folder
     */
    String getPathForFolder(String folderName) throws StorageException;

    /**
     * This method is used to check if a given file exists in a given folder in the storage
     * @param folderName the given folder name
     * @param fileName the given file name
     * @return true if the file exists, false otherwise
     * @throws StorageException if the given folder does not exist or if it is not a directory
     * @throws IllegalArgumentException if the given folder name  or filename contains a path delimiter, is empty or null
     */
    boolean checkIfFileExistsInFolder(String folderName, String fileName) throws StorageException;

    /**
     * Deletes all temporary folders
     * @throws StorageException If an error occurs
     */
    void clearTemporaryStorage() throws StorageException;
}
