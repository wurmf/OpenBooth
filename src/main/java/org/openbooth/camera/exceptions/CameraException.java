package org.openbooth.camera.exceptions;

public class CameraException extends Exception{
    public CameraException(Exception e){
        super(e);
    }

    public CameraException(String message){
        super(message);
    }

    public CameraException(String message, Exception e){
        super(message, e);
    }
}
