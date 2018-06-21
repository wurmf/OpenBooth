package org.openbooth.util.exceptions;

public class TriggerException extends Exception {
    public TriggerException(String message){
        super(message);
    }
    public TriggerException(Throwable e){
        super(e);
    }
    public TriggerException(String message, Throwable e){super(message, e);}
}
