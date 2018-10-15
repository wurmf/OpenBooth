package org.openbooth.storage.impl;

import org.openbooth.storage.StorageHandler;
import org.openbooth.storage.exception.StorageHandlingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Used to create the temporary directory only once
 */
@Component
public class StorageHandlerImpl implements StorageHandler {

    private static final String STORAGE_PATH = System.getProperty("user.home") + "/.openbooth";
    private static final String TEMP_STORAGE_PATH = STORAGE_PATH + "/.tmp";


    private static final Logger LOGGER = LoggerFactory.getLogger(StorageHandlerImpl.class);

    private int folderCount = 0;

    public StorageHandlerImpl() throws StorageHandlingException{
        initializeStorage();
    }

    @Override
    public String getNewTemporaryFolderPath() throws StorageHandlingException{
        File newTempStorageFolder = new File(TEMP_STORAGE_PATH + "/tmp" + folderCount);
        createNewFolder(newTempStorageFolder);
        folderCount++;
        return newTempStorageFolder.getAbsolutePath();
    }


    @Override
    public String getPathForFolder(String folderName) throws StorageHandlingException{
        File folderFile = new File(STORAGE_PATH + "/" + folderName);
        if(folderFile.exists() && folderFile.isDirectory()){
            return folderFile.getAbsolutePath();
        }

        if(!folderFile.exists()){
            createNewFolder(folderFile);
            LOGGER.debug("New folder {} created.", folderFile.getAbsolutePath());
            return folderFile.getAbsolutePath();
        }

        throw new StorageHandlingException("File "+ folderFile.getAbsolutePath() +" exists but it is not a directory.");
    }

    @Override
    public boolean checkIfFileExistsInFolder(String folderName, String fileName) {
        File file = new File(STORAGE_PATH + "/" + folderName + "/" + fileName);
        return file.exists();
    }

    private void initializeStorage() throws StorageHandlingException{
        File tempStorageFile = new File(TEMP_STORAGE_PATH);

        if(tempStorageFile.exists()){
            deleteFilesRecursively(tempStorageFile);
        }

        if(!tempStorageFile.mkdirs()){
            throw new StorageHandlingException("could not create storage");
        }

        LOGGER.debug("Storage initialized");
    }

    private void deleteFilesRecursively(File file) throws StorageHandlingException{
        if(file.isFile()){
            deleteFileOrThrowException(file);
        }else {
            File[] fileList = file.listFiles();
            if(fileList == null){
                throw new StorageHandlingException("fileList is null");
            }
            for(File f : fileList){
                deleteFilesRecursively(f);
            }
            deleteFileOrThrowException(file);
        }
    }

    private void deleteFileOrThrowException(File file) throws StorageHandlingException{
        if(!file.delete()) throw new StorageHandlingException("could not delete file at " + file.getAbsolutePath());
    }


    private void createNewFolder(File folderFile) throws StorageHandlingException{
        if(!folderFile.mkdir()) throw new StorageHandlingException("could not create folder: " + folderFile.getAbsolutePath());
    }

}
