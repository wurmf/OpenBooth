package org.openbooth.config.validation;

import org.openbooth.config.keys.BooleanKey;
import org.openbooth.config.keys.IntegerKey;
import org.openbooth.config.keys.StringKey;
import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.exception.KeyValueStoreException;
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
     * @param keyValueStore the given key value store
     * @throws ValidationException if a value is invalid or a folder could not be created
     */
    public void validate(KeyValueStore keyValueStore) throws ValidationException, KeyValueStoreException {
        validateFolders(keyValueStore);
        validateNumbers(keyValueStore);
        validateBooleans(keyValueStore);
        LOGGER.trace("Configs successfully validated");
    }

    private void validateFolders(KeyValueStore keyValueStore) throws ValidationException, KeyValueStoreException{
        List<String> folderKeys = Collections.singletonList(StringKey.IMAGE_FOLDER.key);
        for(String key : folderKeys){
            try {
                String folderPath = keyValueStore.getString(key);
                fileHelper.createFolderIfItDoesNotExist(folderPath);
            } catch (FileHandlingException e) {
                throw new ValidationException("error while trying to create folder for key '" + key + "' from config file", e);
            }
        }
    }

    private void validateNumbers(KeyValueStore keyValueStore) throws ValidationException, KeyValueStoreException{

        for(IntegerKey integerKey : IntegerKey.values()){
            if(keyValueStore.getInt(integerKey.key) <= integerKey.infimum) throw new ValidationException(integerKey.validationErrorMessage);
        }
    }

    private void validateBooleans(KeyValueStore keyValueStore) throws KeyValueStoreException{
        for(BooleanKey booleanKey : BooleanKey.values()){
            keyValueStore.getBoolean(booleanKey.key);
        }
    }

}
