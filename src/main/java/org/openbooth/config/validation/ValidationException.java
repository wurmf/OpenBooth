package org.openbooth.config.validation;

public class ValidationException extends Exception{

    ValidationException(String message){
        super(message);
    }
    ValidationException(Throwable e){
        super(e);
    }
    ValidationException(String message, Throwable e){super(message, e);}

}
