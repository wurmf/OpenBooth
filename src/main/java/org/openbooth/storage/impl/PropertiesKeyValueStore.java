package org.openbooth.storage.impl;

import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.StorageHandler;
import org.openbooth.storage.exception.PersistenceException;
import org.openbooth.storage.exception.StorageHandlingException;
import org.openbooth.util.FileHelper;
import org.openbooth.util.FileTransfer;
import org.openbooth.util.exceptions.FileHandlingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

@Component
public class PropertiesKeyValueStore implements KeyValueStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesKeyValueStore.class);
    private static final String CONFIG_FOLDER_NAME = "config";
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String RESOURCES_CONFIG_FILE_PATH = "/config/config.properties";
    private static final String RESOURCES_DEFAULT_CONFIG_FILE_PATH = "/config/defaults.properties";

    private String configFilePath;
    private Properties storageProperties;


    @Autowired
    private PropertiesKeyValueStore(StorageHandler storageHandler, FileHelper fileHelper) throws PersistenceException{
        try {
            configFilePath = storageHandler.getPathForFolder(CONFIG_FOLDER_NAME) + "/" + CONFIG_FILE_NAME;

            createConfigFileIfItDoesNotExists(storageHandler, fileHelper);

            storageProperties = new Properties();
            loadProperties(storageProperties, configFilePath);
        } catch (StorageHandlingException e) {
            throw new PersistenceException(e);
        }
    }

    private void createConfigFileIfItDoesNotExists(StorageHandler storageHandler, FileHelper fileHelper) throws PersistenceException {
        try{
            if(!storageHandler.checkIfFileExistsInFolder(CONFIG_FOLDER_NAME, CONFIG_FILE_NAME)) {
                FileTransfer.transfer(RESOURCES_CONFIG_FILE_PATH, configFilePath);
                restoreDefaultProperties(fileHelper);
            }
        } catch (IOException e) {
            throw new PersistenceException(e);
        }

    }

    public void restoreDefaultProperties(FileHelper fileHelper) throws PersistenceException{
        Properties defaultProperties = new Properties();
        loadPropertiesFromResources(defaultProperties, RESOURCES_DEFAULT_CONFIG_FILE_PATH);


        String defaultImageStoragePath = System.getProperty("user.home") + "/openbooth_images";
        try {
            fileHelper.createFolderIfItDoesNotExist(defaultImageStoragePath);
        } catch (FileHandlingException e) {
            throw new PersistenceException("error during creating default image storage folder",e);
        }
        defaultProperties.setProperty("image_storage_path", defaultImageStoragePath);

        persistProperties(defaultProperties, configFilePath);

    }

    private void loadProperties(Properties properties, String path) throws PersistenceException{
        try(Reader reader = new FileReader(new File(path))){
            properties.load(reader);
            LOGGER.debug("config loaded from {}", path);
        }catch (IOException e){
            throw new PersistenceException(e);
        }
    }

    private void loadPropertiesFromResources(Properties properties, String path) throws PersistenceException{
        try(Reader reader = new InputStreamReader(PropertiesKeyValueStore.class.getResourceAsStream(path))){
            properties.load(reader);
            LOGGER.debug("config loaded from resources at {}", path);
        }catch (IOException e){
            throw new PersistenceException(e);
        }
    }

    private void persistProperties(Properties properties, String path) throws PersistenceException{
        try (Writer writer = new FileWriter(path)){
            properties.store(writer,null);
        } catch (IOException e) {
            throw new PersistenceException(e);
        }
    }


    @Override
    public void put(String key, String value) throws PersistenceException{
        storageProperties.setProperty(key,value);
        persistProperties(storageProperties, configFilePath);
        LOGGER.trace("Key {} with value {} persisted.", key, value);
    }

    @Override
    public void put(String key, int value) throws PersistenceException{
        storageProperties.setProperty(key, Integer.toString(value));
        persistProperties(storageProperties, configFilePath);
        LOGGER.trace("Key {} with value {} persisted.", key, value);
    }

    public void put (String key, double value) throws PersistenceException{
        storageProperties.setProperty(key, Double.toString(value));
        persistProperties(storageProperties, configFilePath);
        LOGGER.trace("Key {} with value {} persisted.", key, value);
    }

    private String getValue(String key) throws PersistenceException{
        String value = storageProperties.getProperty(key);
        if (value == null) throw new PersistenceException("No value for key \"" + key + "\" could be found.");
        return value;
    }

    public String getString(String key) throws PersistenceException{
        String value = getValue(key);
        LOGGER.trace("Key {} with value {} retrieved.", key, value);
        return value;
    }


    public int getInt(String key) throws PersistenceException{
        try {
            int value = Integer.parseInt(getValue(key));
            LOGGER.trace("Key {} with value {} retrieved.", key, value);
            return value;
        } catch (NumberFormatException e) {
            throw new PersistenceException("Error when retrieving integer property.", e);
        }
    }

    public double getDouble(String key) throws PersistenceException{
        try {
            double value = Double.parseDouble(getValue(key));
            LOGGER.trace("Key {} with value {} retrieved.", key, value);
            return value;
        } catch (NumberFormatException e) {
            throw new PersistenceException("Error when retrieving double property.", e);
        }
    }


}
