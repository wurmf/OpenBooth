package org.openbooth.util;

import org.openbooth.util.exceptions.FileHandlingException;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * This class is a helper class for creating files and folders
 */
@Component
public class FileHandler {

    /**
     * Creates a new folder if it does not exist.
     * @param folderPath The path where to create the folder
     * @throws FileHandlingException If the creation fails or the given path is a file but not a folder
     */
    public void createFolderIfItDoesNotExist(String folderPath) throws FileHandlingException {
        File folderFile = new File(folderPath);
        if(folderFile.exists() && !folderFile.isDirectory()) throw new FileHandlingException("The given path is a file and not a folder");
        if(!folderFile.mkdir()) throw new FileHandlingException("could not create folder: " + folderFile.getAbsolutePath());
    }

}
