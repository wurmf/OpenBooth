package org.openbooth.service.exceptions;

/**
 * Exception thrown by classes of the Service-layer.
 */
public class ServiceException extends Exception {
    public ServiceException(String message){
        super(message);
    }
    public ServiceException(Throwable e){
        super(e);
    }
    public ServiceException(String message, Throwable e){super(message,e);}
}
