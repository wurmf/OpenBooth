package org.openbooth.util;

import org.openbooth.config.key.ConfigStringKeys;
import org.openbooth.storage.ConfigStore;
import org.openbooth.storage.exception.KeyValueStoreException;
import org.openbooth.util.exceptions.ImageNameHandlingException;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ImageNameHandler {

    private ConfigStore configStore;

    public ImageNameHandler(ConfigStore configStore){
        this.configStore = configStore;
    }


    public String getNewImageName() throws ImageNameHandlingException {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            return configStore.getString(ConfigStringKeys.IMG_PREFIX.key) + "_" + now.format(formatter);
        } catch (KeyValueStoreException e) {
            throw new ImageNameHandlingException(e);
        }catch (DateTimeException e){
            throw new ImageNameHandlingException("Error during parsing date formatting string", e);
        }
    }
}
