package org.openbooth.storage.impl;

import org.openbooth.storage.StorageHandler;
import org.openbooth.storage.exception.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * This class provides the functionality described by the StorageHandler interface by creating folders in the
 * .openbooth folder in the user home directory.
 * Temporary folders are created inside the .tmp directory inside the .openbooth folder
 */
@Component
public class StorageHandlerImpl implements StorageHandler {

    private static final String DEFAULT_STORAGE_PATH = System.getProperty("user.home") + "/.openbooth";
    private static final String TEMP_STORAGE_PATH = DEFAULT_STORAGE_PATH + "/.tmp";


    private static final Logger LOGGER = LoggerFactory.getLogger(StorageHandlerImpl.class);

    private int folderCount = 0;

    protected String storagePath;

    public StorageHandlerImpl() throws StorageException{
        storagePath = DEFAULT_STORAGE_PATH;
        initializeStorage();
    }

    @Override
    public String getNewTemporaryFolderPath() throws StorageException {
        File newTempStorageFolder = new File(TEMP_STORAGE_PATH + "/tmp" + folderCount);
        createNewFolder(newTempStorageFolder);
        folderCount++;
        return newTempStorageFolder.getAbsolutePath();
    }


    @Override
    public String getPathForFolder(String folderName) throws StorageException{
        File folderFile = new File(DEFAULT_STORAGE_PATH + "/" + folderName);
        if(folderFile.exists() && folderFile.isDirectory()){
            return folderFile.getAbsolutePath();
        }

        if(!folderFile.exists()){
            createNewFolder(folderFile);
            LOGGER.debug("New folder {} created.", folderFile.getAbsolutePath());
            return folderFile.getAbsolutePath();
        }

        throw new StorageException("File "+ folderFile.getAbsolutePath() +" exists but it is not a directory.");
    }

    @Override
    public boolean checkIfFileExistsInFolder(String folderName, String fileName) {
        File file = new File(DEFAULT_STORAGE_PATH + "/" + folderName + "/" + fileName);
        return file.exists();
    }

    @Override
    public void clearTemporaryStorage() throws StorageException{
        File tempStorageFile = new File(TEMP_STORAGE_PATH);

        if(tempStorageFile.exists()){
            deleteFilesRecursively(tempStorageFile);
        }
        LOGGER.debug("Temporary storage cleared");
    }

    protected void initializeStorage() throws StorageException{
        clearTemporaryStorage();

        File tempStorageFile = new File(TEMP_STORAGE_PATH);

        if(!tempStorageFile.mkdirs()){
            throw new StorageException("could not create storage");
        }

        LOGGER.debug("Storage initialized");
    }

    private void deleteFilesRecursively(File file) throws StorageException{
        if(file.isFile()){
            deleteFileOrThrowException(file);
        }else {
            File[] fileList = file.listFiles();
            if(fileList == null){
                throw new StorageException("fileList is null");
            }
            for(File f : fileList){
                deleteFilesRecursively(f);
            }
            deleteFileOrThrowException(file);
        }
    }

    private void deleteFileOrThrowException(File file) throws StorageException{
        if(!file.delete()) throw new StorageException("could not delete file at " + file.getAbsolutePath());
    }


    private void createNewFolder(File folderFile) throws StorageException{
        if(!folderFile.mkdir()) throw new StorageException("could not create folder: " + folderFile.getAbsolutePath());
    }

}
