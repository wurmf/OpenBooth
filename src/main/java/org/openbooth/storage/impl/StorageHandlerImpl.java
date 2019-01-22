package org.openbooth.storage.impl;

import org.openbooth.storage.StorageHandler;
import org.openbooth.storage.exception.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;

/**
 * This class provides the functionality described by the StorageHandler interface by creating folders in the
 * .openbooth folder in the user home directory.
 * Temporary folders are created inside the .tmp directory inside the .openbooth folder
 */
@Component
public class StorageHandlerImpl implements StorageHandler {

    private static final String DEFAULT_STORAGE_PATH = System.getProperty("user.home") + "/.openbooth";
    private static final String TEMP_STORAGE_RELATIVE_PATH =  "/.tmp";


    private static final Logger LOGGER = LoggerFactory.getLogger(StorageHandlerImpl.class);

    private int folderCount = 0;

    protected String storagePath;
    protected String temporaryStoragePath;

    public StorageHandlerImpl() throws StorageException{
        storagePath = DEFAULT_STORAGE_PATH;
        initializeStorage();
    }

    @Override
    public String getNewTemporaryFolderPath() throws StorageException {
        File newTempStorageFolder = new File(temporaryStoragePath+ "/" + folderCount);
        createNewFolder(newTempStorageFolder);
        folderCount++;
        return newTempStorageFolder.getAbsolutePath();
    }


    @Override
    public String getPathForFolder(String folderName) throws StorageException{

        if(folderName == null || folderName.isEmpty())
            throw new IllegalArgumentException("folderName is null or empty");

        if(folderName.contains("/"))
            throw new IllegalArgumentException("folderName must not contain path delimiters");


        File folderFile = new File(storagePath + "/" + folderName);
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
    public boolean checkIfFileExistsInFolder(String folderName, String fileName) throws StorageException{
        if(folderName == null || folderName.isEmpty())
            throw new IllegalArgumentException("foldername is null or empty");

        if(fileName == null || fileName.isEmpty())
            throw new IllegalArgumentException("filename is null or empty");

        if(folderName.contains("/") || fileName.contains("/"))
            throw new IllegalArgumentException("foldername or filename must not contain path slashes");

        if(!Paths.get(storagePath + "/" + folderName).toFile().isDirectory())
            throw new StorageException("folder with name " + folderName + " does not exist or is not a directory");

        File file = new File(storagePath + "/" + folderName + "/" + fileName);
        return file.exists();
    }

    @Override
    public void clearTemporaryStorage() throws StorageException{
        File tempStorageFile = new File(temporaryStoragePath);

        if(tempStorageFile.exists()){
            deleteFilesRecursively(tempStorageFile);
        }
        LOGGER.debug("Temporary storage cleared");
    }

    protected void initializeStorage() throws StorageException{
        temporaryStoragePath = storagePath + TEMP_STORAGE_RELATIVE_PATH;

        clearTemporaryStorage();

        File tempStorageFile = new File(temporaryStoragePath);

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
