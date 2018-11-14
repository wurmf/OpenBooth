package org.openbooth.util.validation;

import org.openbooth.storage.KeyValueStore;
import org.openbooth.storage.exception.KeyValueStoreException;
import org.openbooth.util.FileHelper;
import org.openbooth.util.exceptions.FileHandlingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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
        LOGGER.trace("Configs successfully validated");
    }

    private void validateFolders(KeyValueStore keyValueStore) throws ValidationException, KeyValueStoreException{
        List<String> folderKeys = Arrays.asList(KeyValueStore.IMAGE_FOLDER);
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

        if(keyValueStore.getInt(KeyValueStore.MAX_PREVIEW_REFRESH) <= 0) throw new ValidationException("Having 0 previews per second is not valid, as there will be no live preview, please change the value of '" + KeyValueStore.MAX_PREVIEW_REFRESH + "' in the config file");
        if(keyValueStore.getInt(KeyValueStore.NUM_BURST_SHOTS) <= 1) throw new ValidationException("1 or less shots in burst mode does not make sense, please change the value of '" + KeyValueStore.NUM_BURST_SHOTS + "' in the config file");
        if(keyValueStore.getInt(KeyValueStore.SHOW_SHOT_TIME) < 0) throw new ValidationException("It does not make sense to show the shot less than 0 milliseconds, please change the value of '" + KeyValueStore.SHOW_SHOT_TIME + "' in the config file");
    }

}
