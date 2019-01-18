package org.openbooth.imageprocessing.execution.executor.impl;

import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.exception.handler.ProcessingExceptionHandler;
import org.openbooth.imageprocessing.execution.executables.Executable;
import org.openbooth.imageprocessing.execution.executor.Executor;

import java.util.List;

public class StandardExecutor implements Executor {

    private ProcessingExceptionHandler exceptionHandler;

    public StandardExecutor(ProcessingExceptionHandler exceptionHandler){
        this.exceptionHandler = exceptionHandler;
    }

    public void execute(List<Executable> executables) throws StopExecutionException {
        try {
            for(Executable executable : executables){
                executable.execute();
            }
        } catch (ProcessingException e) {
            exceptionHandler.handleProcessingException(e);
        }
    }
}
