package org.openbooth.config.validation;

import org.openbooth.config.key.ConfigBooleanKeys;
import org.openbooth.config.key.ConfigIntegerKeys;
import org.openbooth.config.key.ConfigStringKeys;
import org.openbooth.storage.ReadOnlyConfigStore;
import org.openbooth.storage.exception.ConfigStoreException;
import org.openbooth.util.FileHelper;
import org.openbooth.util.exceptions.FileHandlingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * This class is used for validating the configuration
 */
@Component
public class ConfigValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigValidator.class);


    private FileHelper fileHelper;

    @Autowired
    private ConfigValidator(FileHelper fileHelper){
        this.fileHelper = fileHelper;
    }


    /**
     * Validates the values stored in the given key-value store and creates necessary folders if they do not exist
     * @param configStore the given key value store
     * @throws ValidationException if a value is invalid or a folder could not be created
     */
    public void validate(ReadOnlyConfigStore configStore) throws ValidationException, ConfigStoreException {
        validateFolders(configStore);
        validateNumbers(configStore);
        validateBooleans(configStore);
        LOGGER.trace("Configs successfully validated");
    }

    private void validateFolders(ReadOnlyConfigStore configStore) throws ValidationException, ConfigStoreException {
        List<String> folderKeys = Collections.singletonList(ConfigStringKeys.IMAGE_FOLDER.key);
        for(String key : folderKeys){
            try {
                String folderPath = configStore.getString(key);
                fileHelper.createFolderIfItDoesNotExist(folderPath);
            } catch (FileHandlingException e) {
                throw new ValidationException("error while trying to create folder for key '" + key + "' from configuration", e);
            }
        }
    }

    private void validateNumbers(ReadOnlyConfigStore configStore) throws ValidationException, ConfigStoreException {

        for(ConfigIntegerKeys integerKey : ConfigIntegerKeys.values()){
            if(configStore.getInt(integerKey.key) <= integerKey.infimum) throw new ValidationException(integerKey.validationErrorMessage);
        }
    }

    private void validateBooleans(ReadOnlyConfigStore configStore) throws ConfigStoreException {
        for(ConfigBooleanKeys booleanKey : ConfigBooleanKeys.values()){
            configStore.getBoolean(booleanKey.key);
        }
    }

}
