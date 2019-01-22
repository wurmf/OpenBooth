package org.openbooth.storage.exception;

public class ConfigStoreException extends Exception {
    public ConfigStoreException(String message, Throwable e){
        super(message,e);
    }
    public ConfigStoreException(String message){
        super(message);
    }
}
