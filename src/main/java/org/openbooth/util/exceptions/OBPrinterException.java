package org.openbooth.util.exceptions;

/**
 * Is thrown if the printing process entails an exception.
 */
public class OBPrinterException extends Exception {
    public OBPrinterException(String message){
        super(message);
    }
    public OBPrinterException(Throwable e){
        super(e);
    }
    public OBPrinterException(String message, Throwable e){super(message, e);}
}
