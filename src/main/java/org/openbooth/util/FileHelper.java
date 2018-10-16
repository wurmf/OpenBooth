package org.openbooth.util;

import org.openbooth.util.exceptions.FileHandlingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * This class is a helper class for creating files and folders
 */
@Component
public class FileHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);

    /**
     * Creates a new folder and all of it's parents if it does not exist.
     * @param folderPath The path where to create the folder
     * @throws FileHandlingException If the creation fails or the given path is a file and not a folder
     */
    public void createFolderIfItDoesNotExist(String folderPath) throws FileHandlingException {
        folderPath = PathHelper.expandPath(folderPath);

        File folderFile = new File(folderPath);
        if(folderFile.exists() && !folderFile.isDirectory()) throw new FileHandlingException("The given path is a file and not a folder");
        if(!folderFile.exists()){
            if(!folderFile.mkdirs()) throw new FileHandlingException("could not create folder: " + folderFile.getAbsolutePath());
            LOGGER.debug("Created new folder: " + folderPath);
        }
    }

}
