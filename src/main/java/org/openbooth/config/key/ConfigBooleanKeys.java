package org.openbooth.config.key;

/**
 * This enumeration contains all keys, which represent boolean values in the configuration store.
 */
public enum ConfigBooleanKeys {

    TIMED_SHOT_ACTIVATED ("timed_shot_activated");

    public final String key;

    ConfigBooleanKeys(String key){
        this.key = key;
    }
}
