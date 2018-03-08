package org.openbooth.util.exceptions;

/**
 * Exceptionclass that is thrown if an error occurs while handling images in any way.
 */
public class ImageHandlingException extends Exception {
    public ImageHandlingException(String message){
        super(message);
    }
    public ImageHandlingException(Throwable e){
        super(e);
    }
    public ImageHandlingException(String message, Throwable e){super(message, e);}
}