package org.openbooth.storage.impl;

import org.openbooth.storage.ConfigStore;
import org.openbooth.storage.ReadOnlyConfigStore;
import org.openbooth.storage.StorageHandler;
import org.openbooth.storage.exception.StorageException;
import org.openbooth.util.FileTransfer;
import org.openbooth.storage.exception.ConfigStoreException;
import org.openbooth.config.validation.ConfigValidator;
import org.openbooth.config.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Properties;

/**
 * THIS CLASS IS NOT THREAD SAFE FOR CONCURRENT WRITE OPERATIONS!
 */
@Component
public class PropertiesConfigStore implements ConfigStore, ReadOnlyConfigStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesConfigStore.class);
    private static final String CONFIG_FOLDER_NAME = "config";
    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String RESOURCES_DEFAULT_CONFIG_FILE_PATH = "/config/defaults.properties";

    private String configFilePath;
    private Properties readStorageProperties;
    private Properties writeStorageProperties;

    private ConfigValidator configValidator;

    private boolean autoCommitEnabled = true;


    @Autowired
    private PropertiesConfigStore(StorageHandler storageHandler, ConfigValidator configValidator) throws StorageException {
            this.configValidator = configValidator;

            configFilePath = storageHandler.getPathForFolder(CONFIG_FOLDER_NAME) + "/" + CONFIG_FILE_NAME;

            createConfigFileIfItDoesNotExists(storageHandler);

            /*
            redStorageProperties and writeStorageProperties must be different objects
            to make sure that validation is done before reading the new values
            and to enable rollbacks and transactions
             */

            readStorageProperties = new Properties();
            loadProperties(readStorageProperties, configFilePath);
            writeStorageProperties = new Properties();
            loadProperties(writeStorageProperties, configFilePath);

    }

    @PostConstruct
    private void validateKeyValueStore() throws ValidationException, ConfigStoreException {
        configValidator.validate(this);
    }

    private void createConfigFileIfItDoesNotExists(StorageHandler storageHandler) throws StorageException {
        try{
            if(!storageHandler.checkIfFileExistsInFolder(CONFIG_FOLDER_NAME, CONFIG_FILE_NAME)) {
                LOGGER.debug("config file does not exist, trying to create new config file at {}", configFilePath);
                FileTransfer.transfer(RESOURCES_DEFAULT_CONFIG_FILE_PATH, configFilePath);
            }
        } catch (IOException e) {
            throw new StorageException(e);
        }

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
        try(Reader reader = new InputStreamReader(PropertiesConfigStore.class.getResourceAsStream(path))){
            properties.load(reader);
            LOGGER.debug("config loaded from resources at {}", path);
        }catch (IOException e){
            throw new StorageException(e);
        }
    }

    @Override
    public void commit() throws StorageException, ValidationException, ConfigStoreException {
        configValidator.validate(this);
        try (Writer writer = new FileWriter(configFilePath)){
            writeStorageProperties.store(writer,null);
            readStorageProperties = writeStorageProperties;
            writeStorageProperties = new Properties();
            loadProperties(writeStorageProperties, configFilePath);
        } catch (IOException e) {
            throw new StorageException(e);
        }

    }

    @Override
    public void rollBack(){
        writeStorageProperties = readStorageProperties;
    }


    @Override
    public void put(String key, String value) throws StorageException, ValidationException, ConfigStoreException {
        writeStorageProperties.setProperty(key,value);
        if(autoCommitEnabled) commit();
        LOGGER.trace("Key {} with value {} stored.", key, value);
    }

    @Override
    public void put(String key, int value) throws StorageException, ValidationException, ConfigStoreException {
        writeStorageProperties.setProperty(key, Integer.toString(value));
        if(autoCommitEnabled) commit();
        LOGGER.trace("Key {} with value {} stored.", key, value);
    }

    @Override
    public void put(String key, double value) throws StorageException, ValidationException, ConfigStoreException {
        writeStorageProperties.setProperty(key, Double.toString(value));
        if(autoCommitEnabled) commit();
        LOGGER.trace("Key {} with value {} stored.", key, value);
    }

    @Override
    public void put(String key, boolean value) throws StorageException, ValidationException, ConfigStoreException {
        writeStorageProperties.setProperty(key, Boolean.toString(value));
        if(autoCommitEnabled) commit();
        LOGGER.trace("Key {} with value {} stored.", key, value);
    }

    private String getValue(String key) throws ConfigStoreException {
        String value = readStorageProperties.getProperty(key);
        if (value == null) throw new ConfigStoreException("No value for key \"" + key + "\" could be found.");
        return value;
    }

    @Override
    public String getString(String key) throws ConfigStoreException {
        String value = getValue(key);
        LOGGER.trace("Key {} with value {} retrieved.", key, value);
        return value;
    }


    @Override
    public int getInt(String key) throws ConfigStoreException {
        try {
            int value = Integer.parseInt(getValue(key));
            LOGGER.trace("Key {} with value {} retrieved.", key, value);
            return value;
        } catch (NumberFormatException e) {
            throw new ConfigStoreException("Error when retrieving integer property.", e);
        }
    }

    @Override
    public double getDouble(String key) throws ConfigStoreException {
        try {
            double value = Double.parseDouble(getValue(key));
            LOGGER.trace("Key {} with value {} retrieved.", key, value);
            return value;
        } catch (NumberFormatException e) {
            throw new ConfigStoreException("Error when retrieving double property.", e);
        }
    }

    @Override
    public boolean getBoolean(String key) throws ConfigStoreException {
        String valueString = getValue(key);
        if(!valueString.equalsIgnoreCase("true") && !valueString.equalsIgnoreCase("false")){
            throw new ConfigStoreException("Error when retrieving boolean property '" + key + "': Should be either 'true' or 'false' but was '" + valueString + "'");
        }
        boolean value = Boolean.parseBoolean(valueString);
        LOGGER.trace("Key {} with value {} retrieved.", key, value);
        return value;
    }

    @Override
    public void restoreDefaultProperties() throws StorageException, ValidationException, ConfigStoreException {
        Properties defaultProperties = new Properties();
        loadPropertiesFromResources(defaultProperties, RESOURCES_DEFAULT_CONFIG_FILE_PATH);

        writeStorageProperties = defaultProperties;

        if(autoCommitEnabled) commit();
        LOGGER.debug("restored default config to  {}", configFilePath);

    }

    @Override
    public void setAutoCommit(boolean setAutoCommitEnabled) throws StorageException{
        if(setAutoCommitEnabled == autoCommitEnabled) return;

        if(!setAutoCommitEnabled && autoCommitEnabled){
            writeStorageProperties = new Properties();
            loadProperties(writeStorageProperties, configFilePath);
        }

        if(setAutoCommitEnabled && !autoCommitEnabled){
            readStorageProperties = writeStorageProperties;
        }

        autoCommitEnabled = setAutoCommitEnabled;
    }


}
