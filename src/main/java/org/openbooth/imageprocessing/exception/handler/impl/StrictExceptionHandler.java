package org.openbooth.imageprocessing.exception.handler.impl;

import org.openbooth.gui.ShotFrameController;
import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.exception.handler.ProcessingExceptionHandler;
import org.openbooth.util.ImageHandler;
import org.openbooth.util.exceptions.ImageHandlingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Component
public class StrictExceptionHandler implements ProcessingExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StrictExceptionHandler.class);

    private static final int TOLERATED_EXCEPTION_COUNT = 0;

    private final Map<Class,Integer> exceptionCounter = new HashMap<>();

    private final BufferedImage errorMessage;


    private ShotFrameController shotFrameController;

    @Autowired
    public StrictExceptionHandler(ImageHandler imageHandler, ShotFrameController shotFrameController) throws ImageHandlingException {
        errorMessage = imageHandler.openImage(this.getClass().getResource("/images/error_message.png").getPath());
        this.shotFrameController = shotFrameController;
    }

    @Override
    public void handleProcessingException(ProcessingException e) throws StopExecutionException {

        if(getCausingExceptionCount(e) > TOLERATED_EXCEPTION_COUNT){
            showFatalErrorMessageToUser();
            LOGGER.error("FATAL: Exception count for following exception exceeded limit of {}", TOLERATED_EXCEPTION_COUNT, e);
            throw new StopExecutionException();
        } else {
            showErrorMessageToUser();
            LOGGER.error("Error during execution of processors", e);
        }
    }

    private int getCausingExceptionCount(ProcessingException e){
        Class causingExceptionClass = e.getCause() == null ? e.getClass() : e.getCause().getClass();
        if(!exceptionCounter.containsKey(causingExceptionClass)){
            exceptionCounter.put(causingExceptionClass, 1);
            return 1;
        } else {
          return exceptionCounter.get(causingExceptionClass);
        }
    }

    private void showFatalErrorMessageToUser(){
        shotFrameController.refreshShot(errorMessage);
    }

    private void showErrorMessageToUser(){
        //This is not used at the moment and will be used later
    }
}
