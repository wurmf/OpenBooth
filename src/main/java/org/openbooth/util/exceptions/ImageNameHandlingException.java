package org.openbooth.util.exceptions;

public class ImageNameHandlingException extends Exception {

    public ImageNameHandlingException(Throwable t){
        super(t);
    }

    public ImageNameHandlingException(String message, Throwable t){
        super(message, t);
    }

}
