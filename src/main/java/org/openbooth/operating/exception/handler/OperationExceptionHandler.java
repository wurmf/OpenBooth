package org.openbooth.operating.exception.handler;

import org.openbooth.operating.exception.OperationException;
import org.openbooth.operating.exception.StopExecutionException;

public interface OperationExceptionHandler {

    void handleOperationException(OperationException e) throws StopExecutionException;

}
