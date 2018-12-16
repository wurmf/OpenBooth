package org.openbooth.config.key;


/**
 * This enumeration contains all keys, which represent string values in the configuration store.
 */
public enum ConfigStringKeys {

    IMG_PREFIX ("image_prefix"),
    IMAGE_FOLDER ("image_storage_path");

    public final String key;

    ConfigStringKeys(String key){
        this.key = key;
    }

}
