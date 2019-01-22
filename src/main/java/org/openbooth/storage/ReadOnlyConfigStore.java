package org.openbooth.storage;

import org.openbooth.storage.exception.ConfigStoreException;

public interface ReadOnlyConfigStore {

    /**
     * Retrieves the value for the given key
     * @param key the given key
     * @return the stored value for the given key,
     * @throws ConfigStoreException If no value for the given key was found
     */
    String getString(String key) throws ConfigStoreException;

    /**
     * Retrieves the int value for the given key
     * @param key the given key
     * @return the stored int value for the given key,
     * @throws ConfigStoreException If the value could not be converted to an integer or no value for the given key was found
     */
    int getInt(String key) throws ConfigStoreException;

    /**
     * Retrieves the double value for the given key
     * @param key the given key
     * @return the stored double value for the given key,
     * @throws ConfigStoreException If no value for the given key is found
     * or the value could not be converted to a double
     */
    double getDouble(String key) throws ConfigStoreException;

    /**
     * Retrieves the boolean value for the given key
     * @param key the given key
     * @return the stored boolean value for the given key,
     * @throws ConfigStoreException If no value for the given key is found
     * or the value could not be converted to a boolean
     */
    boolean getBoolean(String key) throws ConfigStoreException;
}
