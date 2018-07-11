package org.openbooth;

public class TestSetUpException extends Exception{
    public TestSetUpException(String message){
        super(message);
    }
    public TestSetUpException(Throwable e){
        super(e);
    }
    public TestSetUpException(String message, Throwable e){super(message, e);}
}
