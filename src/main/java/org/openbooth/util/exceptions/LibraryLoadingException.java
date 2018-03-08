package org.openbooth.util.exceptions;

/**
 * Exception class that is thrown when loading a library fails
 */
public class LibraryLoadingException extends Exception {
    public LibraryLoadingException(String message){super(message);}
    public LibraryLoadingException(Throwable e){super(e);}
    public LibraryLoadingException(String message, Throwable e){super(message, e);}
}
