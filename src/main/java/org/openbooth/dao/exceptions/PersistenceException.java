package org.openbooth.dao.exceptions;

/**
 * Exception thrown by classes of the Persistence-layer.
 */
public class PersistenceException extends Exception {
    public PersistenceException(String message){
        super(message);
    }
    public PersistenceException(Throwable e){
        super(e);
    }
    public PersistenceException(String message, Throwable e){super(message, e);}
}
