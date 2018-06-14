package org.openbooth.util;

import org.openbooth.util.exceptions.StorageHandlingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Used to create the temporary directory only once
 */
@Component
public class TempStorageHandler {

    private static final String TEMP_STORAGE_PATH = System.getProperty("user.home") + "/.openbooth/temp";

    private static final Logger LOGGER = LoggerFactory.getLogger(TempStorageHandler.class);

    public TempStorageHandler() throws StorageHandlingException{
        initializeTempStorage();
    }

    public String getTempStoragePath(){
        return TEMP_STORAGE_PATH;
    }

    private void initializeTempStorage() throws StorageHandlingException{
        File tempStorageFile = new File(TEMP_STORAGE_PATH);

        if(tempStorageFile.exists() && tempStorageFile.isDirectory()){
            File[] fileList = tempStorageFile.listFiles();
            if(fileList == null){
                throw new StorageHandlingException("fileList is null");
            }
            for(File f : fileList){
                boolean deleted = f.delete();
                if(!deleted){
                    LOGGER.error("initializeTempStorage - could not delete file {}", f.getPath());
                    throw new StorageHandlingException("could not delete file in existing temporary storage");
                }
            }

            LOGGER.info("initializeTempStorage - temporary storage cleaned");
        }else{
            boolean created = tempStorageFile.mkdirs();
            if(!created){
                throw new StorageHandlingException("could not create temporary storage");
            }
            LOGGER.info("initializeTempStorage - new temporary storage created");
        }


    }
}
