package org.openbooth.storage;

import org.openbooth.storage.exception.StorageException;
import org.openbooth.storage.exception.KeyValueStoreException;
import org.openbooth.config.validation.ValidationException;


/**
 * This interface represents a configuration store where data can be persisted.
 * Values are stored and retrieved using a key
 */
public interface ConfigStore {

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
    void put(String key, double value) throws StorageException, ValidationException, KeyValueStoreException;

    /**
     * Stores the given key with the given value
     * @param key the given key
     * @param value the given value
     * @throws StorageException the data store could not be accessed
     * @throws ValidationException if the values of the key-value store are not valid after the operation
     * @throws KeyValueStoreException if a to-be-validated value could not be found
     */
    void put(String key, boolean value) throws StorageException, ValidationException, KeyValueStoreException;

    /**
     * Retrieves the value for the given key
     * @param key the given key
     * @return the stored value for the given key,
     * @throws KeyValueStoreException If no value for the given key was found
     */
    String getString(String key) throws KeyValueStoreException;

    /**
     * Retrieves the int value for the given key
     * @param key the given key
     * @return the stored int value for the given key,
     * @throws KeyValueStoreException If the value could not be converted to an integer or no value for the given key was found
     */
    int getInt(String key) throws KeyValueStoreException;

    /**
     * Retrieves the double value for the given key
     * @param key the given key
     * @return the stored double value for the given key,
     * @throws KeyValueStoreException If no value for the given key is found
     * or the value could not be converted to a double
     */
    double getDouble(String key) throws KeyValueStoreException;

    /**
     * Retrieves the boolean value for the given key
     * @param key the given key
     * @return the stored boolean value for the given key,
     * @throws KeyValueStoreException If no value for the given key is found
     * or the value could not be converted to a boolean
     */
    boolean getBoolean(String key) throws KeyValueStoreException;

    /**
     * Restores the default values for all values in the key-value store.
     * @throws StorageException If either the data store for the default values could not be accessed or
     * one of the default values could not be restored
     * @throws ValidationException If one of the default values is invalid or a folder referenced by a default value could no tbe created
     * @throws KeyValueStoreException If a to-be-validated value could not be found
     */
    void restoreDefaultProperties() throws StorageException, ValidationException, KeyValueStoreException;

    /**
     * Validates and writes out all changes made to the key-value store since last commit.
     * @throws StorageException If the data store could not be accessed
     * @throws ValidationException If any of the given values is invalid or a folder referenced by a default value could not be created
     * @throws KeyValueStoreException If a to-be-validated value could not be found
     */
    void commit() throws StorageException, ValidationException, KeyValueStoreException;

    /**
     * Undoes all uncommited changes made since the last commit
     */
    void rollBack();

    /**
     * Change if a commit should be made after every change to the key-value store
     * @param autoCommit Enable or disable auto commit
     * @throws StorageException If the data store could not be accessed
     */
    void setAutoCommit(boolean autoCommit) throws StorageException;

}
