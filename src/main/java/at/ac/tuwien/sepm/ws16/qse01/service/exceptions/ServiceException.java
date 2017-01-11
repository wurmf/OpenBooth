package at.ac.tuwien.sepm.ws16.qse01.service.exceptions;

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
