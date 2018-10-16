package org.openbooth.storage.exception;

public class KeyValueStoreException extends Exception {
    public KeyValueStoreException(String message, Throwable e){
        super(message,e);
    }
    public KeyValueStoreException(String message){
        super(message);
    }
}
