package org.openbooth.operating.exceptions;

public class OperationExecutionException extends Exception {

    public OperationExecutionException(Exception e){
        super(e);
    }

    public OperationExecutionException(String message){
        super(message);
    }

    public OperationExecutionException(String message, Exception e){
        super(message, e);
    }

}
