package org.openbooth.operating.exception;

public class OperationException extends Exception {

    public OperationException(Exception e){
        super(e);
    }

    public OperationException(String message){
        super(message);
    }

    public OperationException(String message, Exception e){
        super(message, e);
    }

}
