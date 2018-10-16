package org.openbooth.storage;

import org.openbooth.storage.exception.StorageException;
import org.openbooth.storage.exception.KeyValueStoreException;
import org.openbooth.util.validation.ValidationException;


/**
 * This interface represents a key-value store where data can be persisted.
 * Values are stored and retrieved using a key
 */
public interface KeyValueStore {

    /**
     * Stores the given key with the given value
     * @param key the given key
     * @param value the given value
     * @throws StorageException the data store could not be accessed
     * @throws ValidationException if the values of the key-value store are not valid after the operation
     * @throws KeyValueStoreException if a to-be-validated value could not be found
     */
    void put(String key, String value) throws StorageException, ValidationException, KeyValueStoreException;

    /**
     * Stores the given key with the given value
     * @param key the given key
     * @param value the given value
     * @throws StorageException the data store could not be accessed
     * @throws ValidationException if the values of the key-value store are not valid after the operation
     * @throws KeyValueStoreException if a to-be-validated value could not be found
     */
    void put(String key, int value) throws StorageException, ValidationException, KeyValueStoreException;

    /**
     * Stores the given key with the given value
     * @param key the given key
     * @param value the given value
     * @throws StorageException the data store could not be accessed
     * @throws ValidationException if the values of the key-value store are not valid after the operation
     * @throws KeyValueStoreException if a to-be-validated value could not be found
     */
    void put (String key, double value) throws StorageException, ValidationException, KeyValueStoreException;

    /**
     * Retrieves the value for the given key
     * @param key the given key
     * @return the stored value for the given key,
     * if no value is stored, the default value will be returned.
     * @throws KeyValueStoreException If no default value is found
     */
    String getString(String key) throws KeyValueStoreException;

    /**
     * Retrieves the int value for the given key
     * @param key the given key
     * @return the stored int value for the given key,
     * if no value is stored, the default value will be returned.
     * @throws KeyValueStoreException If no default value is found
     * or the value could not be converted to an integer
     */
    int getInt(String key) throws KeyValueStoreException;

    /**
     * Retrieves the double value for the given key
     * @param key the given key
     * @return the stored double value for the given key,
     * if no value is stored, the default value will be returned.
     * @throws KeyValueStoreException If no default value is found
     * or the value could not be converted to a double
     */
    double getDouble(String key) throws KeyValueStoreException;

    /**
     * Restores the default values for all values in the key-value store.
     * @throws PersistenceException If either the data store for the default values could not be accessed or
     * one of the default values could not be restored
     */
    void restoreDefaultProperties();

}
