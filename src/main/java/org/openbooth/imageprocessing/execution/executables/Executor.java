package org.openbooth.imageprocessing.execution.executables;

import org.openbooth.imageprocessing.exception.StopExecutionException;

public interface Executor {

    void execute() throws StopExecutionException;
}
