package org.openbooth.config.key;

/**
 * This enumeration contains all keys, which represent boolean values in the configuration store.
 */
public enum ConfigBooleanKeys {

     FILTER_ENABLED("filter_enabled");

    public final String key;

    ConfigBooleanKeys(String key){
        this.key = key;
    }
}
