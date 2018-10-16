package org.openbooth.storage;

import org.openbooth.storage.exception.PersistenceException;


/**
 * This interface represents a key-value store where data can be persisted.
 * Values are stored and retrieved using a key
 */
public interface KeyValueStore {

    /**
     * Stores the given key with the given value
     * @param key the given key
     * @param value the given value
     * @throws PersistenceException the data store could not be accessed
     */
    void put(String key, String value) throws PersistenceException;

    /**
     * Stores the given key with the given value
     * @param key the given key
     * @param value the given value
     * @throws PersistenceException the data store could not be accessed
     */
    void put(String key, int value) throws PersistenceException;

    /**
     * Stores the given key with the given value
     * @param key the given key
     * @param value the given value
     * @throws PersistenceException the data store could not be accessed
     */
    void put (String key, double value) throws PersistenceException;

    /**
     * Retrieves the value for the given key
     * @param key the given key
     * @return the stored value for the given key,
     * if no value is stored, the default value will be returned.
     * @throws PersistenceException If no default value is found, a PersistenceException is thrown
     */
    String getString(String key) throws PersistenceException;

    /**
     * Retrieves the int value for the given key
     * @param key the given key
     * @return the stored int value for the given key,
     * if no value is stored, the default value will be returned.
     * @throws PersistenceException If no default value is found
     * or the value could not be converted to an integer, a PersistenceException is thrown
     */
    int getInt(String key) throws PersistenceException;

    /**
     * Retrieves the double value for the given key
     * @param key the given key
     * @return the stored double value for the given key,
     * if no value is stored, the default value will be returned.
     * @throws PersistenceException If no default value is found
     * or the value could not be converted to a double, a PersistenceException is thrown
     */
    double getDouble(String key) throws PersistenceException;

    /**
     * Restores the default values for all values in the key-value store.
     * @throws PersistenceException If either the data store for the default values could not be accessed or
     * one of the default values could not be restored
     */
    void restoreDefaultProperties() throws PersistenceException;
}
