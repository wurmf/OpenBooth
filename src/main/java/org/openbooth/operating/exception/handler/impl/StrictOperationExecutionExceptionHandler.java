package org.openbooth.operating.exception.handler.impl;

import org.openbooth.operating.exception.OperationExecutionException;
import org.openbooth.operating.exception.StopExecutionException;
import org.openbooth.operating.exception.handler.OperationExecutionExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StrictOperationExecutionExceptionHandler implements OperationExecutionExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StrictOperationExecutionExceptionHandler.class);

    private static final int TOLERATED_EXCEPTION_COUNT = 5;

    private final Map<Class,Integer> exceptionCounter = new HashMap<>();

    @Override
    public void handleOperationExecutionException(OperationExecutionException e) throws StopExecutionException {

        if(getCausingExceptionCount(e) > TOLERATED_EXCEPTION_COUNT){
            showFatalErrorMessageToUser();
            LOGGER.error("FATAL: Exception count for following exception exceeded limit of {}", TOLERATED_EXCEPTION_COUNT, e);
            throw new StopExecutionException();
        } else {
            showErrorMessageToUser();
            LOGGER.error("Error during execution of operations", e);
        }
    }

    private int getCausingExceptionCount(OperationExecutionException e){
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
