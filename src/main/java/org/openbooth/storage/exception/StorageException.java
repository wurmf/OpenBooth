package org.openbooth.storage.exception;

public class StorageException extends Exception{
    public StorageException(String message){
        super(message);
    }
    public StorageException(Throwable e){
        super(e);
    }
    public StorageException(String message, Throwable e){super(message,e);}
}
