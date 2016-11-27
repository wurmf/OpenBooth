package at.ac.tuwien.sepm.ws16.qse01.service.exceptions;

/**
 * Created by Martin Schroeder on 27.11.16.
 */
public class ServiceException extends Exception {
    public ServiceException(String message){
        super(message);
    }
    public ServiceException(Exception e){
        super(e);
    }
}
