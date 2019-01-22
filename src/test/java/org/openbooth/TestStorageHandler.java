package org.openbooth;

import org.openbooth.storage.exception.StorageException;
import org.openbooth.storage.impl.StorageHandlerImpl;

import java.io.IOException;
import java.nio.file.Files;


public class TestStorageHandler extends StorageHandlerImpl {

    public TestStorageHandler() throws StorageException {
        try {
            storagePath = Files.createTempDirectory("openbooth").toAbsolutePath().toString();
            initializeStorage();
        } catch (IOException e) {
            throw new StorageException("Error while creating temporary storage for testing", e);
        }
    }

    public String getStoragePath(){
        return storagePath;
    }

    public String getTemporaryStoragePath() {return temporaryStoragePath; }




}
