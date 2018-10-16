package org.openbooth.storage.impl;

import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.StorageHandler;
import org.openbooth.storage.exception.StorageException;
import org.openbooth.util.FileTransfer;
import org.openbooth.storage.exception.KeyValueStoreException;
import org.openbooth.util.validation.ConfigValidator;
import org.openbooth.util.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    private ConfigValidator configValidator;


    @Autowired
    private PropertiesKeyValueStore(StorageHandler storageHandler, ConfigValidator configValidator) throws StorageException {
            this.configValidator = configValidator;

            configFilePath = storageHandler.getPathForFolder(CONFIG_FOLDER_NAME) + "/" + CONFIG_FILE_NAME;

            createConfigFileIfItDoesNotExists(storageHandler);

            storageProperties = new Properties();
            loadProperties(storageProperties, configFilePath);

    }

    @PostConstruct
    private void validateKeyValueStore() throws ValidationException, KeyValueStoreException{
        configValidator.validate(this);
    }

    private void createConfigFileIfItDoesNotExists(StorageHandler storageHandler) throws StorageException {
        try{
            if(!storageHandler.checkIfFileExistsInFolder(CONFIG_FOLDER_NAME, CONFIG_FILE_NAME)) {
                LOGGER.debug("config file does not exist, trying to create new config file at " + configFilePath);
                FileTransfer.transfer(RESOURCES_CONFIG_FILE_PATH, configFilePath);
                restoreDefaultProperties();
            }
        } catch (IOException e) {
            throw new StorageException(e);
        }

    }

    public void restoreDefaultProperties() throws StorageException {
        Properties defaultProperties = new Properties();
        loadPropertiesFromResources(defaultProperties, RESOURCES_DEFAULT_CONFIG_FILE_PATH);

        persistProperties(defaultProperties, configFilePath);
        LOGGER.debug("restored default config to  " + configFilePath);

    }

    private void loadProperties(Properties properties, String path) throws StorageException {
        try(Reader reader = new FileReader(new File(path))){
            properties.load(reader);
            LOGGER.debug("config loaded from {}", path);
        }catch (IOException e){
            throw new StorageException(e);
        }
    }

    private void loadPropertiesFromResources(Properties properties, String path) throws StorageException {
        try(Reader reader = new InputStreamReader(PropertiesKeyValueStore.class.getResourceAsStream(path))){
            properties.load(reader);
            LOGGER.debug("config loaded from resources at {}", path);
        }catch (IOException e){
            throw new StorageException(e);
        }
    }

    private void persistProperties(Properties properties, String path) throws StorageException {
        try (Writer writer = new FileWriter(path)){
            properties.store(writer,null);
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }


    @Override
    public void put(String key, String value) throws StorageException, ValidationException, KeyValueStoreException {
        storageProperties.setProperty(key,value);
        configValidator.validate(this);
        persistProperties(storageProperties, configFilePath);
        LOGGER.trace("Key {} with value {} persisted.", key, value);
    }

    @Override
    public void put(String key, int value) throws StorageException, ValidationException, KeyValueStoreException {
        storageProperties.setProperty(key, Integer.toString(value));
        configValidator.validate(this);
        persistProperties(storageProperties, configFilePath);
        LOGGER.trace("Key {} with value {} persisted.", key, value);
    }

    public void put (String key, double value) throws StorageException, ValidationException, KeyValueStoreException {
        storageProperties.setProperty(key, Double.toString(value));
        configValidator.validate(this);
        persistProperties(storageProperties, configFilePath);
        LOGGER.trace("Key {} with value {} persisted.", key, value);
    }

    private String getValue(String key) throws KeyValueStoreException{
        String value = storageProperties.getProperty(key);
        if (value == null) throw new KeyValueStoreException("No value for key \"" + key + "\" could be found.");
        return value;
    }

    public String getString(String key) throws KeyValueStoreException{
        String value = getValue(key);
        LOGGER.trace("Key {} with value {} retrieved.", key, value);
        return value;
    }


    public int getInt(String key) throws KeyValueStoreException{
        try {
            int value = Integer.parseInt(getValue(key));
            LOGGER.trace("Key {} with value {} retrieved.", key, value);
            return value;
        } catch (NumberFormatException e) {
            throw new KeyValueStoreException("Error when retrieving integer property.", e);
        }
    }

    public double getDouble(String key) throws KeyValueStoreException{
        try {
            double value = Double.parseDouble(getValue(key));
            LOGGER.trace("Key {} with value {} retrieved.", key, value);
            return value;
        } catch (NumberFormatException e) {
            throw new KeyValueStoreException("Error when retrieving double property.", e);
        }
    }


}
