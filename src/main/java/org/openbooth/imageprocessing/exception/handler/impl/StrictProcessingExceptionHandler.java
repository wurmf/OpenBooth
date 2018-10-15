package org.openbooth.imageprocessing.exception.handler.impl;

import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.exception.handler.ProcessingExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StrictProcessingExceptionHandler implements ProcessingExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StrictProcessingExceptionHandler.class);

    private static final int TOLERATED_EXCEPTION_COUNT = 0;

    private final Map<Class,Integer> exceptionCounter = new HashMap<>();

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

    private void showFatalErrorMessageToUser(){}

    private void showErrorMessageToUser(){

    }
}
