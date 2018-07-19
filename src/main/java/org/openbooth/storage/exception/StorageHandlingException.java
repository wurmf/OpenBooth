package org.openbooth.storage.exception;

/**
 *  Exceptionclass that is thrown if an error occurs while handling storages in any way.
 */
public class StorageHandlingException extends Exception{

    public StorageHandlingException(String message){
        super(message);
    }

    public StorageHandlingException(Throwable e){
        super(e);
    }

    public StorageHandlingException(String message, Throwable e){
        super(message, e);
    }
}
