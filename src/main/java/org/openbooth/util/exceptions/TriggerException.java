package org.openbooth.util.exceptions;

/**
 * This exception type is used when a problem comes up while triggering a camera.
 */

public class TriggerException extends Exception {
    public TriggerException(String message){
        super(message);
    }
    public TriggerException(Throwable e){
        super(e);
    }
    public TriggerException(String message, Throwable e){super(message, e);}
}
