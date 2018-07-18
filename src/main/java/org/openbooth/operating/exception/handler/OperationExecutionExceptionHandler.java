package org.openbooth.operating.exception.handler;

import org.openbooth.operating.exception.OperationExecutionException;
import org.openbooth.operating.exception.StopExecutionException;

public interface OperationExecutionExceptionHandler {

    void handleOperationExecutionException(OperationExecutionException e) throws StopExecutionException;

}
