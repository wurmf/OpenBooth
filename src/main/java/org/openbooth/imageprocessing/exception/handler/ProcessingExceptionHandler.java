package org.openbooth.imageprocessing.exception.handler;

import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.exception.StopExecutionException;

public interface ProcessingExceptionHandler {

    void handleProcessingException(ProcessingException e) throws StopExecutionException;

}
