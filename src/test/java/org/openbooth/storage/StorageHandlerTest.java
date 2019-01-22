package org.openbooth.storage;

import org.junit.Assert;
import org.junit.Test;
import org.openbooth.SpringTestEnvironment;
import org.openbooth.TestStorageHandler;
import org.openbooth.storage.exception.StorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StorageHandlerTest extends SpringTestEnvironment {


    private TestStorageHandler storageHandler;

    public StorageHandlerTest(){
        this.storageHandler = getPrototypedApplicationContext().getBean(TestStorageHandler.class);
    }


    @Test
    public void getNewTemporaryFolderPathTestCheckIfNewPath() throws StorageException {
        String tempFolderPath1 = storageHandler.getNewTemporaryFolderPath();
        String tempFolderPath2 = storageHandler.getNewTemporaryFolderPath();

        Assert.assertNotEquals(tempFolderPath1, tempFolderPath2);
    }

    @Test
    public void getNewTemporaryFolderPathCheckIfCorrectDirectory() throws StorageException{
        Path tempFolderPath = Paths.get(storageHandler.getNewTemporaryFolderPath());

        Assert.assertTrue(Files.isDirectory(tempFolderPath));
        tempFolderPath.startsWith(Paths.get(storageHandler.getStoragePath()));
    }





    @Test
    public void getPathForFolderWithNonExistingDirectory() throws StorageException{
        Path newFolderPath = Paths.get(storageHandler.getPathForFolder("test"));
        newFolderPath.startsWith(Paths.get(storageHandler.getStoragePath()));
        Files.isDirectory(newFolderPath);
    }

    @Test
    public void getPathForFolderWithExistingDirectory() throws StorageException, IOException{
        Files.createDirectories(Paths.get(storageHandler.getStoragePath() + "/test"));
        Path newFolderPath = Paths.get(storageHandler.getPathForFolder("test"));
        Files.isDirectory(newFolderPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPathForFolderTestWithNull() throws StorageException{
        storageHandler.getPathForFolder(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPathForFolderTestWithEmptyString() throws StorageException{
        storageHandler.getPathForFolder("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPathForFolderWithNameContainingSlash() throws StorageException{
        storageHandler.getPathForFolder("test1/test2");
    }

    @Test(expected = StorageException.class)
    public void getPathForFolderWithExistingFile() throws StorageException, IOException {
        Files.createFile(Paths.get(storageHandler.getStoragePath() + "/test"));
        storageHandler.getPathForFolder("test");
    }


    @Test
    public void checkIfFileExistsInFolderWithExistingFile() throws StorageException, IOException{
        Files.createDirectory(Paths.get(storageHandler.getStoragePath() + "/test"));
        Files.createFile(Paths.get(storageHandler.getStoragePath() + "/test/testfile"));
        Assert.assertTrue(storageHandler.checkIfFileExistsInFolder("test", "testfile"));
    }

    @Test
    public void checkIfFileExistsInFolderWithNonExistingFile() throws StorageException, IOException{
        Files.createDirectory(Paths.get(storageHandler.getStoragePath() + "/test"));
        Assert.assertFalse(storageHandler.checkIfFileExistsInFolder("test", "testfile"));
    }


    @Test(expected = IllegalArgumentException.class)
    public void checkIfFileExistsInFolderWithNull() throws StorageException{
        storageHandler.checkIfFileExistsInFolder(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfFileExistsInFolderWithEmptyStrings() throws StorageException{
        storageHandler.checkIfFileExistsInFolder("", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfFileExistsInFolderWithStringsContainingSlash() throws StorageException{
        storageHandler.checkIfFileExistsInFolder("/test1/test2", "/test3/test4");
    }

    @Test(expected = StorageException.class)
    public void checkIfFileExistsInFolderWithNonExistingFolder() throws StorageException{
        storageHandler.checkIfFileExistsInFolder("testfolder", "testfile");
    }

    @Test(expected = StorageException.class)
    public void checkIfFileExistsInFolderWithFolderBeingAFile() throws StorageException, IOException{
        Assert.assertTrue(Paths.get(storageHandler.getStoragePath() + "/" + "test").toFile().createNewFile());
        storageHandler.checkIfFileExistsInFolder("test", "testfile");
    }


    @Test
    public void clearTemporaryStorageTest() throws StorageException, IOException{
        Assert.assertTrue(Files.isDirectory(Paths.get(storageHandler.getTemporaryStoragePath())));
        Assert.assertTrue(Paths.get(storageHandler.getTemporaryStoragePath() + "/testfile").toFile().createNewFile());
        storageHandler.clearTemporaryStorage();
        Assert.assertFalse(Files.isDirectory(Paths.get(storageHandler.getTemporaryStoragePath())));
    }





}
