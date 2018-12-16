package org.openbooth.imageprocessing.exception;

public class ProcessingException extends Exception {

    public ProcessingException(Exception e){
        super(e);
    }

    public ProcessingException(String message){
        super(message);
    }

    public ProcessingException(String message, Exception e){
        super(message, e);
    }

}
