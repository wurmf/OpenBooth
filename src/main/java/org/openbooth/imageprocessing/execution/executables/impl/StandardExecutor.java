package org.openbooth.imageprocessing.execution.executables.impl;

import org.openbooth.imageprocessing.exception.ProcessingException;
import org.openbooth.imageprocessing.exception.StopExecutionException;
import org.openbooth.imageprocessing.exception.handler.ProcessingExceptionHandler;
import org.openbooth.imageprocessing.execution.executables.Executable;
import org.openbooth.imageprocessing.execution.executables.Executor;

import java.util.List;

public class StandardExecutor implements Executor {

    private ProcessingExceptionHandler exceptionHandler;
    private List<Executable> executables;

    public StandardExecutor(List<Executable> executables, ProcessingExceptionHandler exceptionHandler){
        this.exceptionHandler = exceptionHandler;
        this.executables = executables;
    }

    public void execute() throws StopExecutionException {
        try {
            for(Executable executable : executables){
                executable.execute();
            }
        } catch (ProcessingException e) {
            exceptionHandler.handleProcessingException(e);
        }
    }
}
