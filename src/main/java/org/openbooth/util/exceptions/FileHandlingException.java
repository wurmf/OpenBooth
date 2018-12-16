package org.openbooth.util.exceptions;

public class FileHandlingException extends Exception {

    public FileHandlingException(String message){
        super(message);
    }
    public FileHandlingException(Throwable e){
        super(e);
    }
    public FileHandlingException(String message, Throwable e){super(message, e);}

}
